package org.developerden.codosseum.server.routes

import io.github.tabilzad.ktor.annotations.GenerateOpenApi
import io.github.tabilzad.ktor.annotations.KtorDescription
import io.github.tabilzad.ktor.annotations.KtorResponds
import io.github.tabilzad.ktor.annotations.ResponseEntry
import io.ktor.http.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.developerden.codosseum.ChallengesService
import org.developerden.codosseum.challenge.ChallengeInfo
import org.developerden.codosseum.server.Challenges

@GenerateOpenApi
fun Routing.getRandomChallenge() {
  @KtorDescription("Get a random challenge")
  @KtorResponds(
    mapping = [
      ResponseEntry("200", ChallengeInfo::class),
      ResponseEntry("404", String::class)
    ]
  )
  get<Challenges.Random> { route ->
    var challenge = ChallengesService.challenges
    if (route.tagFilters.isNotEmpty()) {
      challenge =
        challenge.filter { it.info.tags.containsAll(route.tagFilters) }
          .toMutableSet() // should it be containsAll or containsAny?
    }
    if (route.difficultyFilters.isNotEmpty()) {
      challenge = challenge.filter { route.difficultyFilters.contains(it.info.difficulty) }.toMutableSet()
    }
    if (challenge.isEmpty()) {
      call.respondText("No challenges found with the given filters", status = HttpStatusCode.NotFound)
    } else {
      call.respond(challenge.random().info)
    }
  }
}