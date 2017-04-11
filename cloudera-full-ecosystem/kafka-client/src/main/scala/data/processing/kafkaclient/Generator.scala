package data.processing.kafkaclient

import java.io.ByteArrayOutputStream
import java.util.Properties

import org.apache.avro.Schema
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.apache.avro.io.{BinaryEncoder, EncoderFactory}
import org.apache.avro.specific.SpecificDatumWriter
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

/**
  * Created by ipogudin on 22/03/2017.
  */
object Generator {

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("client.id", "ScalaProducerExample")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer")

  def generateEvent(): Unit = {


    //val producer = new KafkaProducer[String, String](props)
    //producer.send(new ProducerRecord[String, String]("", "", ""))
  }

  def main(args: Array[String]): Unit = {
    val source = scala.io.Source.fromInputStream(getClass.getResourceAsStream("/event-record.json"))
    val schemaS: String = try source.mkString finally source.close()

    val schema: Schema = new Schema.Parser().parse(schemaS)

    val e: GenericRecord = new GenericData.Record(schema)

    e.put("id", 1)
    e.put("name", "singh")
    e.put("ts", 213214234l)
    e.put("value", null)

    val writer = new SpecificDatumWriter[GenericRecord](schema)
    val out = new ByteArrayOutputStream()
    val encoder: BinaryEncoder = EncoderFactory.get().binaryEncoder(out, null)
    writer.write(e, encoder)
    encoder.flush()
    out.close()

    val serializedBytes: Array[Byte] = out.toByteArray()
    println(serializedBytes.mkString)

    val producer = new KafkaProducer[String, Array[Byte]](props)
    producer.send(new ProducerRecord[String, Array[Byte]]("test", "1", serializedBytes))
    producer.flush()
    producer.close()
  }
}
