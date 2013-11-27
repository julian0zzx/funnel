/**
 *
 */
package com.osteching.funnel.pipe.impl

import java.io.Serializable
import com.osteching.funnel.Brick
import com.osteching.funnel.pipe.AbstractPipe

/**
 * @author Zhenxing Zhu
 *
 */
class LogPipe extends AbstractPipe {

  def pipe(brick: Brick): Brick = {
    println(brick.raw)

    brick
  }

}
