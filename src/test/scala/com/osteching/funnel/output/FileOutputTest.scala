/**
 *
 */
package com.osteching.funnel.output;

import junit.framework.TestCase
import junit.framework.Assert._
import com.osteching.funnel.input.db.DbBrick
import com.osteching.funnel.cfg.Cfg

/**
 * @author Zhenxing Zhu
 *
 */
class FileOutputTest extends TestCase {

  val cfg = new Cfg("src/test/resources/funnel.xml")

  def testWriter() {
    val outputs = cfg.getOutput(cfg.getChannels.iterator.next)
    assertTrue(outputs.size > 0)

    val fot: Output = outputs(0)
    fot.write(new DbBrick(1, "xx"))
    assertTrue(true)
  }

}
