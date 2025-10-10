package org.developerden.codosseum.server.routes.challenges

import io.github.perracodex.kopapi.dsl.operation.api
import io.github.perracodex.kopapi.dsl.parameter.pathParameter
import io.github.perracodex.kopapi.dsl.parameter.queryParameter
import io.ktor.http.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.developerden.codosseum.challenges.Challenge
import org.developerden.codosseum.challenges.storage.ChallengeStorage
import org.developerden.codosseum.server.Challenges
import org.koin.ktor.ext.inject


fun Routing.randomChallenge() {
    get<Challenges.Random> { route ->
        val challengeStorage by inject<ChallengeStorage>()
        var challenges: Set<Challenge> = challengeStorage.getAllChallenges()
        if (route.tagFilters.isNotEmpty()) {
            challenges =
                challenges.filter { it.info.tags.any { tag -> route.tagFilters.contains(tag) } }
                    .toMutableSet() // should it be containsAll or containsAny? any :D
        }
        if (route.difficultyFilters.isNotEmpty()) {
            challenges = challenges.filter { route.difficultyFilters.contains(it.info.difficulty) }.toMutableSet()
        }
        if (challenges.isEmpty()) {
            call.respondText("No challenges found with the given filters", status = HttpStatusCode.NotFound)
        } else {
            call.respond(challenges.random().info)
        }
    } api {
        description = "Get a random challenge"

        queryParameter<List<Challenge.Info.Difficulty>>("difficultyFilters") {
            description =
                "Filter for challenge difficulties. A list of difficulties to _include_ in the search. If empty, all difficulties will be included."
        }

        queryParameter  <List<String>>("tagFilters") {
            description =
                "Filter for challenge tags. A list of tags to _include_ in the search. If empty, all tags will be included."
        }

        response<Challenge.Info>(HttpStatusCode.OK) {
            description = "A random challenge meeting the filter criteria."
            contentType = setOf(ContentType.Application.Json)
        }

        response<String>(HttpStatusCode.NotFound) {
            description = "No challenges found with the given filters."
            contentType = setOf(ContentType.Text.Plain)
        }
    }
}