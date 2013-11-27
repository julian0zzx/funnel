/**
 *
 */
package com.osteching.funnel.output

import java.lang.Boolean
import com.osteching.funnel.Brick

/**
 * @author Zhenxing Zhu
 *
 */
trait Output {
  
  def init(url: String, buffer: Int): Unit
  
  def write(brick: Brick): Boolean
  
}
