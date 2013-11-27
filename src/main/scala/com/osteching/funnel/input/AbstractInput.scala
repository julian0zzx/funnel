/**
 *
 */
package com.osteching.funnel.input

import java.util.concurrent.BlockingQueue

import com.osteching.funnel.Brick

/**
 * @author Zhenxing Zhu
 *
 */
abstract class AbstractInput extends Input {

  var queue: BlockingQueue[Brick] = null

  def initQueue(queue: BlockingQueue[Brick]): Unit = {
    AbstractInput.this.queue = queue
  }

  final def read(): java.io.Serializable = {
    val bricks = internalRead
    for (b <- bricks) {
        queue.put(b)
    }
    if(!bricks.isEmpty) {
      return bricks.last.getWatermark
    } else {
      return null;
    }
  }

  def internalRead(): List[Brick]

}
