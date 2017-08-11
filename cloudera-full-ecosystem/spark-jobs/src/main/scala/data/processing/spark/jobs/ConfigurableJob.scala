package data.processing.spark.jobs

import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config

import org.apache.spark._

import scala.io.Source

/**
  * Created by ipogudin on 14/07/2017.
  */
trait ConfigurableJob {

  val APPLICATION_CONF = "application.conf"

  def run(sc: SparkContext, config: Config): Unit

  def main(args: Array[String]): Unit = {
    val isLocalMode = args.length > 0 && args.contains("local")
    val sparkConf = new SparkConf().setAppName("Simple Streaming Application")
    val sc = new SparkContext(if (isLocalMode) sparkConf.setMaster("local[*]") else sparkConf)
    val configFile = SparkFiles.get("application.conf")
    val f = new java.io.File(configFile)
    val config = if (f.exists()) ConfigFactory.load().withFallback(ConfigFactory.parseFile(f)) else ConfigFactory.load()
    run(sc, config)
  }

}
