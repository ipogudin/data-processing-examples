package data.processing.avro

import java.util.Properties

import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import com.github.andr83.scalaconfig._

import scala.concurrent.forkjoin.ThreadLocalRandom

/**
  * Created by ipogudin on 22/03/2017.
  */
object Generator {

  val config = ConfigFactory.load()
  val props = config.getConfig("kafka-client").as[Properties]
  val topic = config.getString("kafka-client.topic")
  val numberOfUsers = config.getInt("generator.number.of.users")
  val urls = config.getStringList("generator.urls")
  val eventTypes = config.getStringList("generator.event.types")

  val avroEncoder = new AvroEncoder("/event-record.json")

  def generateEvent() = {
    val id = ThreadLocalRandom.current().nextLong()
    val ts = java.lang.System.currentTimeMillis()
    val userId = ThreadLocalRandom.current().nextInt(numberOfUsers).toHexString
    val url = urls.get(ThreadLocalRandom.current().nextInt(urls.size()))
    val eventType = eventTypes.get(ThreadLocalRandom.current().nextInt(eventTypes.size()))

    (id, avroEncoder.encode((id, ts, userId, url, eventType)))
  }

  def main(args: Array[String]): Unit = {
    val producer = new KafkaProducer[String, Array[Byte]](props)
    while(true) {
      val event = generateEvent()
      producer.send(new ProducerRecord[String, Array[Byte]](topic, event._1.toString, event._2))
    }
    producer.flush()
    producer.close()
  }
}
