package org.developerden.codosseum.server.routes

import io.ktor.server.routing.*
import io.ktor.server.sse.*
import io.ktor.sse.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.json.Json
import org.developerden.codosseum.SSEEventBus
import org.developerden.codosseum.model.SSE
import org.developerden.codosseum.ktor_koin.inject

fun Routing.events() {
	val eventBus by inject<SSEEventBus>()
	val json by inject<Json>()

	sse("/events") {
		eventBus.events.collectLatest { it: SSE<*> ->
			val eventData = it.toJson(json)
			send(
				ServerSentEvent(event = it.type, data = eventData)
			)
		}
	}
}