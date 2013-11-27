/**
 *
 */
package com.osteching.funnel.input.db

import com.osteching.funnel.input.AbstractDataSource

/**
 * @author Zhenxing Zhu
 *
 */
object DataSourceHolder {
  private val _tx = new ThreadLocal[AbstractDataSource]()

  def hold(ds: AbstractDataSource) {
    _tx.set(ds)
  }

  def get = _tx.get()

  def clear {
    _tx.remove()
  }

}
