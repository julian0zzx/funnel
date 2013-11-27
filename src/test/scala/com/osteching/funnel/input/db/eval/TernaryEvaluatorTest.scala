/**
 *
 */
package com.osteching.funnel.input.db.eval;

import scala.collection.mutable.Map

import org.junit.Assert.assertEquals

import junit.framework.TestCase
/**
 * @author Zhenxing Zhu
 *
 */
class TernaryEvaluatorTest extends TestCase {

  val map: Map[String, Object] = Map()

  val event1 = 286765896L
  val expr1 = "(( (0==ustat) && ( (2==ushifenstatid) || (3==ushifenstatid) || (6==ushifenstatid) ) && (0==sfstattransfer) )" +
    "&& ( ( (0==utype) && (balance>0) ) || ( (1==utype) && (dummyaccount>0) ) ))" +
    " ? 0 : 1 "

  val event2 = 286765897L
  val expr2 = "(( (0==ustat) && ( (2==ushifenstatid) || (3==ushifenstatid) || (6==ushifenstatid) ) && (0==sfstattransfer) )" +
    "&& ( ( (0==utype) && (balance>0) ) || ( (1==utype) && (dummyaccount>0) ) ))" +
    " ? 0 : 1 "

  def testEval() {
    map.put("one", "111")
    map.put("two", "222")
    assertEquals("0", TernaryEvaluator.eval(map.toMap, "(one == two) ? 1 : 0"))
  }

  def testEval2() {
    val expr = "(((1==a)||(2==b)) && (3==c))" + " ? 1 : 0"
    map.put("one", "111")
    map.put("two", "222")
    map.put("a", Int.box(2))
    map.put("b", Int.box(2))
    map.put("c", Int.box(2))
    assertEquals("0", TernaryEvaluator.eval(map.toMap, expr))
  }

  def testParseExpr() {
    val ee = TernaryEvaluator.parseOpr("(a == b)", 1)
    assertEquals("==", ee)
  }

  def testParseExpr2() {
    val expr = "((1==a)||(2==b))"
    val ee = TernaryEvaluator.parseOpr(expr, expr.count(c => c == '('))
    assertEquals("||", ee)
  }

  def testParseExpr3() {
    val expr = "(((1==a)||(2==b)) && (3==c))"
    val ee = TernaryEvaluator.parseOpr(expr, expr.count(c => c == '('))
    assertEquals("&&", ee)
  }

  def testParseExpr4() {
    val expr = "( ( (0==ustat) && ( (2==ushifenstatid) || (3==ushifenstatid) || (6==ushifenstatid) ) && (0==sfstattransfer) )" +
      " && ( ( (0==utype) && (balance>0) ) || ( (1==utype) && (dummyaccount>0) ) ) )"
    val ee = TernaryEvaluator.parseOpr(expr, expr.count(c => c == '('))
    assertEquals("&&", ee)
  }
  
}

