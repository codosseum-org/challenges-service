package org.developerden.codosseum.server

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import io.ktor.server.sse.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.developerden.codosseum.ChallengesService
import org.developerden.codosseum.ServiceConfiguration
import org.developerden.codosseum.indexing.indexChallenges
import org.developerden.codosseum.serializers.UUIDSerializer
import org.developerden.codosseum.serializers.ValidationErrorSerializer
import org.developerden.codosseum.server.generated.sandkasten
import org.developerden.codosseum.server.generated.templatespiler
import org.developerden.codosseum.server.koin.FixedKoin
import org.developerden.codosseum.server.routes.challenges.randomChallenge
import org.developerden.codosseum.server.routes.event.EventBus
import org.developerden.codosseum.server.routes.event.events
import org.developerden.codosseum.server.routes.validation.validationSummary
import org.developerden.codosseum.validation.SolutionValidationService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import java.nio.file.Paths
import kotlin.io.path.inputStream
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ServerContentNegotiation

@OptIn(ExperimentalSerializationApi::class)
fun Application.server() {
  val json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    explicitNulls = true
    serializersModule = (SerializersModule {
      contextual(UUIDSerializer)
      contextual(ValidationErrorSerializer)
    })
  }

  install(ServerContentNegotiation) {
    json(json)
  }

  install(Resources)

  /**
   * install(MicrometerMetrics)
   * TODO: Are we using prometheus?
   */

  install(SSE)

  routing {
    validationSummary()
    randomChallenge()
    events()
    swaggerUI("swagger", "openapi/openapi.yaml") {
      version = "5.17.14"
    }
  }

  install(FixedKoin) {
    modules(module {
      single { json }

      single { sandkasten(json) }
      single { templatespiler(json) }

      val configuration: ServiceConfiguration = json.decodeFromStream<ServiceConfiguration>(
        Paths.get(System.getenv()["CONFIGURATION_PATH"] ?: "./challenges-service.json").inputStream()
      )

      runBlocking {
        CoroutineScope(ChallengesService.coroutineContext).async {
          indexChallenges(configuration.indexing)
        }.await()
      }


      single { configuration }

      singleOf(::EventBus)
      singleOf(::SolutionValidationService)

      single {
        HttpClient(CIO) {
          expectSuccess = true

          install(Logging) {
            this.logger = object : Logger {
              override fun log(message: String) = ChallengesService.logger.debug { message }
            }
          }
          install(ClientContentNegotiation) {
            json(json)
          }
        }
      }
    })
  }
}