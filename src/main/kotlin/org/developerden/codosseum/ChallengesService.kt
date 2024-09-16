package org.developerden.codosseum

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.application.*
import io.ktor.server.cio.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.developerden.codosseum.challenge.Challenge
import kotlin.coroutines.CoroutineContext

object ChallengesService : CoroutineScope {

  val logger: KLogger by lazy { KotlinLogging.logger("challenges-service") }

  private val job: Job = Job()

  override val coroutineContext: CoroutineContext
    get() = Dispatchers.Default + job

  val challenges: MutableSet<Challenge> = mutableSetOf()
}

fun Application.setup() {

}

fun main(args: Array<String>): Unit = EngineMain.main(args)
