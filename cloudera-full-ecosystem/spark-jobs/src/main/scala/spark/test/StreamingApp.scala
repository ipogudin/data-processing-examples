package spark.test

import data.processing.avro.AvroDecoder
import kafka.serializer.StringDecoder
import kafka.serializer.DefaultDecoder
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka.KafkaUtils

/**
  * Created by ipogudin on 14/03/2017.
  */
object StreamingApp {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Simple Streaming Application")
    val ssc = new StreamingContext(conf, Seconds(1))

    val topicsSet = "test".split(",").toSet
    val kafkaParams = Map[String, String]("metadata.broker.list" -> "localhost:9092")

    val directKafkaStream = KafkaUtils.createDirectStream[String, Array[Byte], StringDecoder, DefaultDecoder](
      ssc, kafkaParams, topicsSet
    )



    directKafkaStream.foreachRDD(rdd =>
      rdd.foreachPartition(partitionOfRecords => {
        val avroDecoder = new AvroDecoder("/event-record.json")
        partitionOfRecords.map(m => (m._1, avroDecoder.decode(m._2))).foreach(m => println(m))
    }))


    ssc.start()
    ssc.awaitTermination()
  }
}
