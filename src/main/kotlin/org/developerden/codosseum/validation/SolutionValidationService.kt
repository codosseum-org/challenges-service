package org.developerden.codosseum.validation

import org.developerden.codosseum.files.StoredChallenges
import org.developerden.codosseum.model.Challenge
import org.developerden.codosseum.sandkasten.api.apis.ProgramsApi
import kotlin.io.path.readText

class SolutionValidationService(private val programsApi: ProgramsApi) {
	suspend fun validateChallenge(storedChallenges: StoredChallenges, challenge: Challenge): ValidationResult =
		ValidationResult(challenge.name, isSchemaValid(storedChallenges, challenge), isSolutionsValid(challenge))

	fun isSchemaValid(storedChallenges: StoredChallenges, challenge: Challenge): ChallengeSchemaValidationOutput =
		validate(storedChallenges.schema.readText(Charsets.UTF_8), challenge)

	suspend fun isSolutionsValid(challenge: Challenge): SolutionValidationResult = validateSolutions(programsApi, challenge)
}