/**
 *
 */
package com.osteching.funnel.pipe;

import org.junit.Assert._
import org.junit.Test
import junit.framework.TestCase
import com.osteching.funnel.input.db.DbBrick
import com.osteching.funnel.pipe.impl.LogPipe

/**
 * @author Zhenxing Zhu
 *
 */
class LogpipeTest extends TestCase {

  /**
   * Test method for {@link com.osteching.funnel.pipe.impl.LogPipe#pipe(com.osteching.funnel.Brick)}.
   */
  def testPipe() {
    val brick = new DbBrick(22, "xx")
    val logPipe = new LogPipe

    logPipe.pipe(brick)

    assertTrue(true)
  }

}
