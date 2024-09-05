package org.developerden.codosseum.validation

import io.github.optimumcode.json.schema.JsonSchema
import io.github.optimumcode.json.schema.ValidationError
import io.ktor.util.*
import it.krzeminski.snakeyaml.engine.kmp.api.Load
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import org.developerden.codosseum.model.Challenge
import org.developerden.codosseum.serializers.ValidationErrorSerializer

fun validate(schema: String, challenge: Challenge): ChallengeSchemaValidationOutput {
	val x = Load().loadOne(challenge.inputStream).toJsonElement()

	val loader = JsonSchema.fromDefinition(schema)

	val errors = mutableListOf<ValidationError>()

	val success = loader.validate(x, errors::add)

	return ChallengeSchemaValidationOutput(success, errors.toList())
}

private fun Any?.toJsonElement(): JsonElement {
	return when (this) {
		is Map<*, *> -> JsonObject(entries.associate { (key, value) -> "$key" to value.toJsonElement() })
		is List<*> -> JsonArray(map { it.toJsonElement() })
		is Set<*> -> JsonArray(map { it.toJsonElement() })
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
	val errors: List<@Serializable(with = ValidationErrorSerializer::class) ValidationError>
)

@Serializable
data class ValidationResult(
	val challengeName: String,
	val schemaValidation: ChallengeSchemaValidationOutput,
	val solutionValidation: SolutionValidationResult
)

