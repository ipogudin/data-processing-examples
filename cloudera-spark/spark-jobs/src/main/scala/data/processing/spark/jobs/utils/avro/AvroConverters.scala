package data.processing.spark.jobs.utils.avro

import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.spark.sql.Row

import collection.JavaConverters._

object AvroConverters {
  def genericRecordToRow(avroSchema: Schema, record: GenericRecord): Row = {
    Row.fromSeq(avroSchema.getFields().asScala
      .map(f => record.get(f.pos()) match {
        case u: org.apache.avro.util.Utf8 => u.toString
        case other => other
      }))
  }
}
