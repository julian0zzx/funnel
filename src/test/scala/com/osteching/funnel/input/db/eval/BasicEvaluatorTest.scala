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
class BasicEvaluatorTest extends TestCase {

  val map: Map[String, Object] = Map()

  def testEval() {
    map.put("hello", "^world^")
    assertEquals("^world^", BasicEvaluator.eval(map.toMap, "hello"))
  }

  def testEvalConst() {
    assertEquals(123L, BasicEvaluator.eval(map.toMap, "123"))
  }

  def testEvalConstDouble() {
    assertEquals(12.3, BasicEvaluator.eval(map.toMap, "12.3"))
  }

  def testEvalConstString() {
    assertEquals("hello", BasicEvaluator.eval(map.toMap, "\"hello\""))
  }

  def testEvalConstException() {
    map.put("hello", "^world^")
    try {
      BasicEvaluator.eval(map.toMap, "12d.3")
      assertTrue(false)
    } catch {
      case e: Exception => assertTrue(true)
    }
  }

}
