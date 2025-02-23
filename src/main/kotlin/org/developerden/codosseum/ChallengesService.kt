package org.developerden.codosseum

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.cio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

object ChallengesService {
  val logger: KLogger = KotlinLogging.logger("challenges-service")

  private val job: Job = Job()

  val coroutineContext: CoroutineContext
    get() = Dispatchers.Default + job

}

fun main(args: Array<String>) = EngineMain.main(args)
