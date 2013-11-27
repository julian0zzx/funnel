/**
 *
 */
package com.osteching.funnel.input.db.eval

/**
 * @author Zhenxing Zhu
 *
 */
trait Evaluator {
  def eval(row: Map[String, Object], expr: String): Object
}
