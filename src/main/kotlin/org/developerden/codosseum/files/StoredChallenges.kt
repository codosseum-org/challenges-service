package org.developerden.codosseum.files

import net.pwall.json.schema.output.BasicOutput
import org.developerden.codosseum.validation.ValidationResult
import org.developerden.codosseum.validation.validate
import java.nio.file.Path
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.readText

interface StoredChallenges {

	val schema: Path
	val challenges: Collection<Path>

	fun isSchemaValid(challenge: String): BasicOutput = validate(schema.readText(Charsets.UTF_8), challenge)

	fun validateChallenges(): Collection<ValidationResult> =
		challenges.map { ValidationResult(it.nameWithoutExtension, isSchemaValid(it.readText(Charsets.UTF_8))) }

	fun findChallenge(name: String) = challenges.find { it.name == name }

	fun validateChallenge(challenge: String): ValidationResult = ValidationResult(challenge, isSchemaValid(challenge))
}