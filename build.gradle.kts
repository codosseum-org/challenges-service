import io.ktor.plugin.features.*
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

version = "0.0.1"
val javaVersion = JavaVersion.VERSION_21

repositories {
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/ktor/eap")
}

dependencies {
    implementation(libs.jgit)
    implementation(libs.kaml)
    implementation(libs.schema)
    implementation(libs.kfswatch)
    implementation(libs.snakeyaml)
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.ktor)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.logging)
    implementation(libs.bundles.kotlinx)
    implementation(libs.bundles.jackson)

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
                    providers.environmentVariable("CHALLENGES_SERVICE_PORT").getOrElse("6543").toInt(),
                    8080,
                    DockerPortMappingProtocol.TCP,
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

tasks.compileKotlin {
    dependsOn(generateSandkasten, generateTemplatespiler)
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
        generateRequestSchemas = true
        hideTransientFields = true
        hidePrivateAndInternalFields = true
        deriveFieldRequirementFromTypeNullability = true

        info {
            title = "Codosseum Challenges Service"
            description = "Service used to index and validate challenges."
            version = project.version.toString()
        }
    }

    pluginOptions {
        format = "yaml"
    }
}