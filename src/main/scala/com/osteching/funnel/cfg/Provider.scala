/**
 *
 */
package com.osteching.funnel.cfg

import java.lang.Class

/**
 * @author Zhenxing Zhu
 *
 */
class Provider(id: String, clazz: String) {

  override def toString() = new StringBuilder("id:").append(id).append(",provider:").append(clazz).toString

  def getProvider(): Object = Class.forName(clazz)

  def getId(): String = id
}
