rootProject.name = "challenges-service"

dependencyResolutionManagement {
	versionCatalogs {
		create("libs") {
			version("kotlin", "2.0.20")
			version("ktor", "3.0.0-beta-2")


			plugin("jvm", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
			plugin("serialization", "org.jetbrains.kotlin.plugin.serialization").versionRef("kotlin")
			plugin("ktor", "io.ktor.plugin").versionRef("ktor")
			plugin("openapi", "org.openapi.generator").version("7.8.0")

			version("coroutines", "1.9.0-RC.2")
			version("serialization", "1.7.2")

			library(
				"kotlinx.serialization",
				"org.jetbrains.kotlinx",
				"kotlinx-serialization-json"
			).versionRef("serialization")

			library("kotlinx.coroutines", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").versionRef("coroutines")

			bundle(
				"kotlinx",
				listOf(
					"kotlinx.serialization",
					"kotlinx.coroutines"
				)
			)

			library("ktor.server.cio", "io.ktor", "ktor-server-cio").withoutVersion()
			library("ktor.server.core", "io.ktor", "ktor-server-core").withoutVersion()
			library("ktor.server.resources", "io.ktor", "ktor-server-resources").withoutVersion()
			library("ktor.server.negotiation", "io.ktor", "ktor-server-content-negotiation").withoutVersion()
			library("ktor.server.logging", "io.ktor", "ktor-server-call-logging").withoutVersion()
			library("ktor.serialization-json", "io.ktor", "ktor-serialization-kotlinx-json").withoutVersion()
			library("ktor.client", "io.ktor", "ktor-client-core").withoutVersion()
			library("ktor.client-serialization", "io.ktor", "ktor-client-serialization").withoutVersion()
			library("ktor.clientnegotiation", "io.ktor", "ktor-client-content-negotiation").withoutVersion()
			library("ktor.javahttp", "io.ktor", "ktor-client-java").withoutVersion()
			library("ktor.micrometer", "io.ktor", "ktor-server-metrics-micrometer").withoutVersion()
			library("ktor.sse", "io.ktor", "ktor-server-sse").withoutVersion()

			bundle(
				"ktor",
				listOf(
					"ktor.server.core",
					"ktor.server.cio",
					"ktor.server.resources",
					"ktor.server.negotiation",
					"ktor.server.logging",
					"ktor.serialization-json",
					"ktor.client",
					"ktor.client-serialization",
					"ktor.clientnegotiation",
					"ktor.javahttp",
					"ktor.micrometer",
					"ktor.sse"
				)
			)

			version("koin", "4.0.0-RC2")
			library("koin", "io.insert-koin", "koin-core").versionRef("koin")
			library("koin.ktor", "io.insert-koin", "koin-ktor").versionRef("koin")
			bundle("koin", listOf("koin", "koin.ktor"))

			library("kfswatch", "io.github.irgaly.kfswatch", "kfswatch").version("0.9.0")

			library("jgit", "org.eclipse.jgit", "org.eclipse.jgit").version("6.6.1.202309021850-r")

			version("logback", "1.4.11")
			version("kotlin-logging", "5.1.0")

			library("logging.logback", "ch.qos.logback", "logback-classic").versionRef("logback")
			library("logging.kotlin", "io.github.oshai", "kotlin-logging-jvm").versionRef("kotlin-logging")

			bundle("logging", listOf("logging.logback", "logging.kotlin"))

			library("schema", "io.github.optimumcode", "json-schema-validator").version("0.2.3")
			library("snakeyaml", "it.krzeminski", "snakeyaml-engine-kmp").version("3.0.2")
			library("kaml", "com.charleskorn.kaml", "kaml").version("0.55.0")

			version("jackson", "2.15.1")
			library("jackson-db", "com.fasterxml.jackson.core", "jackson-databind").versionRef("jackson")
			library("jackson-core", "com.fasterxml.jackson.core", "jackson-core").versionRef("jackson")
			library("jackson-annotations", "com.fasterxml.jackson.core", "jackson-annotations").versionRef("jackson")
			library("jackson-yaml", "com.fasterxml.jackson.dataformat", "jackson-dataformat-yaml").versionRef("jackson")

			bundle("jackson", listOf("jackson-db", "jackson-core", "jackson-annotations", "jackson-yaml"))

		}
	}
}