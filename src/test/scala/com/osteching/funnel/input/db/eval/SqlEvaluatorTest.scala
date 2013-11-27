/**
 *
 */
package com.osteching.funnel.input.db.eval;

import java.math.BigInteger

import scala.collection.mutable.Map

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull

import com.osteching.funnel.cfg.Cfg
import com.osteching.funnel.input.MysqlDataSource
import com.osteching.funnel.input.db.DataSourceHolder
/**
 * @author Zhenxing Zhu
 *
 */
class SqlEvaluatorTest /*extends TestCase*/ {

  val cfg = new Cfg("src/test/resources/funnel.xml")

  val chanCfg = cfg.getChannels.iterator.next

  val map: Map[String, Object] = Map()

  def testEval() {
    map.put("eventid", "286767229")
    val expr = "select level from BD_Output.bdevent where eventid = :eventid"
    assertNotNull(SqlEvaluator.eval(map.toMap, expr))
  }

  def testEval2() {
    map.put("eventid", "286767229")
    val expr = "select eventid from BD_Output.bdevent where eventid = :eventid"
    assertEquals(BigInteger.valueOf(286767229L), SqlEvaluator.eval(map.toMap, expr))
  }

  def testMakeSql() {
    val expr = "select name from user where uid = :uid and addr = :addr"
    val (sql, params) = SqlEvaluator.makeSql(expr)
    assertEquals("select name from user where uid = ? and addr = ?", sql)
    assertEquals(2, params.size)
    assertEquals("uid", params(0))
    assertEquals("addr", params(1))
  }

  def testMakeSqlTrim() {
    val expr = "select name from user where uid = :uid and addr = :addr   "
    val (sql, params) = SqlEvaluator.makeSql(expr)
    assertEquals("select name from user where uid = ? and addr = ?", sql)
    assertEquals(2, params.size)
    assertEquals("uid", params(0))
    assertEquals("addr", params(1))
  }

  /*override*/ def setUp() {
    val dsCfg = chanCfg \ "input" \ "datasource"
    val url = (dsCfg \ "url").text
    val user = (dsCfg \ "user").text
    val pwd = (dsCfg \ "password").text
    val datasource = new MysqlDataSource(url, user, pwd)
    DataSourceHolder.hold(datasource)
  }

}
