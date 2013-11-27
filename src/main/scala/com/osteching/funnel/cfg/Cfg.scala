/**
 *
 */
package com.osteching.funnel.cfg

import java.io.File
import scala.collection.mutable.ListBuffer
import scala.xml.NodeSeq
import scala.xml.XML
import com.osteching.funnel.output.Output
import com.osteching.funnel.pipe.Pipe
import com.osteching.funnel.input.Input

/**
 * @author Zhenxing Zhu
 *
 */
class Cfg(cfgFile: String) {

  val cfg = XML.load(cfgFile)

  def getChannels(): NodeSeq = {
    cfg \ "channel"
  }

  def getQueueSize(cfgNode: NodeSeq): Int = {
    (cfgNode \ "queue" \ "@size").text.toInt
  }

  def getWatermarkFile(cfgNode: NodeSeq): File = {
    new File((cfgNode \ "watermark" \ "@file").text)
  }

  def getInput(cfgNode: NodeSeq): Input = {
    val input = cfgNode \ "input"
    val ip = new Provider((input \ "@id").text, (input \ "@provider").text).getProvider
    val di = ip.asInstanceOf[Class[Input]].newInstance()
    val dsNode = input \ "datasource"
    //    val p = new Provider((dsNode \ "@id").text, (dsNode \ "@provider").text).getProvider
    val dsType = (dsNode \ "@type").text
    val url = (dsNode \ "url").text
    val user = (dsNode \ "user").text
    val pwd = (dsNode \ "password").text
    di.loadDataSource(dsType, url, user, pwd)
    di.loadCfg((input \ "cfg" \ "@file").text)
    return di
  }

  def getOutput(cfgNode: NodeSeq): List[Output] = {
    val exports = cfgNode \ "output" \ "export"
    if (exports.isEmpty) return List.empty
    val providers = new ListBuffer[Output]
    for (e <- exports) yield {
      val p = new Provider((e \ "@id").text, (e \ "@provider").text)
      (e \ "@type").text match {
        case "file" => {
          val file = (e \ "url").text
          val bufferStr = (e \ "buffer").text
          var buffer = 0
          if (!bufferStr.isEmpty()) {
            buffer = bufferStr.toInt
          }
          val ot = p.getProvider.asInstanceOf[Class[Output]].newInstance()
          ot.init(file, buffer)
          providers.append(ot)
        }
        case "mq" => {
          throw new UnsupportedOperationException("---not support MQ yet---")
        }
        case _ => {
          val file = (e \ "url").text
          val buffer = (e \ "buffer").text.toInt
          val ot = p.getProvider.asInstanceOf[Class[Output]].newInstance()
          ot.init(file, buffer)
          providers.append(ot)
        }
      }
    }
    providers.toList
  }

  def getPipe(cfgNode: NodeSeq): Pipe = {
    val pipes = cfgNode \ "pipeline" \ "pipe"
    if (pipes.isEmpty) return null
    val providers = new ListBuffer[Pipe]
    for (p <- pipes) {
      val dp = new Provider((p \ "@id").text, (p \ "@provider").text).getProvider
      if (dp.isInstanceOf[Class[Pipe]]) {
        providers.append(dp.asInstanceOf[Class[Pipe]].newInstance())
      } else {
        // 
      }
    }
    val dps = providers.toList
    var idx = 0;
    for (dp <- dps) {
      if (idx != 0) {
        dps(idx - 1).append(dp)
      }
      idx += 1
    }
    dps(0)
  }

}
