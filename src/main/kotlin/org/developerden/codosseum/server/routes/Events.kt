package org.developerden.codosseum.server.routes

import io.github.tabilzad.ktor.annotations.GenerateOpenApi
import io.github.tabilzad.ktor.annotations.KtorDescription
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import io.ktor.server.sse.*
import io.ktor.sse.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.developerden.codosseum.SSEEventBus
import org.developerden.codosseum.server.Events
import org.developerden.codosseum.server.koin.inject
import org.developerden.codosseum.validation.FailedTest

interface Event<T: Event<T>> {
	val type: String
}

@Serializable
data class TestCompleteEvent(
	val challengeName: String,
	val testName: String,
	val success: Boolean,
	val failedTest: FailedTest?,
) : Event<TestCompleteEvent> {

	override val type: String
		get() = "test_complete"
}

@GenerateOpenApi
fun Routing.events() {
	val eventBus by inject<SSEEventBus>()
	val json by inject<Json>()

	@KtorDescription("Subscribe to server sent events")
	get<Events> {
		sse {
			eventBus.events.collectLatest { event ->
				send(
					ServerSentEvent(event = event.type, data = json.encodeToString(event))
				)
			}
		}
	}
}