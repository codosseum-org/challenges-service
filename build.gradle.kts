import io.ktor.plugin.features.*

plugins {
	application
	alias(libs.plugins.jvm)
	alias(libs.plugins.ktor)
	alias(libs.plugins.serialization)
}

version = "0.1-b"
val javaVersion = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation(libs.jgit)
	implementation(libs.kaml)
	implementation(libs.schema)
	implementation(libs.kfswatch)
	implementation(libs.bundles.ktor)
	implementation(libs.bundles.logging)
	implementation(libs.bundles.kotlinx)

	testImplementation(kotlin("test"))
}

application {
	mainClass.set("org.developerden.codosseum.ChallengesService")
}

ktor {
	docker {
		jreVersion.set(javaVersion)

		localImageName.set("challenges-service")
		imageTag.set(version.toString())

		portMappings.set(
			listOf(
				DockerPortMapping(
					80,
					8080,
					DockerPortMappingProtocol.TCP
				)
			)
		)
	}
}

tasks.test {
	useJUnitPlatform()
}

kotlin {
	jvmToolchain(javaVersion.majorVersion.toInt())
}