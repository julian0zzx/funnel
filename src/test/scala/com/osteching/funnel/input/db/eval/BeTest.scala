/**
 *
 */
package com.osteching.funnel.input.db.eval

import scala.collection.mutable.Map

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
 * @author Zhenxing Zhu
 *
 */
class BeTest extends FlatSpec with ShouldMatchers {

  it should "get value by key from context" in {
    val map: Map[String, String] = Map()
    map.put("hello", "^world^")
    BasicEvaluator.eval(map.toMap, "hello") should be("^world^")
  }

  it should "get mixed value by key from context" in {
    val map: Map[String, String] = Map()
    map.put("hello", "^world^")
    BasicEvaluator.eval(map.toMap, "hello") should not be ("world")
  }

  it should "get original number by constent digital number from context" in {
    val map: Map[String, String] = Map()
    map.put("hello", "^world^")
    BasicEvaluator.eval(map.toMap, "123") should equal(123L)
  }

}

