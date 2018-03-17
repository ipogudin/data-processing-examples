# Building

Simple building jars for all sub-projects.
> sbt package

Building uber jars for some sub-projects (for instance kafka-client).
> sbt assembly

# Running

Running examples kafka generator and streaming job to save data from kafka to parquet files on hdfs.
> ./execute run-cluster

> ./execute kafka-generator

> ./execute stream-from-kafka82-to-parquet-with-checkpoints

Showing application logs.
> docker exec -it gateway yarn logs -applicationId APP_ID

