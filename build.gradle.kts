import io.ktor.plugin.features.*
import org.gradle.internal.os.OperatingSystem
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
  application
  alias(libs.plugins.jvm)
  alias(libs.plugins.ktor)
  alias(libs.plugins.serialization)
  alias(libs.plugins.openapi)
  alias(libs.plugins.ktorOpenapi)
  checkstyle
}

version = "0.1-b"
val javaVersion = JavaVersion.VERSION_21

repositories {
  mavenCentral()
  maven(url = "https://maven.pkg.jetbrains.space/public/p/ktor/eap")
}

dependencies {
  //implementation(libs.jgit)
  implementation(libs.kaml)
  implementation(libs.schema)
  implementation(libs.kfswatch)
  implementation(libs.snakeyaml)
  implementation(libs.bundles.koin)
  implementation(libs.bundles.ktor)
  implementation(libs.kotlinx.hocon)
  implementation(libs.bundles.logging)
  implementation(libs.bundles.kotlinx)
  implementation(libs.bundles.jackson)

  implementation("org.kohsuke:github-api:2.0.0-alpha-1")
  implementation("io.ktor:ktor-client-java:3.0.0-beta-2")
  implementation("io.ktor:ktor-client-cio-jvm:3.0.0-beta-2")

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
  val file = if (OperatingSystem.current().isMacOsX) "./fix-generated-macos.sh" else "./fix-generated-linux.sh"
  commandLine(file)
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

swagger {
  documentation {
    docsTitle = "Codosseum Challenges Service"
    docsVersion = version.toString()
    generateRequestSchemas = true
  }

  pluginOptions {
    format = "yaml"
  }
}