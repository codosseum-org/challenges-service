package org.developerden.codosseum.server

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import io.ktor.server.sse.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.developerden.codosseum.SSEEventBus
import org.developerden.codosseum.sandkasten.api.apis.ProgramsApi
import org.developerden.codosseum.serializers.UUIDSerializer
import org.developerden.codosseum.server.koin.FixedKoin
import org.developerden.codosseum.server.routes.events
import org.developerden.codosseum.server.routes.getRandomChallenge
import org.developerden.codosseum.server.routes.validate
import org.developerden.codosseum.templatespiler.api.apis.DefaultApi
import org.developerden.codosseum.validation.SolutionValidationService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ServerContentNegotiation

fun Application.ktor() {
  val json = Json {
    serializersModule = (SerializersModule {
      contextual(UUIDSerializer)
    })
  }

  install(ServerContentNegotiation) {
    json(json)
  }
  install(Resources)

  //install(MicrometerMetrics)

  install(SSE)

  routing {
    validate()
    getRandomChallenge()
    events()
    swaggerUI("swagger", "openapi/openapi.yaml") {
      version = "5.17.14"
    }
  }

  install(FixedKoin) {
    modules(module {
      single {
        ProgramsApi("https://sandkasten.developerden.org", httpClientConfig = {
          it.install(ClientContentNegotiation) {
            json(json)
          }
        })
      }
      single {
        DefaultApi("https://templatespiler-codosseum.developerden.org", httpClientConfig = {
          it.install(ClientContentNegotiation) {
            json(json)
          }
        })
      }
      singleOf(::SolutionValidationService)
      singleOf(::SSEEventBus)
      single { json }
    })
  }
}