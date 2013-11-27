/**
 *
 */
package com.osteching.funnel.engine

import java.util.concurrent.ArrayBlockingQueue
import com.osteching.funnel.cfg.Cfg
import com.osteching.funnel.Brick

/**
 * @author Zhenxing Zhu
 *
 */
class Engine(cfg: Cfg) {

  def run(): Unit = {
    // load configuration
    val chans = cfg.getChannels
    for (c <- chans) yield {
      (new Thread(new Channel(cfg.getWatermarkFile(c), new ArrayBlockingQueue[Brick](cfg.getQueueSize(c)), cfg.getInput(c), cfg.getPipe(c), cfg.getOutput(c)))).start
    }
  }

}
