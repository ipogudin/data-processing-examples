package data.processing.spark.jobs

import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config
import org.apache.spark._
import org.apache.spark.sql.SparkSession


/**
  * Created by ipogudin on 14/07/2017.
  */
trait ConfigurableJob {

  val APPLICATION_CONF = "application.conf"

  def run(spark: SparkSession, config: Config): Unit

  def main(args: Array[String]): Unit = {
    val isLocalMode = args.length > 0 && args.contains("local")
    val sparkBuilder = SparkSession.builder().appName("Simple Streaming Application")
    val spark = (if (isLocalMode) sparkBuilder.master("local[*]") else sparkBuilder).getOrCreate()
    val configFile = SparkFiles.get("application.conf")
    val f = new java.io.File(configFile)
    val config = if (f.exists()) ConfigFactory.load().withFallback(ConfigFactory.parseFile(f)) else ConfigFactory.load()
    run(spark, config)
  }

}
