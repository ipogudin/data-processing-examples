import Dependencies._
import sbt.Keys.libraryDependencies
import sbtassembly.MergeStrategy


val defaultMergeStrategy: String => MergeStrategy = {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}

lazy val data = project.in(file("data"))
  .settings(Common.settings: _*)
  .settings(
    libraryDependencies ++= Seq(
      avro
    )
  )
  .disablePlugins(AssemblyPlugin)

lazy val sparkJobsDependencies = Seq(
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
  sparkStreamingKafka,
  kafka % Provided,
  sparkStreamingMqttAssembly % Provided,
  sparkStreamingMqtt % Provided,
  sparkStreamingTwitter % Provided,
  sparkStreamingZeromq % Provided,
  sparkStreaming % Provided,
  sparkTestTags % Provided,
  sparkUnsafe % Provided,
  sparkYarn % Provided,
  hadoopClient % Provided excludeAll ExclusionRule(organization = "javax.servlet"),
  scalaConfig)

lazy val sparkJobs = project.in(file("spark-jobs"))
  .settings(Common.settings: _*)
  .settings(
    libraryDependencies ++= sparkJobsDependencies,
    test in assembly := {},
    assemblyJarName in assembly := s"${name.value}-${version.value}-with-dependencies.jar",
    assemblyMergeStrategy in assembly := defaultMergeStrategy
  )
  .dependsOn(data)

lazy val kafkaGenerator = project.in(file("kafka-generator"))
  .settings(Common.settings: _*)
  .settings(
    libraryDependencies ++= Seq(
      sparkStreamingKafka,
      typesafeConfig,
      scalaConfig
    ),
    test in assembly := {},
    assemblyJarName in assembly := s"${name.value}-${version.value}-with-dependencies.jar",
    mainClass in assembly := Some("data.processing.kafkagenerator.Generator"),
    assemblyMergeStrategy in assembly := defaultMergeStrategy
  ).dependsOn(data)

// Section to describe modules with compile time dependencies to provide an ability to run such modules in IntelliJ Idea

//lazy val vvv = libraryDependencies ++= sparkJobsDependencies.map(m => m.organization % m.name % m.revision % Compile)
lazy val sparkJobsRunner = project.in(file("spark-job-runner"))
    .dependsOn(sparkJobs)
  .settings(
    libraryDependencies ++= sparkJobsDependencies.map(m => m.copy(configurations = Some("compile")))
  )
  .disablePlugins(AssemblyPlugin)

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
  .aggregate(sparkJobs, kafkaGenerator, data, sparkJobsRunner)
