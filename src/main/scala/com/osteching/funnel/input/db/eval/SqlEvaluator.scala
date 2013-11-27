/**
 *
 */
package com.osteching.funnel.input.db.eval

import scala.collection.immutable.Map
import scala.collection.mutable.ListBuffer.apply
import scala.collection.mutable.ListBuffer
import java.sql.Connection
import com.osteching.funnel.input.db.DataSourceHolder

/**
 * Sql evaluator retrieves value according to expression as sql
 *
 * @author Zhenxing Zhu
 *
 */
object SqlEvaluator extends Evaluator {

  /**
   * @row map holding values
   * @param expr: select name from user where uid = :uid or addr = :addr
   */
  def eval(row: Map[String, Object], expr: String): Object = {
    val ds = DataSourceHolder.get
    val conn = ds.connect.asInstanceOf[Connection]
    val (sql, params) = makeSql(expr)
    val ps = conn.prepareStatement(sql)
    var i = 0
    for (p <- params) {
      i += 1
      ps.setObject(i, row.get(p).get)
    }
    val rs = ps.executeQuery()
    if (rs.next()) {
      return rs.getObject(1)
    }
    null
  }

  def makeSql(expr: String): (String, List[String]) = {
    val originalSql = expr.trim()
    val params = ListBuffer[String]()
    val parts = originalSql.split(":")
    var idx = 0
    for (p <- parts) {
      idx += 1
      if (1 != idx) {
        var r = 0
        if (p.indexOf(" ") >= 0) {
          r = p.indexOf(' ')
        } else {
          r = p.length
        }
        params.append(p.substring(0, r))
      }
    }
    (originalSql.replaceAll(":\\w+", "?"), params.toList)
  }

}
