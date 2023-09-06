package org.developerden.codosseum

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import kotlinx.coroutines.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.developerden.codosseum.config.ChallengesConfiguration
import org.developerden.codosseum.files.trigger.GitFileUpdateTrigger
import org.developerden.codosseum.files.trigger.LocalFileUpdateTrigger
import org.developerden.codosseum.git.Repository
import java.nio.file.Paths
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

object ChallengesService : CoroutineScope {

	val logger: KLogger by lazy { KotlinLogging.logger("challenges-service") }

	suspend fun startup(): Unit = coroutineScope {
		val config = ChallengesConfiguration.loadConfig()

		logger.info { "Setting up local file updater." }
		launch {
			LocalFileUpdateTrigger(Paths.get(config.local)).setupTrigger()
		}

		logger.info { "Setting up git file updater. Checking all ${config.remoteUpdatePeriod}ms." }
		config.repositories.forEach {
			logger.info { "Setting up ${it.owner}/${it.name} updater." }
			launch {
				GitFileUpdateTrigger(config.remoteUpdatePeriod, it).setupTrigger()
			}
		}

		launch {
			embeddedServer(CIO, applicationEngineEnvironment {
				config()
			}).start(wait = true)
		}
	}

	private val job: Job = Job()

	override val coroutineContext: CoroutineContext
		get() = Dispatchers.Default + job
}

fun ApplicationEngineEnvironmentBuilder.config() {
	module {

	}

	connector {
		host = "0.0.0.0"
		port = 8080
	}
}

suspend fun main(): Unit = ChallengesService.startup()
