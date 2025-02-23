package org.developerden.codosseum.server.generated

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.developerden.codosseum.sandkasten.api.apis.ProgramsApi

typealias Sandkasten = ProgramsApi

fun sandkasten(json: Json) =
  Sandkasten(System.getenv()["SANDKASTEN_URL"] ?: error("SANDKASTEN_URL should not be null.")) {
    it.install(ContentNegotiation) {
      json(json)
    }
  }

