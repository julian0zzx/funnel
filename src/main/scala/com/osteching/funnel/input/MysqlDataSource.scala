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
class MysqlDataSource(url: String, user: String, pwd: String) extends AbstractDataSource(url: String, user: String, pwd: String) {

  val driver = "com.mysql.jdbc.Driver"

  def connect(): Connection = {
    Class.forName(driver)
    DriverManager.getConnection(url, user, pwd)
  }

}
