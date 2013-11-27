/**
 *
 */
package com.osteching.funnel.input.db

import java.io.File
import java.sql.Connection
import java.sql.ResultSet
import java.util.concurrent.BlockingQueue

import scala.collection.immutable.SortedMap
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map
import scala.xml._
import scala.xml.NodeSeq

import com.osteching.funnel.Brick
import com.osteching.funnel.input.AbstractInput
import com.osteching.funnel.input.MysqlDataSource
import com.osteching.funnel.input.db.eval.BasicEvaluator
import com.osteching.funnel.input.db.eval.Evaluator
import com.osteching.funnel.input.db.eval.ExprEvaluator
import com.osteching.funnel.input.db.eval.SqlEvaluator
import com.osteching.funnel.input.db.eval.TernaryEvaluator
import com.osteching.funnel.util.WaterMarkUtil

/**
 * @author Zhenxing Zhu
 *
 */
class DbInput extends AbstractInput {

  private var datasource: MysqlDataSource = null

  // internal variables
  private var wmFile: String = null
  private val buffer = new ListBuffer[Map[String, Object]]
  private var inputSql: String = null
  private var limitNum: Int = 10000
  private val columnMap: Map[String, String] = Map()
  // [level_id, [idx,eval]]
  private val levelMap: Map[Int, Map[Int, DbField]] = Map()

  def loadDataSource(dsType: String, url: String, user: String, pwd: String): Unit = {
    dsType match {
      case "db" => {
        datasource = new MysqlDataSource(url, user, pwd)
      }
      case _ => {
        throw new UnsupportedOperationException("---UNKNOWN Data Source---")
      }
    }
  }

  def loadCfg(cfgFile: String): Unit = {
    val cfg = XML.load(cfgFile)

    // get db configuration folder
    val cfgFolder = cfgFile.substring(0, cfgFile.lastIndexOf("/"))
    wmFile = cfgFolder + (cfg \ "watermark" \ "@file").text

    // import tcontroloop
    val tcontroloopFile = (cfg \ "tcontrloop" \ "import" \ "@src").text
    val tcontroloopCfg = XML.load(cfgFolder + tcontroloopFile)

    // limitation
    inputSql = (tcontroloopCfg \ "profile" \ "@query_sql").text
    limitNum = (tcontroloopCfg \ "profile" \ "@read_num").text.toInt
    // bdevent columns
    val columns = tcontroloopCfg \ "event_meta_data" \ "column"
    for (c <- columns) {
      columnMap.put((c \ "@name").text, (c \ "@type").text)
    }

    // load all level into map<key,eval_func>
    val levels = cfg \ "levels" \ "level"
    for (l <- levels) {
      val levelId = (l \ "@id").text.toInt
      val levelCfg = XML.load(cfgFolder + (l \ "@mapping").text)
      if (!levelId.equals((levelCfg \ "@level").text.toInt)) {
        throw new IllegalArgumentException("id is unconformant of level " + levelId)
      }
      val fields = levelCfg \ "field"
      val fieldMap: Map[Int, DbField] = Map()
      for (f <- fields) {
        var eval: Evaluator = null
        val et = (f \ "eval" \ "@type").text
        val e = (f \ "eval").text
        et match { // (basic|expr|ternary|sql)
          case "expr" => eval = ExprEvaluator
          case "ternary" => eval = TernaryEvaluator
          case "sql" => {
            eval = SqlEvaluator
            // hold data source for this thread
            DataSourceHolder.hold(datasource)
          }
          case _ => eval = BasicEvaluator
        }
        val idx = (f \ "@idx").text.toInt
        fieldMap.put(idx, new DbField(idx, e, eval))
      }
      levelMap.put(levelId, fieldMap)
    }
  }

  // a row in tcontroloop will be transformed to a map<key,value>
  private def readFromEvent(): java.io.Serializable = {
    val ps = datasource.connect.prepareStatement(inputSql)
    val wmf = new File(wmFile)
    var wm: Long = WaterMarkUtil.readWm(wmf).toString.toLong
    ps.setLong(1, wm)
    ps.setInt(2, limitNum)
    val rs = ps.executeQuery()

    var lastEventId = 0L
    while (rs.next) {
      lastEventId = rs.getLong("eventid")
      buffer.append(transform(rs))
    }

    rs.close
    ps.close

    WaterMarkUtil.writeWm(wmf, lastEventId)
    lastEventId
  }

  private def transform(rs: ResultSet): Map[String, Object] = {
    val row = Map[String, Object]()
    for ((k, v) <- columnMap) {
      var value: Object = null
      v match {
        case "int" => value = Integer.valueOf(rs.getInt(k))
        case "unsigned int" => value = java.lang.Long.valueOf(rs.getLong(k))
        case "long long" => value = java.lang.Long.valueOf(rs.getLong(k))
        case "unsigned long long" => value = rs.getBigDecimal(k)
        case _ => {
          value = rs.getObject(k)
          // TODO log.warn("---unknown type for " + k)
        }
      }
      row.put(k, value)
    }
    row
  }

//  /**
//   * read to queue
//   */
//  def read(): java.io.Serializable = {
//    // a row in tcontroloop will be transformed to a map<key,value>
//    var eventId = readFromEvent()
//
//    // fetch properties
//    if (!buffer.isEmpty) {
//      for (row <- buffer) {
//        var brick = buildBrick(row)
//        queue.add(brick)
//      }
//      buffer.clear
//    }
//
//    eventId
//  }

  def internalRead(): List[Brick] = {
    val eventId = readFromEvent()
    val bricks = ListBuffer[DbBrick]()
    // fetch properties
    if (!buffer.isEmpty) {
      for (row <- buffer) {
        val brick = buildBrick(row)
        queue.add(brick)
        bricks.append(brick)
      }
      buffer.clear
    }
    bricks.toList
  }

  /**
   * this method will go thru all levels
   */
  private def buildBrick(rowMap: Map[String, Object]): DbBrick = {
    val eid = rowMap("eventid").toString.toLong
    val level = rowMap("level").toString.toInt

    val sb: StringBuilder = new StringBuilder()
    if (!levelMap.contains(level)) {
      throw new UnsupportedOperationException("---unsupported level " + level)
    }
    // this will throw exception if no key is given level 
    val fieldMap: Map[Int, DbField] = levelMap(level)
    // sort it by idx
    val sortedMap = SortedMap[Int, DbField]() ++ fieldMap
    for ((_, v) <- fieldMap) {
      sb.append(v.eval.eval(rowMap.toMap, v.expr))
      sb.append(" ")
    }

    new DbBrick(eid, sb.toString)
  }

}
