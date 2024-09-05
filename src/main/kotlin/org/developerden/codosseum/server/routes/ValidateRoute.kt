package org.developerden.codosseum.server.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.developerden.codosseum.ChallengesService
import org.developerden.codosseum.server.Validate
import org.developerden.codosseum.validation.SolutionValidationService
import org.koin.ktor.ext.inject


fun Routing.validate() {
	val validation by inject<SolutionValidationService>()
	get<Validate.Title> { route ->
		ChallengesService.logger.info { "Validating challenge ${route.title}" }
		val found = ChallengesService.findChallenge(route.title)

		if (found == null) {
			call.respond(HttpStatusCode.NotFound)
			return@get
		}
		val (stored, challenge) = found

		call.respond(validation.validateChallenge(stored, challenge))
	}
}