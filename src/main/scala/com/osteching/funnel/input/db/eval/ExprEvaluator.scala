/**
 *
 */
package com.osteching.funnel.input.db.eval

import scala.collection.immutable.Map

/**
 * Expression evaluator, calculate expression according to known values in map.
 * This evaluator only handles digital number or string
 *
 * @author Zhenxing Zhu
 *
 */
object ExprEvaluator extends Evaluator {
  /**
   * + : digital or string
   * - : digital
   * * : digital
   * \/: digital
   * % : digital
   *
   * @param row map holding values
   * @param expr (key_a +|-|*|/|% key_b)
   */
  def eval(row: Map[String, Object], expr: String): Object = {
    var ex: String = null
    if ('(' == expr.charAt(0) && ')' == expr.charAt(expr.length() - 1)) {
      ex = expr.substring(1, expr.length() - 1)
    } else if ((expr.indexOf('(') >= 0 && expr.indexOf(')') < 0) || (expr.indexOf('(') < 0 && expr.indexOf(')') >= 0)) {
      throw new IllegalArgumentException("---wrong expression pattern, the expr should follow [" + expr + "]")
    } else {
      ex = expr
    }
    val eee = ex.split(" ")
    val e1 = BasicEvaluator.eval(row, eee(0))
    val e2 = BasicEvaluator.eval(row, eee(2))
    eee(1) match {
      case "+" => {
        if (e1.isInstanceOf[String] || e2.isInstanceOf[String]) {
          return e1.toString + e2.toString
        } else if (e1.toString().indexOf(".") > 0 || e2.toString().indexOf(".") > 0) {
          return Double.box(e1.toString.toDouble + e2.toString.toDouble)
        } else {
          return Long.box(e1.toString.toLong + e2.toString.toLong)
        }
      }
      case "-" => {
        if (e1.isInstanceOf[String] || e2.isInstanceOf[String]) {
          throw new UnsupportedOperationException("---apply - to String is not allowed, in [" + ex + "]")
        } else if (e1.toString().indexOf(".") > 0 || e2.toString().indexOf(".") > 0) {
          return Double.box(e1.toString.toDouble - e2.toString.toDouble)
        } else {
          return Long.box(e1.toString.toLong - e2.toString.toLong)
        }
      }
      case "*" => {
        if (e1.isInstanceOf[String] || e2.isInstanceOf[String]) {
          throw new UnsupportedOperationException("---apply * to String is not allowed, in [" + ex + "]")
        } else if (e1.toString().indexOf(".") > 0 || e2.toString().indexOf(".") > 0) {
          return Double.box(e1.toString.toDouble * e2.toString.toDouble)
        } else {
          return Long.box(e1.toString.toLong * e2.toString.toLong)
        }
      }
      case "/" => {
        if (e1.isInstanceOf[String] || e2.isInstanceOf[String]) {
          throw new UnsupportedOperationException("---apply / to String is not allowed, in [" + ex + "]")
        } else if (e1.toString().indexOf(".") > 0 || e2.toString().indexOf(".") > 0) {
          return Double.box(e1.toString.toDouble / e2.toString.toDouble)
        } else {
          return Long.box(e1.toString.toLong / e2.toString.toLong)
        }
      }
      case "%" => {
        if (e1.isInstanceOf[String] || e2.isInstanceOf[String]) {
          throw new UnsupportedOperationException("---apply % to String is not allowed, in [" + ex + "]")
        } else if (e1.toString().indexOf(".") > 0 || e2.toString().indexOf(".") > 0) {
          return Double.box(e1.toString.toDouble % e2.toString.toDouble)
        } else {
          return Long.box(e1.toString.toLong % e2.toString.toLong)
        }
      }
      case _ => {
        throw new UnsupportedOperationException("---unsupported expression [" + expr + "]")
      }

    }
    null
  }
}
