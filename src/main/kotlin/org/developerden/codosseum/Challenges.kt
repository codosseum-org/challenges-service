package org.developerden.codosseum

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import io.ktor.server.sse.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.developerden.codosseum.config.ChallengesConfiguration
import org.developerden.codosseum.files.StoredChallenges
import org.developerden.codosseum.files.stored.GitStoredChallenges
import org.developerden.codosseum.files.stored.LocalStoredChallenges
import org.developerden.codosseum.files.trigger.GitFileUpdateTrigger
import org.developerden.codosseum.files.trigger.LocalFileUpdateTrigger
import org.developerden.codosseum.ktor_koin.FixedKoin
import org.developerden.codosseum.model.Challenge
import org.developerden.codosseum.sandkasten.api.apis.ProgramsApi
import org.developerden.codosseum.serializers.UUIDSerializer
import org.developerden.codosseum.server.routes.events
import org.developerden.codosseum.server.routes.getRandomChallenge
import org.developerden.codosseum.server.routes.validate
import org.developerden.codosseum.templatespiler.api.apis.DefaultApi
import org.developerden.codosseum.validation.SolutionValidationService
import org.koin.core.module.dsl.singleOf
import java.nio.file.Paths
import kotlin.coroutines.CoroutineContext
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation
import org.koin.dsl.module as koinModule

object ChallengesService : CoroutineScope {

	val logger: KLogger by lazy { KotlinLogging.logger("challenges-service") }

	private val storedChallenges: MutableList<StoredChallenges> by lazy { mutableListOf() }

	val challenges: List<Challenge>
		get() = storedChallenges.flatMap { it.challenges }

	fun findChallenge(name: String): Pair<StoredChallenges, Challenge>? {
		storedChallenges.forEach { stored ->
			val challenge = stored.findChallenge(name)
			if (challenge != null) {
				return Pair(stored, challenge)
			}
		}
		return null
	}

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
			embeddedServer(CIO, applicationEnvironment {
			}, configure = {
				connector {
					port = 8080
				}
			}, ktorModule
			).start(wait = true)
		}
	}

	private val job: Job = Job()

	override val coroutineContext: CoroutineContext
		get() = Dispatchers.Default + job
}


val ktorModule: Application.() -> Unit = {
	val json = Json {
		serializersModule = (SerializersModule {
			contextual(UUIDSerializer)
		})
	}

	install(ContentNegotiation) {
		json(json)
	}
	install(Resources)

//	install(MicrometerMetrics)

	install(SSE)

	routing {
		validate()
		getRandomChallenge()
		events()
	}

	install(FixedKoin) {
		modules(koinModule {
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

suspend fun main(): Unit = ChallengesService.startup()
