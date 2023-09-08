package org.developerden.codosseum

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import kotlinx.coroutines.*
import org.developerden.codosseum.config.ChallengesConfiguration
import org.developerden.codosseum.files.StoredChallenges
import org.developerden.codosseum.files.stored.GitStoredChallenges
import org.developerden.codosseum.files.stored.LocalStoredChallenges
import org.developerden.codosseum.files.trigger.GitFileUpdateTrigger
import org.developerden.codosseum.files.trigger.LocalFileUpdateTrigger
import org.developerden.codosseum.server.routes.validate
import java.nio.file.Paths
import kotlin.coroutines.CoroutineContext

object ChallengesService : CoroutineScope {

	val logger: KLogger by lazy { KotlinLogging.logger("challenges-service") }

	val storedChallenges: MutableList<StoredChallenges> by lazy { mutableListOf() }

	suspend fun startup(): Unit = coroutineScope {
		val config = ChallengesConfiguration.loadConfig()


		logger.info { "Setting up local file updater." }
		config.locals.forEach {
			logger.info { "Setting up $it updater." }
			launch {
				LocalFileUpdateTrigger(Paths.get(it)).setupTrigger()
			}

			storedChallenges += LocalStoredChallenges(it)
		}

		logger.info { "Setting up git file updater. Checking all ${config.remoteUpdatePeriod}ms." }
		config.repositories.forEach {
			logger.info { "Setting up ${it.owner}/${it.name} updater." }
			launch {
				GitFileUpdateTrigger(config.remoteUpdatePeriod, it).setupTrigger()
			}

			storedChallenges += GitStoredChallenges(it)
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
		install(Resources)
		routing {
			validate()
		}
	}

	connector {
		host = "0.0.0.0"
		port = 8080
	}
}

suspend fun main(): Unit = ChallengesService.startup()
