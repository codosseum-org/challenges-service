package org.developerden.codosseum.validation

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.pwall.json.schema.JSONSchema
import net.pwall.json.schema.output.BasicOutput
import java.nio.file.Path

fun validate(schema: String, yaml: String): BasicOutput =
	JSONSchema.parse(schema).validateBasic(Json.encodeToString(Yaml.default.decodeFromString<Map<String, Any>>(yaml)))

data class ValidationResult(
	val path: Path,
	val output: BasicOutput
)