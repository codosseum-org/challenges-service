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
import org.developerden.codosseum.server.Validate
import org.developerden.codosseum.server.koin.inject
import org.developerden.codosseum.validation.SolutionValidationService
import org.developerden.codosseum.validation.ValidationResult

@GenerateOpenApi
fun Routing.validate() {
  val validation by inject<SolutionValidationService>()

  @KtorDescription("Validate a challenge")
  @KtorResponds(
    mapping = [
      ResponseEntry("200", ValidationResult::class),
      ResponseEntry("404", String::class)
    ]
  )
  get<Validate.Title> { route ->
    ChallengesService.logger.info { "Validating challenge ${route.title}" }
    val found = ChallengesService.challenges.first()

    if (found == null) {
      call.respond(HttpStatusCode.NotFound, "Challenge not found with title ${route.title}")
      return@get
    }
    val (stored, challenge) = found

  }
}