package data.processing.avro

import org.apache.avro.generic.{GenericData, GenericRecord}
import org.apache.avro.io.{BinaryDecoder, DecoderFactory}
import org.apache.avro.specific.SpecificDatumReader

/**
  * Created by ipogudin on 03/05/2017.
  */
@SerialVersionUID(9902040L)
class AvroDecoder(override val schemaPath: String) extends SchemaProcessor(schemaPath) {

  @transient lazy val reader = new SpecificDatumReader[GenericRecord](schema)

  def decode(b: Array[Byte]): GenericRecord = {
    val decoder: BinaryDecoder = DecoderFactory.get().binaryDecoder(b, null)
    reader.read(new GenericData.Record(schema), decoder)
  }

}
