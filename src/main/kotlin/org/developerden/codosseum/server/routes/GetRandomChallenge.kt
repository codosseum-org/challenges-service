package org.developerden.codosseum.server.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.developerden.codosseum.ChallengesService
import org.developerden.codosseum.server.RandomChallenge

fun Routing.getRandomChallenge() {
	get<RandomChallenge> { route ->
		var challenge = ChallengesService.challenges
		if (route.tagFilters.isNotEmpty()) {
			challenge =
				challenge.filter { it.info.tags.containsAll(route.tagFilters) } // should it be containsAll or containsAny?
		}
		if (route.difficultyFilters.isNotEmpty()) {
			challenge = challenge.filter { route.difficultyFilters.contains(it.info.difficulty) }
		}
		if (challenge.isEmpty()) {
			call.respondText("No challenges found with the given filters", status = HttpStatusCode.NotFound)
		} else {
			call.respond(challenge.random().info)
		}

	}
}