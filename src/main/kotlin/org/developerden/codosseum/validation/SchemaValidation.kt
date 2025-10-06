package org.developerden.codosseum.validation

import io.github.optimumcode.json.schema.JsonSchema
import io.github.optimumcode.json.schema.ValidationError
import io.ktor.util.*
import it.krzeminski.snakeyaml.engine.kmp.api.Load
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import org.developerden.codosseum.challenges.Challenge
import org.developerden.codosseum.serializers.ValidationErrorSerializer
import java.net.URI
import java.nio.file.Files
import kotlin.io.path.inputStream

fun Challenge.validate(): ChallengeSchemaValidationOutput {
    val element = Load().loadOne(challengePath.inputStream()).toJsonElement()

    val schema = fetchSchemaByUriString(info.schema) ?: throw IllegalArgumentException("Schema not found")

    val loader = JsonSchema.fromJsonElement(schema)

    val errors = mutableListOf<ValidationError>()

    val success = loader.validate(element, errors::add)

    return ChallengeSchemaValidationOutput(success, errors.toList())
}

private fun Challenge.fetchSchemaByUriString(uriStr: String): JsonElement? {
    return runCatching {
        val base = challengePath.parent.toUri().toString()
        val resolved = URI(base).resolve(uriStr)
        when (resolved.scheme?.lowercase()) {
            "http", "https" -> {
                // Simple fetch; replace with your HttpClient if you have one in scope
                val bytes = java.net.URL(resolved.toString()).openStream().use { it.readBytes() }
                Json.parseToJsonElement(bytes.decodeToString())
            }

            "file", null -> {
                // Null scheme after resolve implies a file-like path
                val path = java.nio.file.Paths.get(resolved)
                Files.newInputStream(path).use { stream ->
                    Json.parseToJsonElement(stream.readBytes().decodeToString())
                }
            }

            else -> null // Unsupported scheme for MVP
        }
    }.getOrNull()
}

private fun Any?.toJsonElement(): JsonElement {
    return when (this) {
        is Map<*, *> -> JsonObject(entries.associate { (key, value) -> "$key" to value.toJsonElement() })
        is List<*> -> JsonArray(map(Any?::toJsonElement))
        is Set<*> -> JsonArray(map(Any?::toJsonElement))
        is Boolean -> JsonPrimitive(this)
        is Number -> JsonPrimitive(this)
        is String -> JsonPrimitive(this)
        is ByteArray -> JsonPrimitive(encodeBase64())
        null -> JsonNull
        else -> error("Unexpected type: ${this::class.qualifiedName}")
    }
}

@Serializable
data class ChallengeSchemaValidationOutput(
    val success: Boolean,
    val errors: List<@Serializable(with = ValidationErrorSerializer::class) ValidationError>,
)

@Serializable
data class ValidationResult(
    val challengeName: String,
    val schemaValidation: ChallengeSchemaValidationOutput,
    val solutionValidation: SolutionValidationResult,
    val templateValidation: TemplateValidationResult,
)

