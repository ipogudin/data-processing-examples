package data.processing.kafkaclient

import org.apache.avro.generic.{GenericData, GenericRecord}
import org.apache.avro.io.{BinaryDecoder, DecoderFactory}
import org.apache.avro.specific.SpecificDatumReader

/**
  * Created by ipogudin on 03/05/2017.
  */
class AvroDecoder(val schemaPath: String) extends SchemaProcessor(schemaPath) {

  val reader = new SpecificDatumReader[GenericRecord](schema)

  def decode(b: Array[Byte]): GenericRecord = {
    val decoder: BinaryDecoder = DecoderFactory.get().binaryDecoder(b, null)
    reader.read(new GenericData.Record(schema), decoder)
  }

}
