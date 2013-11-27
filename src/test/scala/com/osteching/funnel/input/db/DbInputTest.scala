/**
 *
 */
package com.osteching.funnel.input.db;

import org.junit.Assert._
import junit.framework.TestCase
import java.util.concurrent.ArrayBlockingQueue
import com.osteching.funnel.cfg.Cfg
import com.osteching.funnel.Brick

/**
 * @author Zhenxing Zhu
 *
 */
class DbInputTest /*extends TestCase*/ {

  val cfg = new Cfg("src/test/resources/funnel.xml")

  val chanCfg = cfg.getChannels.iterator.next

  val input = cfg.getInput(chanCfg)

  def testLoadDataSource() {
    // get input from cfg should trigger load data source
    assertNotNull(input)
  }

  def testLoadCfg() {
    assertNotNull(input)
  }

  def testRead() {
    input.initQueue(new ArrayBlockingQueue[Brick](10000))
    input.read
  }

}
