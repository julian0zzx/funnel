/**
 *
 */
package com.osteching.funnel.cfg;

import org.junit.Assert._
import junit.framework.TestCase
import com.osteching.funnel.input.Input

/**
 * @author Zhenxing Zhu
 *
 */
class CfgTest extends TestCase {

  val cfg = new Cfg("src/test/resources/funnel.xml")

  val chanCfg = cfg.getChannels.iterator.next

  def testGetChannels() {
    assertTrue(cfg.getChannels.size > 0)
  }

  def testGetInput() {
    val input = cfg.getInput(chanCfg)
    assertNotNull(input)
    assertTrue(input.isInstanceOf[Input])
  }

  def testGetPipe() {
    val pipe = cfg.getPipe(chanCfg)
    assertTrue(true)
  }

  def testGetOutput() {
    val output = cfg.getOutput(chanCfg)
    assertEquals(1, output.size)
  }

}
