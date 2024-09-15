package org.developerden.codosseum.server.routes

import io.github.tabilzad.ktor.annotations.GenerateOpenApi
import io.github.tabilzad.ktor.annotations.KtorDescription
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import io.ktor.server.sse.*
import io.ktor.sse.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.json.Json
import org.developerden.codosseum.SSEEventBus
import org.developerden.codosseum.model.SSE
import org.developerden.codosseum.server.Events
import org.developerden.codosseum.server.koin.inject

@GenerateOpenApi
fun Routing.events() {
	val eventBus by inject<SSEEventBus>()
	val json by inject<Json>()

	@KtorDescription("Subscribe to server sent events")
	get<Events> {
		sse {
			eventBus.events.collectLatest { it: SSE<*> ->
				val eventData = it.toJson(json)
				send(
					ServerSentEvent(event = it.type, data = eventData)
				)
			}
		}
	}
}