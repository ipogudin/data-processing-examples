package spark.test

import org.apache.spark._
import org.apache.spark.streaming._

/**
  * Created by ipogudin on 14/03/2017.
  */
object StreamingApp {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Simple Streaming Application")
    val ssc = new StreamingContext(conf, Seconds(1))

    val lines = ssc.socketTextStream("localhost", 9999)
    lines.flatMap(_.split(" ")).map(word => (word, 1)).reduceByKey(_ + _).print()
    ssc.start()
    ssc.awaitTermination()
  }
}
