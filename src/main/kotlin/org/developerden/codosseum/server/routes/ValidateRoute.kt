package org.developerden.codosseum.server.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.developerden.codosseum.ChallengesService
import org.developerden.codosseum.server.Validate

fun Routing.validate() {
	get<Validate.Title> { route ->
		ChallengesService.logger.info { "Validating challenge ${route.title}" }
		val stored = ChallengesService.storedChallenges.firstOrNull { it.findChallenge(route.title) != null }

		if (stored == null) {
			call.respond(HttpStatusCode.NotFound)
			return@get
		}

		call.respond(Json.encodeToString(stored.validateChallenge(route.title)))
	}
}