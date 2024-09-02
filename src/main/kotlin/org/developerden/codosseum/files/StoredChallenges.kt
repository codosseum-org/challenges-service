package org.developerden.codosseum.files

import org.developerden.codosseum.model.Challenge
import org.developerden.codosseum.validation.ValidationOutput
import org.developerden.codosseum.validation.ValidationResult
import org.developerden.codosseum.validation.validate
import java.nio.file.Path
import kotlin.io.path.readText

interface StoredChallenges {

	val schema: Path
	val challenges: Collection<Challenge>

	fun isSchemaValid(challenge: Challenge): ValidationOutput = validate(schema.readText(Charsets.UTF_8), challenge)

//	fun validateChallenges(): Collection<ValidationResult> =
//		challenges.map { ValidationResult(it.nameWithoutExtension, isSchemaValid(it.readText(Charsets.UTF_8))) }

	fun findChallenge(name: String) = challenges.find { it.name == name }

	fun validateChallenge(challenge: Challenge): ValidationResult =
		ValidationResult(challenge.name, isSchemaValid(challenge))
}