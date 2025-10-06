package org.developerden.codosseum.validation

import kotlinx.serialization.Serializable
import org.developerden.codosseum.challenges.Challenge
import org.developerden.codosseum.sandkasten.api.apis.ProgramsApi
import org.developerden.codosseum.server.generated.Templatespiler
import org.developerden.codosseum.server.routes.event.EventBus


class ChallengeValidationService(
    val eventsBus: EventBus,
    val defaultApi: Templatespiler,
    val programsApi: ProgramsApi
) {
    @Serializable
    data class ValidationResult(
        val challengeName: String,
        val schemaValidation: ChallengeSchemaValidationOutput,
        val solutionValidation: SolutionValidationResult,
        val templateValidation: TemplateValidationResult,
    ) {
        val success: Boolean
            get() = schemaValidation.success && solutionValidation.success && templateValidation.success
    }

    suspend fun validate(challenge: Challenge): ValidationResult {
        val schemaValidation = challenge.validateSchema()

        val solutionValidation = challenge.validateSolutions(eventsBus, programsApi)

        val templateValidation = validateTemplate(defaultApi, challenge)

        return ValidationResult(challenge.name, schemaValidation, solutionValidation, templateValidation)
    }

}