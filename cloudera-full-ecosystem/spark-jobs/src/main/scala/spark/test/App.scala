package spark.test

import scala.collection.immutable.HashSet

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

/**
  * Created by ipogudin on 05/03/2017.
  */
object App {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Simple Application")
    val sc = new SparkContext(conf)
    val f = sc.textFile("/Users/ipogudin/Downloads/wikispeedia/paths_unfinished.tsv")
    val r = f
      .filter((s: String) => !s.startsWith("#") && s.nonEmpty)
      .map((s: String) => s.split(Array('\t', ' ', ';')))
      .flatMap((a) => a.drop(3).filter(s => !Filters.isStopWord(s)).map(s => (s, 1)))
      .reduceByKey((a, b) => a + b).take(5)
    r.map(s => s._1 + "=" + s._2).foreach(println)
    sc.stop()
  }
}

object Filters {
  val stopWords = HashSet("timeout", "restart")
  def isStopWord(s: String): Boolean = stopWords.contains(s);
}