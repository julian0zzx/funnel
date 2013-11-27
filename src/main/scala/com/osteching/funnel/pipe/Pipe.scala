/**
 *
 */
package com.osteching.funnel.pipe

import java.io.Serializable
import com.osteching.funnel.Brick

/**
 * @author Zhenxing Zhu
 *
 */
trait Pipe {

  def pipe(brick: Brick): Brick

  def append(pipe: Pipe): Pipe

  def hasNext(): Boolean

  def next(): Pipe

}
