package org.developerden.codosseum.server.routes.validation

import io.github.oshai.kotlinlogging.KLogger
import io.github.perracodex.kopapi.dsl.operation.api
import io.github.perracodex.kopapi.dsl.parameter.pathParameter
import io.ktor.http.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import org.developerden.codosseum.server.Validate
import org.developerden.codosseum.validation.ChallengeValidationService
import org.developerden.codosseum.validation.SolutionValidationService
import org.koin.ktor.ext.inject

fun Routing.validationSummary() {
    val validation by inject<SolutionValidationService>()
    val logger by inject<KLogger>()

    get<Validate.Title> { route ->

    } api {
        description = "Validate a challenge by title."
        summary = "Validate a challenge"

        pathParameter<String>("title") {
            description = "The title of the challenge to validate."
        }

        response<ChallengeValidationService.ValidationResult>(HttpStatusCode.OK) {
            description = "Validate successful"
        }

        response<String>(HttpStatusCode.NotFound) {
            description = "Challenge with the given title was not found."
        }
    }
}