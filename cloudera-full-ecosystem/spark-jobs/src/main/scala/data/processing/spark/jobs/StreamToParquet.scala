package data.processing.spark.jobs

import java.util.concurrent.TimeUnit

import com.typesafe.config.Config
import data.processing.avro.AvroDecoder
import kafka.serializer._
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka.KafkaUtils
import com.github.andr83.scalaconfig._
import org.apache.avro.generic.GenericRecord
import org.apache.spark.sql.{Row, SQLContext, SaveMode, SparkSession}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{DataType, StructType}

import collection.JavaConverters._
import data.processing.spark.jobs.utils.avro.{AvroConverters, SchemaConverters}
import data.processing.spark.jobs.utils.kafka.KafkaAvroDecoder

/**
  * Created by ipogudin on 14/03/2017.
  */
object StreamToParquet extends ConfigurableJob {

  def run(spark: SparkSession, config: Config): Unit = {
    val ssc = new StreamingContext(
      spark.sparkContext, Milliseconds(config.getDuration("streaming-context.batch-duration", TimeUnit.MILLISECONDS)))

    val topicsSet = config.getStringList("kafka-consumer.topics").asScala.toSet
    val kafkaParams = config.getConfig("kafka-consumer").entrySet().asScala
      .map(e => e.getKey.toString -> e.getValue.unwrapped().toString)
      .toMap
    val avroDecoder = new AvroDecoder("/event-record.json")

    val directKafkaStream = KafkaUtils.createDirectStream[String, GenericRecord, StringDecoder, KafkaAvroDecoder](
      ssc, kafkaParams, topicsSet
    )

    directKafkaStream.foreachRDD(rdd => {
      import spark.implicits._
      val st = SchemaConverters.toSqlType(avroDecoder.schema).dataType.asInstanceOf[StructType]

      val rowRdd = rdd.map(r => AvroConverters.genericRecordToRow(avroDecoder.schema, r._2))
      spark.createDataFrame(rowRdd, st).write.mode(SaveMode.Append).parquet("/tmp/data.parquet")
      })

    ssc.start()
    ssc.awaitTermination()
  }
}