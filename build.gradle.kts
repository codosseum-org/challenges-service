import io.ktor.plugin.features.*
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

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
	maven(url = "https://maven.pkg.jetbrains.space/public/p/ktor/eap")
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


kotlin {
	jvmToolchain(javaVersion.majorVersion.toInt())
}

fun GenerateTask.configureBase() {
	generatorName.set("kotlin")
	library.set("jvm-ktor")
	outputDir.set(project.layout.buildDirectory.dir("generated/$name").get().asFile.absolutePath)
	additionalProperties.apply {
		put("omitGradlePluginVersions", true)
		put("omitGradleWrapper", true)
		put("serializationLibrary", "kotlinx_serialization")
	}
	outputs.upToDateWhen { false }
	outputs.cacheIf { false }
}

val generateSandkasten by tasks.registering(GenerateTask::class) {
	configureBase()
	inputSpec.set("sandkasten-spec.json")
	packageName.set("org.developerden.codosseum.sandkasten.api")
}

val generateTemplatespiler by tasks.registering(GenerateTask::class) {
	configureBase()
	inputSpec.set("templatespiler-spec.json")
	packageName.set("org.developerden.codosseum.templatespiler.api")
}


task<Exec>("generateAndFix") {
	commandLine("./fix-generated.sh")
		.workingDir(project.projectDir)

	dependsOn(generateSandkasten, generateTemplatespiler)
}

tasks.compileKotlin {
	dependsOn("generateAndFix")
}

sourceSets {
	main {
		kotlin {
			srcDirs("${generateSandkasten.get().outputDir.get()}/src/main/kotlin")
			srcDirs("${generateTemplatespiler.get().outputDir.get()}/src/main/kotlin")
		}
	}
}

