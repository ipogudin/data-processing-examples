# Building

Simple building jars for all sub-projects.
> sbt package

Building uber jars for some sub-projects (for instance kafka-client).
> sbt assembly

java -jar kafka-generator/target/scala-2.10/kafka-generat-0.1.0-SNAPSHOT-with-dependencies.jar
spark-submit --class data.processing.spark.jobs.StreamToParquet --master local[4] spark-jobs/target/scala-2.10/spark-jobs-0.1.0-SNAPSHOT-with-dependencies.jar