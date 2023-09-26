package org.developerden.codosseum.validation

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlNode
import com.charleskorn.kaml.yamlMap
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import net.pwall.json.schema.JSONSchema
import net.pwall.json.schema.output.BasicOutput
import org.developerden.codosseum.serializers.BasicOutputSerializer
import java.nio.file.Path

fun validate(schema: String, yaml: String): BasicOutput =
	JSONSchema.parse(schema).validateBasic(Json.encodeToString(Yaml.default.parseToYamlNode(yaml).yamlMap.entries))

@Serializable
data class ValidationResult(
	val challengeName: String,
	val output: @Serializable(with = BasicOutputSerializer::class) BasicOutput
)