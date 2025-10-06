package org.developerden.codosseum.server.routes.challenges

import io.github.tabilzad.ktor.annotations.GenerateOpenApi
import io.github.tabilzad.ktor.annotations.KtorDescription
import io.github.tabilzad.ktor.annotations.KtorResponds
import io.github.tabilzad.ktor.annotations.ResponseEntry
import io.ktor.http.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.developerden.codosseum.challenges.Challenge
import org.developerden.codosseum.challenges.storage.ChallengeStorage
import org.developerden.codosseum.server.Challenges
import org.koin.ktor.ext.inject


@GenerateOpenApi
fun Routing.randomChallenge() {
    @KtorDescription("Get a random challenge")
    @KtorResponds(
        mapping = [
            ResponseEntry("200", Challenge.Info::class),
            ResponseEntry("404", String::class)
        ]
    )
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
    }
}