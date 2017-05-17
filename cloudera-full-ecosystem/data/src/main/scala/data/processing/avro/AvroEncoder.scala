package data.processing.avro

import java.io.ByteArrayOutputStream

import org.apache.avro.generic.{GenericData, GenericRecord}
import org.apache.avro.io.{BinaryEncoder, EncoderFactory}
import org.apache.avro.specific.SpecificDatumWriter

/**
  * Created by ipogudin on 03/05/2017.
  */
class AvroEncoder(val schemaPath: String) extends SchemaProcessor(schemaPath) {

  val writer = new SpecificDatumWriter[GenericRecord](schema)

  def encode(t: Product): Array[Byte] = {
    val e: GenericRecord = new GenericData.Record(schema)

    0 until t.productArity foreach(i => {
      e.put(i, t.productElement(i))
    })

    val out = new ByteArrayOutputStream()
    try {
      val encoder: BinaryEncoder = EncoderFactory.get().binaryEncoder(out, null)
      writer.write(e, encoder)
      encoder.flush()
    } finally {
      out.close()
    }

    out.toByteArray()
  }

}
