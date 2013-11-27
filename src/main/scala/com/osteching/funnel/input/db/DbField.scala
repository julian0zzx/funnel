/**
 *
 */
package com.osteching.funnel.input.db

import com.osteching.funnel.input.db.eval.Evaluator

/**
 * @author Zhenxing Zhu
 *
 */
class DbField(_idx: Int, _expr: String, _eval: Evaluator) {
  def idx = _idx
  def expr = _expr
  def eval = _eval
}
