package data.processing.spark.jobs

import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config
import org.apache.spark._
import org.apache.spark.sql.SparkSession
import org.slf4j.LoggerFactory

import scala.io.Source


/**
  * Created by ipogudin on 14/07/2017.
  */
trait ConfigurableJob {

  private val logger = LoggerFactory.getLogger("root")
  val APPLICATION_CONF = "application.conf"

  def run(spark: SparkSession, config: Config): Unit

  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load()
    logger.info("Loaded configuration: {}", config.toString)
    val isLocalMode = args.length > 0 && args.contains("local")
    val sparkBuilder = SparkSession.builder().appName(config.getString("app.name"))
    val spark = (if (isLocalMode) sparkBuilder.master("local[*]") else sparkBuilder).getOrCreate()
    run(spark, config)
  }

}
