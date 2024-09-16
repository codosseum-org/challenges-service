package org.developerden.codosseum

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.developerden.codosseum.server.routes.Event

class EventBus {
	private val _events = MutableSharedFlow<Event<*>>()
	val events = _events.asSharedFlow()

	suspend fun publish(event: Event<*>) {
		_events.emit(event)
	}
}