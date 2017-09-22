package data.processing.spark.jobs.utils.kafka

import data.processing.avro.AvroDecoder
import data.processing.spark.jobs.utils.avro.AvroConverters
import kafka.serializer.Decoder
import kafka.utils.VerifiableProperties
import org.apache.spark.sql.Row

class KafkaAvroDecoder(props: VerifiableProperties = null) extends Decoder[Row] {

  val avroDecoder = new AvroDecoder(props.getProperty("avro.schema"))

  def fromBytes(bytes: Array[Byte]): Row = {
    AvroConverters.genericRecordToRow(
      avroDecoder.schema,
      avroDecoder.decode(bytes))
  }

}
