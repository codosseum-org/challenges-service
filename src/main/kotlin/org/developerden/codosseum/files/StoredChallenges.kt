package org.developerden.codosseum.files

import net.pwall.json.schema.output.BasicOutput
import org.developerden.codosseum.validation.ValidationResult
import org.developerden.codosseum.validation.validate
import java.nio.file.Path
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.readText

interface StoredChallenges {

	val schema: Path
	val challenges: Collection<Path>

	fun isSchemaValid(challenge: String): BasicOutput = validate(schema.readText(Charsets.UTF_8), challenge)

	fun validateChallenges(): Collection<ValidationResult> = challenges.map { ValidationResult(it, isSchemaValid(it.readText(Charsets.UTF_8))) }
}