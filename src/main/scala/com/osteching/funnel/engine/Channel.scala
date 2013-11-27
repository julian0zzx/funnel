/**
 *
 */
package com.osteching.funnel.engine

import java.io.File
import java.util.concurrent.BlockingQueue
import com.osteching.funnel.Brick
import com.osteching.funnel.util.WaterMarkUtil
import com.osteching.funnel.output.Output
import com.osteching.funnel.input.Input
import com.osteching.funnel.pipe.Pipe

/**
 * @author Zhenxing Zhu
 *
 */
class Channel(wmFile: File, queue: BlockingQueue[Brick], input: Input, pipe: Pipe, outputs: List[Output]) extends Runnable {

  override def run(): Unit = {
    if (null == outputs || 0 == outputs.size) {
      throw new IllegalArgumentException("---channel can't live without output---")
    }

    // input starts with queue
    input.initQueue(queue)

    //    var idx = input.read

    (new Thread(new Runnable() {
      override def run(): Unit = {
        var idx = input.read
      }
    })).start()

    while (true) {
      val b: Brick = queue.take
      // pipeline
      if (null != pipe) {
        pipe.pipe(b)
        if (null != pipe.next) {
          pipe.next.pipe(b)
        }
      }

      // put into output
      for (o <- outputs) yield { // this is unsufficient, but it's simple
        o.write(b)
        // record watermark
        WaterMarkUtil.writeWm(wmFile, b.getWatermark)
      }
    }
  }

}
