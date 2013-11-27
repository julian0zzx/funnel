/**
 *
 */
package com.osteching.funnel.util

import java.io.BufferedReader
import java.io.File
import java.io.PrintWriter
import java.io.Serializable
import java.io.FileReader

/**
 * @author Zhenxing Zhu
 *
 */
object WaterMarkUtil {

  def writeWm(wmFile: File, watermark: Serializable): Unit = {
    if (wmFile.exists()) {
      wmFile.delete()
    }
    val pw = new PrintWriter(wmFile)
    pw.print(watermark)
    pw.close
  }

  def readWm(wmFile: File): Serializable = {
    if (!wmFile.exists()) {
      return java.lang.Long.valueOf(0)
    }
    val fr = new FileReader(wmFile)
    val br = new BufferedReader(fr)
    val wm: Serializable = br.readLine
    br.close
    fr.close
    wm
    //    java.lang.Long.valueOf(286767228)
  }

}
