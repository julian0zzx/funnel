/**
 *
 */
package com.osteching.funnel.pipe

/**
 * @author Zhenxing Zhu
 *
 */
abstract class AbstractPipe extends Pipe {

  var nextPipe: Pipe = null

  def append(pipe: Pipe): Pipe = {
    AbstractPipe.this.nextPipe = pipe
    AbstractPipe.this
  }

  def hasNext(): Boolean = {
    null == next
  }

  def next(): Pipe = {
    nextPipe
  }

}
