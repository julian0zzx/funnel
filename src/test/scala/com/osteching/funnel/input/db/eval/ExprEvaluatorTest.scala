/**
 *
 */
package com.osteching.funnel.input.db.eval;

import scala.collection.mutable.Map

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

import junit.framework.TestCase
/**
 * @author Zhenxing Zhu
 *
 */
class ExprEvaluatorTest extends TestCase {

  var map: Map[String, Object] = Map()

  def testEvalStringParenthsisException() {
    map.put("hello", "^world^")
    map.put("tom", "from USA")
    try {
      ExprEvaluator.eval(map.toMap, "(hello + tom")
    } catch {
      case e: Exception => assertTrue(true)
    }
  }

  def testEvalStringParenthsis() {
    map.put("hello", "^world^")
    map.put("tom", "from USA")
    assertEquals("^world^from USA", ExprEvaluator.eval(map.toMap, "(hello + tom)"))
  }

  def testEvalIntAdd() {
    map.put("one", Int.box(1))
    map.put("two", Int.box(2))
    assertEquals(3L, ExprEvaluator.eval(map.toMap, "one + two"))
  }

  def testEvalDoubleAdd() {
    map.put("one_point_two", Double.box(1.2))
    map.put("two", Int.box(2))
    assertEquals(3.2, ExprEvaluator.eval(map.toMap, "one_point_two + two"))
  }

  def testEvalIntSubtract() {
    map.put("one", Int.box(1))
    map.put("two", Int.box(2))
    assertEquals(-1L, ExprEvaluator.eval(map.toMap, "one - two"))
  }

  def testEvalDoubleSubtract() {
    map.put("one_point_two", Double.box(1.2))
    map.put("two", Int.box(2))
    assertEquals(-0.8, ExprEvaluator.eval(map.toMap, "one_point_two - two"))
  }

  def testEvalIntMultiply() {
    map.put("one", Int.box(3))
    map.put("two", Int.box(2))
    assertEquals(6L, ExprEvaluator.eval(map.toMap, "one * two"))
  }

  def testEvalDoubleMultiply() {
    map.put("one_point_two", Double.box(1.2))
    map.put("two", Int.box(2))
    assertEquals(2.4, ExprEvaluator.eval(map.toMap, "one_point_two * two"))
  }

  def testEvalIntDivide() {
    map.put("one", Int.box(1))
    map.put("two", Int.box(2))
    assertEquals(0L, ExprEvaluator.eval(map.toMap, "one / two"))
  }

  def testEvalDoubleDivide() {
    map.put("one_point_two", Double.box(1.2))
    map.put("two", Int.box(2))
    assertEquals(0.6, ExprEvaluator.eval(map.toMap, "one_point_two / two"))
  }

  def testEvalIntMod() {
    map.put("one", Int.box(1))
    map.put("two", Int.box(2))
    assertEquals(1L, ExprEvaluator.eval(map.toMap, "one % two"))
  }

  def testEvalDoubleMod() {
    map.put("one_point_two", Double.box(1.2))
    map.put("two", Int.box(2))
    assertEquals(1.2, ExprEvaluator.eval(map.toMap, "one_point_two % two"))
  }

  def testEvalConst() {
    map.put("one", Int.box(1))
    map.put("two", Int.box(2))
    assertEquals(4L, ExprEvaluator.eval(map.toMap, "one + 3"))
  }

  def testEvalConstDouble() {
    map.put("one", Int.box(1))
    map.put("two", Int.box(2))
    assertEquals(4.0, ExprEvaluator.eval(map.toMap, "one + 3.0"))
  }

  def testEvalConstException() {
    map.put("one", Int.box(1))
    map.put("two", Int.box(2))
    try {
      ExprEvaluator.eval(map.toMap, "one + three")
      assertTrue(false)
    } catch {
      case e: Exception => assertTrue(true)
    }
  }

}
