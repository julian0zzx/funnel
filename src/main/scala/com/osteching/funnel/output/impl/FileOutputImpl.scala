/**
 *
 */
package com.osteching.funnel.output.impl

import java.io.File
import java.io.PrintWriter
import java.lang.Boolean
import com.osteching.funnel.Brick
import com.osteching.funnel.output.Output

/**
 * @author Zhenxing Zhu
 *
 */
class FileOutputImpl extends Output {

  var writer: PrintWriter = null

  def init(url: String, buffer: Int): Unit = {
    writer = new PrintWriter(new File(url))
  }

  def write(brick: Brick): Boolean = {
    writer.println(brick.raw)
    writer.flush
    Boolean.TRUE
  }

}
