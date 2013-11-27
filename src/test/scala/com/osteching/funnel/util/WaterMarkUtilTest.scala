/**
 *
 */
package com.osteching.funnel.util;

import java.io.File

import org.junit.Assert.assertEquals

import junit.framework.TestCase

/**
 * @author Zhenxing Zhu
 *
 */
class WaterMarkUtilTest extends TestCase {

  val wmFile = new File("src/test/resources/db_test/eventid")

  def testOne() {
    WaterMarkUtil.writeWm(wmFile, 10000L)
    assertEquals(10000, WaterMarkUtil.readWm(wmFile).toString().toLong)
  }

  def testTwo() {
    WaterMarkUtil.writeWm(wmFile, 10000)
    assertEquals(10000L, WaterMarkUtil.readWm(wmFile).toString().toLong)
    WaterMarkUtil.writeWm(wmFile, 20000L)
    assertEquals(20000L, WaterMarkUtil.readWm(wmFile).toString().toLong)
  }

}
