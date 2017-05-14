import Dependencies._
import sbtassembly.MergeStrategy


lazy val root = (project in file("."))
  .settings(Common.settings: _*)
  .settings(
    inThisBuild(List(
      organization := "data.processing",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "cloudera-full-ecosystem",
    libraryDependencies += scalaTest % Test
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(spark_jobs, kafka_client, data)

lazy val spark_jobs = project.in(file("spark-jobs"))
  .settings(Common.settings: _*)
  .settings(
    libraryDependencies ++= Seq(
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
  .disablePlugins(AssemblyPlugin)

val defaultMergeStrategy: String => MergeStrategy = {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}

lazy val kafka_client = project.in(file("kafka-client"))
  .settings(Common.settings: _*)
  .settings(
    libraryDependencies ++= Seq(
      sparkStreamingKafka,
      typesafeConfig,
      scalaConfig
    ),
    test in assembly := {},
    assemblyJarName in assembly := s"${name.value}-${version.value}-with-dependencies.jar",
    mainClass in assembly := Some("data.processing.kafkaclient.Generator"),
    assemblyMergeStrategy in assembly := defaultMergeStrategy
  ).dependsOn(data)

lazy val data = project.in(file("data"))
  .settings(Common.settings: _*)
  .settings(
    libraryDependencies ++= Seq(
      avro
    )
  )
  .disablePlugins(AssemblyPlugin)
