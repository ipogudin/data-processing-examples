import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "data.processing",
      scalaVersion := "2.10.6",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Hello",
    libraryDependencies += scalaTest % Test
  ).aggregate(spark_jobs, kafka_client, data)

lazy val spark_jobs = project.in(file("spark-jobs"))
  .settings(Common.settings: _*)
  .settings(
    libraryDependencies := Seq(
      sparkAssembly % Provided,
      sparkBagel % Provided,
      sparkCatalyst % Provided,
      sparkCore % Provided,
      sparkDockerIntegrationTests % Provided,
      sparkGraphx % Provided,
      sparkHive % Provided,
      sparkLauncher % Provided,
      sparkMllib % Provided,
      sparkNetworkCommon % Provided,
      sparkNetworkShuffle % Provided,
      sparkNetworkYarn % Provided,
      sparkRepl % Provided,
      sparkSql % Provided,
      sparkStreamingFlumeSink % Provided,
      sparkStreamingFlume % Provided,
      sparkStreamingKafka % Provided,
      sparkStreamingMqttAssembly % Provided,
      sparkStreamingMqtt % Provided,
      sparkStreamingTwitter % Provided,
      sparkStreamingZeromq % Provided,
      sparkStreaming % Provided,
      sparkTestTags % Provided,
      sparkUnsafe % Provided,
      sparkYarn % Provided
    )
  )

lazy val kafka_client = project.in(file("kafka-client"))
  .settings(Common.settings: _*)
  .settings(
    libraryDependencies := Seq(
      sparkStreamingKafka,
      typesafeConfig,
      scalaConfig
    )
  ).dependsOn(data)

lazy val data = project.in(file("data"))
  .settings(Common.settings: _*)
  .settings(
    libraryDependencies := Seq(
      avro
    )
  )
