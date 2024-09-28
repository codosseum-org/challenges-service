rootProject.name = "challenges-service"

dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      version("kotlin", "2.0.0")
      version("ktor", "3.0.0-beta-2")

      plugin("ktor", "io.ktor.plugin").versionRef("ktor")
      plugin("jvm", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
      plugin("openapi", "org.openapi.generator").version("7.8.0")
      plugin("serialization", "org.jetbrains.kotlin.plugin.serialization").versionRef("kotlin")
      plugin("ktorOpenapi", "io.github.tabilzad.ktor-docs-plugin-gradle").version("0.6.2-alpha")

      version("coroutines", "1.9.0")
      version("serialization", "1.7.2")

      library(
        "kotlinx.serialization",
        "org.jetbrains.kotlinx",
        "kotlinx-serialization-json"
      ).versionRef("serialization")

      library("kotlinx.coroutines", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").versionRef("coroutines")
      library("kotlinx.serialization", "org.jetbrains.kotlinx", "kotlinx-serialization-json").versionRef("serialization")

      bundle(
        "kotlinx",
        listOf(
          "kotlinx.serialization",
          "kotlinx.coroutines"
        )
      )

      library("ktor.sse", "io.ktor", "ktor-server-sse").withoutVersion()
      library("ktor.client", "io.ktor", "ktor-client-core").withoutVersion()
      library("ktor.client.cio", "io.ktor", "ktor-client-cio").withoutVersion()
      library("ktor.server.cio", "io.ktor", "ktor-server-cio").withoutVersion()
      library("ktor.openapi", "io.ktor", "ktor-server-openapi").withoutVersion()
      library("ktor.swagger", "io.ktor", "ktor-server-swagger").withoutVersion()
      library("ktor.server.core", "io.ktor", "ktor-server-core").withoutVersion()
      library("ktor.server.netty", "io.ktor", "ktor-server-netty").withoutVersion()
      library("ktor.resources", "io.ktor", "ktor-server-resources").withoutVersion()
      library("ktor.client.logging", "io.ktor", "ktor-client-logging").withoutVersion()
      library("ktor.server.resources", "io.ktor", "ktor-server-resources").withoutVersion()
      library("ktor.server.logging", "io.ktor", "ktor-server-call-logging").withoutVersion()
      library("ktor.micrometer", "io.ktor", "ktor-server-metrics-micrometer").withoutVersion()
      library("ktor.client.serialization", "io.ktor", "ktor-client-serialization").withoutVersion()
      library("ktor.clientnegotiation", "io.ktor", "ktor-client-content-negotiation").withoutVersion()
      library("ktor.serialization.json", "io.ktor", "ktor-serialization-kotlinx-json").withoutVersion()
      library("ktor.server.negotiation", "io.ktor", "ktor-server-content-negotiation").withoutVersion()

      bundle(
        "ktor",
        listOf(
          "ktor.sse",
          "ktor.client",
          "ktor.openapi",
          "ktor.swagger",
          "ktor.resources",
          "ktor.micrometer",
          "ktor.server.cio",
          "ktor.client.cio",
          "ktor.server.core",
          "ktor.server.netty",
          "ktor.client.logging",
          "ktor.server.logging",
          "ktor.server.resources",
          "ktor.clientnegotiation",
          "ktor.serialization-json",
          "ktor.server.negotiation",
          "ktor.client-serialization",
        )
      )

      version("koin", "4.0.0-RC2")
      library("koin", "io.insert-koin", "koin-core").versionRef("koin")
      library("koin.ktor", "io.insert-koin", "koin-ktor").versionRef("koin")

      bundle("koin", listOf("koin", "koin.ktor"))

      library("kfswatch", "io.github.irgaly.kfswatch", "kfswatch").version("1.3.0")

      library("jgit", "org.eclipse.jgit", "org.eclipse.jgit").version("7.0.0.202409031743-r")

      version("logback", "1.5.8")
      version("kotlin-logging", "7.0.0")

      library("logging.logback", "ch.qos.logback", "logback-classic").versionRef("logback")
      library("logging.kotlin", "io.github.oshai", "kotlin-logging-jvm").versionRef("kotlin-logging")

      bundle("logging", listOf("logging.logback", "logging.kotlin"))

      library("schema", "io.github.optimumcode", "json-schema-validator").version("0.2.3")
      library("snakeyaml", "it.krzeminski", "snakeyaml-engine-kmp").version("3.0.2")
      library("kaml", "com.charleskorn.kaml", "kaml").version("0.61.0")

      version("jackson", "2.17.2")
      library("jackson-core", "com.fasterxml.jackson.core", "jackson-core").versionRef("jackson")
      library("jackson-db", "com.fasterxml.jackson.core", "jackson-databind").versionRef("jackson")
      library("jackson-annotations", "com.fasterxml.jackson.core", "jackson-annotations").versionRef("jackson")
      library("jackson-yaml", "com.fasterxml.jackson.dataformat", "jackson-dataformat-yaml").versionRef("jackson")

      bundle("jackson", listOf("jackson-db", "jackson-core", "jackson-annotations", "jackson-yaml"))

    }
  }
}