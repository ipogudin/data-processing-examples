package data.processing.avro

import org.apache.avro.Schema

@SerialVersionUID(10034411L)
class SchemaProcessor(val schemaPath: String) extends Serializable {

  @transient lazy val source = scala.io.Source.fromInputStream(getClass.getResourceAsStream(schemaPath))
  @transient lazy val schemaS: String = try source.mkString finally source.close()
  @transient lazy val schema: Schema = new Schema.Parser().parse(schemaS)


}