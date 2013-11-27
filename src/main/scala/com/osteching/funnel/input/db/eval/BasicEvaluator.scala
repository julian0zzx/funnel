/**
 *
 */
package com.osteching.funnel.input.db.eval

/**
 * basic evaluator
 *
 * @author Zhenxing Zhu
 *
 */
object BasicEvaluator extends Evaluator {

  /**
   * @param row map holding values
   * @param expr acts as key to get value from database record's row map, or expr acts as constant
   */
  def eval(row: Map[String, Object], expr: String): Object = {
    val ex = expr.trim()
    if (!row.contains(ex)) {
      if (ex.matches("\\d+(\\.\\d+)?")) {
        if (ex.indexOf(".") > 0)
          return Double.box(ex.toDouble)
        else
          return Long.box(ex.toLong)
      } else if (ex.startsWith("\"") && ex.endsWith("\"")) {
        return ex.substring(1, ex.length() - 1)
      } else {
        throw new IllegalArgumentException("---only digital constant is allowed---")
      }
    } else {
      return row.get(ex).get
    }
  }

}
