package data.processing.kafkaclient

import org.apache.avro.Schema

class SchemaProcessor(schemaPath: String) {

  val source = scala.io.Source.fromInputStream(getClass.getResourceAsStream(schemaPath))
  val schemaS: String = try source.mkString finally source.close()

  val schema: Schema = new Schema.Parser().parse(schemaS)

}