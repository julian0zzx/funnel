/**
 *
 */
package com.osteching.funnel.input.db.eval

import scala.collection.immutable.Map

/**
 *
 * Ternary expression evaluator, calculate expression according to known values in map.
 * This evaluator only handles &&, ||, ==, != ,>, >=, < and <=
 *
 * @author Zhenxing Zhu
 *
 */
object TernaryEvaluator extends Evaluator {

  /**
   * @param row map holding values
   * @param expr (key_a == key_b) ? 1 : 0
   */
  def eval(row: Map[String, Object], expr: String): Object = {
    val expstr = expr.trim()
    val ev = expstr.substring(0, expstr.indexOf("?"))
    val vv = expstr.substring(expstr.indexOf("?") + 1).split(":")
    val v0 = vv(0).trim()
    val v1 = vv(1).trim()

    if (parseNode(ev).execute(row)) {
      v0
    } else {
      v1
    }
  }

  def parseNode(expr: String): ExprNode = {
    val ex = expr.trim()
    val pNum = ex.count(c => c == '(')
    if (0 == pNum % 2) { // illegal
      throw new IllegalArgumentException("---your expression should be surrounded by parenthesis, " + ex)
    }

    var node = new ExprNode()

    val notation = parseOpr(ex, pNum);
    node.expr = notation

    // charAt(0) == '('
    val leftExpr = ex.substring(1, ex.indexOf(notation))
    if (leftExpr.indexOf('(') > -1) {
      node.left = parseNode(leftExpr)
    } else {
      val l = new ExprNode()
      l.expr = leftExpr
      node.left = l
    }
    // endsWith(')') == true
    val rightExpr = ex.substring(ex.indexOf(notation) + notation.length(), ex.length() - 1)
    if (rightExpr.indexOf('(') > -1) {
      node.right = parseNode(rightExpr)
    } else {
      val r = new ExprNode()
      r.expr = rightExpr
      node.right = r
    }

    node
  }

  def parseOpr(expr: String, pNum: Int): String = {
    val ex = expr.trim()
    if (1 == pNum) {
      var i = 0
      for (c <- ex) {
        if ("!=><".indexOf(c) > -1) {
          return ex.substring(i, i + 2).trim
        }
        i += 1
      }
      return null
    } else {
      var mlp = 0
      var mrp = 0
      var i = 0
      var mlIdx = 0
      var mrIdx = 0
      for (c <- ex) {
        if (')' == c) {
          mrp += 1
          mrIdx = i
        }
        if ('(' == c) {
          mlp += 1
          mlIdx = i
        }
        if ((mrp >= (pNum - 1) / 2) && (mlp == mrp + 2) && (mrIdx < mlIdx)) {
          return ex.substring(mrIdx + 1, mlIdx).trim
        }
        i += 1
      }
      null
    }
  }

  class ExprNode() {
    var left: ExprNode = _
    var right: ExprNode = _
    var expr: String = _

    def hasLeafNode(node: ExprNode): Boolean = null != node && "&&||".indexOf(node.expr) > -1

    def execute(row: Map[String, Object]): Boolean = {
      if (hasLeafNode(left)) {
        return left.execute(row)
      }
      if (hasLeafNode(right)) {
        return right.execute(row)
      }
      expr match {
        case "&&" => {
          return left.execute(row) && right.execute(row)
        }
        case "||" => {
          return left.execute(row) || right.execute(row)
        }
        case "==" => {
          return BasicEvaluator.eval(row, left.expr).equals(BasicEvaluator.eval(row, right.expr))
        }
        case "!=" => {
          return !BasicEvaluator.eval(row, left.expr).equals(BasicEvaluator.eval(row, right.expr))
        }
        case ">=" => {
          evalInt(row, left.expr) >= evalInt(row, right.expr)
        }
        case ">" => {
          evalInt(row, left.expr) > evalInt(row, right.expr)
        }
        case "<=" => {
          evalInt(row, left.expr) <= evalInt(row, right.expr)
        }
        case "<" => {
          evalInt(row, left.expr) <= evalInt(row, right.expr)
        }
        case _ => throw new UnsupportedOperationException("---only support &&, ||, ==, != ,>, >=, < and <= now, " + left + "-" + expr + "-" + right)
      }
    }

    private def evalInt(row: Map[String, Object], key: String): Int = {
      val v = BasicEvaluator.eval(row, key)
      if (!v.isInstanceOf[Int]) {
        throw new IllegalArgumentException("---only int is allowed in ternary evaluator, " + key)
      } else {
        v.asInstanceOf[Int]
      }
    }

    override def toString(): String = {
      left + "-" + expr + "-" + right
    }
  }

}
