/**
 *
 */
package com.osteching.funnel.input

import java.util.concurrent.BlockingQueue
import scala.xml.NodeSeq
import com.osteching.funnel.Brick

/**
 * @author Zhenxing Zhu
 *
 */
trait Input {

  def initQueue(queue: BlockingQueue[Brick]): Unit

  //  def loadDataSource(cfgNode: NodeSeq): Unit
  def loadDataSource(dsType: String, url: String, user: String, pwd: String): Unit

  //  def loadCfg(cfgNode: NodeSeq): Unit
  def loadCfg(cfgPath: String): Unit

  /**
   * @return original records water mark, a start point for next loop
   */
  def read(): java.io.Serializable

}
