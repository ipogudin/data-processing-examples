package data.processing.spark.jobs.utils.kafka

import data.processing.avro.AvroDecoder
import kafka.serializer.Decoder
import kafka.utils.VerifiableProperties
import org.apache.avro.generic.GenericRecord

class KafkaAvroDecoder(props: VerifiableProperties = null) extends Decoder[GenericRecord] {

  val avroDecoder = new AvroDecoder(props.getProperty("avro.schema"))

  def fromBytes(bytes: Array[Byte]): GenericRecord = {
    avroDecoder.decode (bytes)
  }

}
