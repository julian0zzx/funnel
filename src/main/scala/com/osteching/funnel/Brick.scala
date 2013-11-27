/**
 *
 */
package com.osteching.funnel

import java.io.Serializable

/**
 * @author Zhenxing Zhu
 *
 */
trait Brick extends Serializable {
  def raw(): Serializable
  def getWatermark(): Serializable
}
