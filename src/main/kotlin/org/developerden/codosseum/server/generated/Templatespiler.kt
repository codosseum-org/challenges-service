package org.developerden.codosseum.server.generated

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.developerden.codosseum.templatespiler.api.apis.DefaultApi

typealias Templatespiler = DefaultApi

fun templatespiler(json: Json) =
  Templatespiler(System.getenv()["TEMPLATESPILER_URL"] ?: error("TEMPLATESPILER_URL should not be null.")) {
    it.install(ContentNegotiation) {
      json(json)
    }
  }