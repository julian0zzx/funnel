package com.osteching.funnel

import scala.collection.immutable.ListSet
import scala.collection.mutable.Map
import java.util.Collections
import scala.collection.immutable.SortedMap
import com.osteching.funnel.engine.Engine
import com.osteching.funnel.cfg.Cfg

object Main extends App {

  if (0 == args.length) {
    println("Usage: com.osteching.funnel.Main your_funnel_cfg_path")
  } else {
    // load cfg
    val cfg = new Cfg(args(0))

    // init engine
    val engine = new Engine(cfg)

    engine.run
  }

}
