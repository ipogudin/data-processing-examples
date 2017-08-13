package data.processing.spark.jobs

import com.typesafe.config.Config
import data.processing.avro.AvroDecoder
import kafka.serializer._
import kafka.utils.VerifiableProperties
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka.KafkaUtils
import com.github.andr83.scalaconfig._
import org.apache.avro.generic.GenericRecord
import org.apache.spark.sql.{Row, SQLContext, SaveMode}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{DataType, StructType}

import collection.JavaConverters._
import org.apache.avro.Schema
import data.processing.spark.jobs

/**
  * Created by ipogudin on 14/03/2017.
  */
object StreamToParquet extends ConfigurableJob {

  def run(sc: SparkContext, config: Config): Unit = {
    val ssc = new StreamingContext(sc, Milliseconds(config.getMilliseconds("streaming-context.batch-duration")))

    val topicsSet = config.getStringList("kafka-consumer.topics").asScala.toSet
    val kafkaParams = config.getConfig("kafka-consumer").entrySet().asScala
      .map(e => e.getKey.toString -> e.getValue.unwrapped().toString)
      .toMap

    val directKafkaStream = KafkaUtils.createDirectStream[String, GenericRecord, StringDecoder, KafkaAvroDecoder](
      ssc, kafkaParams, topicsSet
    )

    directKafkaStream.foreachRDD(rdd => {
        val sqlContext = SQLContext.getOrCreate(rdd.sparkContext)
        import sqlContext.implicits._
      val st = CSchemaConverters.toSqlType((new AvroDecoder("/event-record.json")).schema).dataType.asInstanceOf[StructType]

      val rowRdd = rdd.map(r => Converters.genericRecordToRow((new AvroDecoder("/event-record.json")).schema, r._2))
      sqlContext.createDataFrame(rowRdd, st).write.mode(SaveMode.Append)parquet("/tmp/data.parquet")
      })

    ssc.start()
    ssc.awaitTermination()
  }
}

object Converters {

  def genericRecordToRow(avroSchema: Schema, record: GenericRecord): Row = {
    Row.fromSeq(avroSchema.getFields().asScala
      .map(f => record.get(f.pos()) match {
        case u: org.apache.avro.util.Utf8 => u.toString
        case other => other
      }))
  }

}

class KafkaAvroDecoder(props: VerifiableProperties = null) extends Decoder[GenericRecord] {

  val avroDecoder = new AvroDecoder("/event-record.json")

  def fromBytes(bytes: Array[Byte]): GenericRecord = {
    avroDecoder.decode (bytes)
  }

}