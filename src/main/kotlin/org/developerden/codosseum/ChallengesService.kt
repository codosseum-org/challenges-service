package org.developerden.codosseum

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.serialization.json.Json
import org.developerden.codosseum.challenge.Challenge
import kotlin.coroutines.CoroutineContext

object ChallengesService : CoroutineScope {

  val logger: KLogger by lazy { KotlinLogging.logger("challenges-service") }

  private val job: Job = Job()

  override val coroutineContext: CoroutineContext
    get() = Dispatchers.Default + job

  val challenges: MutableSet<Challenge> = mutableSetOf()

  val httpClient: HttpClient = HttpClient(CIO) {
    expectSuccess = true

    install(Logging) {
      this.logger = object : Logger {
        override fun log(message: String) {
          this@ChallengesService.logger.debug { message }
        }
      }

      this.level = LogLevel.INFO
    }

    install(ContentNegotiation) {
      json(Json {
        ignoreUnknownKeys = true
      })
    }
  }
}

fun Application.setup() {

}

fun main(args: Array<String>) = EngineMain.main(args)
