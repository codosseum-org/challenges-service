package org.developerden.codosseum.server.routes.validation

import io.github.oshai.kotlinlogging.KLogger
import io.github.tabilzad.ktor.annotations.GenerateOpenApi
import io.github.tabilzad.ktor.annotations.KtorDescription
import io.github.tabilzad.ktor.annotations.KtorResponds
import io.github.tabilzad.ktor.annotations.ResponseEntry
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import org.developerden.codosseum.server.Validate
import org.developerden.codosseum.server.koin.inject
import org.developerden.codosseum.validation.SolutionValidationService
import org.developerden.codosseum.validation.ValidationResult

@GenerateOpenApi
fun Routing.validationSummary() {
  val validation by inject<SolutionValidationService>()
  val logger by inject<KLogger>()

  @KtorDescription("Validate a challenge")
  @KtorResponds(
    mapping = [
      ResponseEntry("200", ValidationResult::class),
      ResponseEntry("404", String::class)
    ]
  )
  get<Validate.Title> { route ->

  }
}