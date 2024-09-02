rootProject.name = "challenges-service"

dependencyResolutionManagement {
	versionCatalogs {
		create("libs") {
			version("kotlin", "2.0.20")
			version("ktor", "2.3.4")

			plugin("jvm", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
			plugin("serialization", "org.jetbrains.kotlin.plugin.serialization").versionRef("kotlin")
			plugin("ktor", "io.ktor.plugin").versionRef("ktor")

			version("coroutines", "1.7.3")
			version("serialization", "1.6.0")

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

			bundle(
				"ktor",
				listOf(
					"ktor.server.core",
					"ktor.server.cio",
					"ktor.server.resources",
					"ktor.server.negotiation",
					"ktor.server.logging",
				)
			)

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
		}
	}
}