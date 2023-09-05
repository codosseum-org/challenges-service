rootProject.name = "challenges-service"

dependencyResolutionManagement {
	versionCatalogs {
		create("libs") {
			version("kotlin", "1.9.10")
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

			bundle("kotlinx", listOf("kotlinx.serialization", "kotlinx.coroutines"))

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

			version("exposed", "0.43.0")

			library("exposed-dao", "org.jetbrains.exposed", "exposed-dao").versionRef("exposed")
			library("exposed-core", "org.jetbrains.exposed", "exposed-core").versionRef("exposed")
			library("exposed-json", "org.jetbrains.exposed", "exposed-json").versionRef("exposed")
			library("exposed-jdbc", "org.jetbrains.exposed", "exposed-jdbc").versionRef("exposed")
			library("exposed-crypt", "org.jetbrains.exposed", "exposed-crypt").versionRef("exposed")
			library("exposed-kotlin-datetime", "org.jetbrains.exposed", "exposed-kotlin-datetime").versionRef("exposed")

			bundle("exposed", listOf(
				"exposed-dao",
				"exposed-core",
				"exposed-json",
				"exposed-jdbc",
				"exposed-crypt",
				"exposed-kotlin-datetime",
			))

			version("logback", "1.4.11")
			version("kotlin-logging", "5.1.0")

			library("logging.logback", "ch.qos.logback", "logback-classic").versionRef("logback")
			library("logging.kotlin", "io.github.oshai", "kotlin-logging-jvm").versionRef("kotlin-logging")

			bundle("logging", listOf("logging.logback", "logging.kotlin"))
		}
	}
}