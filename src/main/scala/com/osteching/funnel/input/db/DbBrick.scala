/**
 *
 */
package com.osteching.funnel.input.db

import java.io.Serializable
import com.osteching.funnel.Brick

/**
 * @author Zhenxing Zhu
 *
 */
class DbBrick(eventId: Long, msg: String) extends Brick {
  def raw(): Serializable = msg
  def getWatermark() = eventId

  override def toString: String = eventId + " - " + msg
}
