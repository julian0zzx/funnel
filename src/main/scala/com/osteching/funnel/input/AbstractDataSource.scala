/**
 *
 */
package com.osteching.funnel.input

import java.sql.Connection
import java.sql.DriverManager

/**
 * @author Zhenxing Zhu
 *
 */
abstract class AbstractDataSource(url: String, user: String, pwd: String) {
  def connect(): Object
}
