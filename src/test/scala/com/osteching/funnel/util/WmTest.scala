/**
 *
 */
package com.osteching.funnel.util

import java.io.File

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
 * @author Zhenxing Zhu
 *
 */
class WmTest extends FlatSpec with ShouldMatchers {

  val wmFile = new File("src/test/resources/db_test/eventid")

  it should "return watermark from record file" in {
    WaterMarkUtil.writeWm(wmFile, 10000L)
    WaterMarkUtil.readWm(wmFile).toString.toLong should be(10000)
  }

}
