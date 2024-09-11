package org.developerden.codosseum.validation

import org.developerden.codosseum.SSEEventBus
import org.developerden.codosseum.files.StoredChallenges
import org.developerden.codosseum.model.Challenge
import org.developerden.codosseum.sandkasten.api.apis.ProgramsApi
import org.developerden.codosseum.templatespiler.api.apis.DefaultApi
import kotlin.io.path.readText

class SolutionValidationService(
	private val programsApi: ProgramsApi,
	private val defaultApi: DefaultApi,
	private val eventsBus: SSEEventBus
) {
	suspend fun validateChallenge(storedChallenges: StoredChallenges, challenge: Challenge): ValidationResult =
		ValidationResult(
			challenge.name,
			isSchemaValid(storedChallenges, challenge),
			isSolutionsValid(challenge),
			validateTemplate(defaultApi, challenge)
		)

	fun isSchemaValid(storedChallenges: StoredChallenges, challenge: Challenge): ChallengeSchemaValidationOutput =
		validate(storedChallenges.schema.readText(Charsets.UTF_8), challenge)

	suspend fun isSolutionsValid(challenge: Challenge): SolutionValidationResult =
		validateSolutions(eventsBus, programsApi, challenge)
}