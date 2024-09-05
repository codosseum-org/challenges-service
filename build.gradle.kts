import io.ktor.plugin.features.*

plugins {
	application
	alias(libs.plugins.jvm)
	alias(libs.plugins.ktor)
	alias(libs.plugins.serialization)
	alias(libs.plugins.openapi)
	checkstyle
}

version = "0.1-b"
val javaVersion = JavaVersion.VERSION_21

repositories {
	mavenCentral()
}

dependencies {
	implementation(libs.jgit)
	implementation(libs.kaml)
	implementation(libs.snakeyaml)
	implementation(libs.schema)
	implementation(libs.kfswatch)
	implementation(libs.bundles.ktor)
	implementation(libs.bundles.logging)
	implementation(libs.bundles.kotlinx)
	implementation(libs.bundles.jackson)
	implementation(libs.bundles.koin)

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

tasks.compileKotlin {
	dependsOn("openApiGenerate")
}

kotlin {
	jvmToolchain(javaVersion.majorVersion.toInt())
}

openApiGenerate {
	generatorName.set("kotlin")
	inputSpec.set("sandkasten-spec.json")
	packageName.set("org.developerden.codosseum.sandkasten.api")
	logToStderr = true
	cleanupOutput = true
	library.set("jvm-ktor")
	additionalProperties.apply {
		put("omitGradlePluginVersions", true)
		put("omitGradleWrapper", true)
		put("serializationLibrary", "kotlinx_serialization")
	}
}

sourceSets {
	main {
		kotlin {
			srcDirs("${openApiGenerate.outputDir.get()}/src/main/kotlin")
		}
	}
}

