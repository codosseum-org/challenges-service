package org.developerden.codosseum

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.developerden.codosseum.model.SSE

class SSEEventBus {
	private val _events = MutableSharedFlow<SSE<*>>()
	val events = _events.asSharedFlow()

	suspend fun publish(event: SSE<*>) {
		_events.emit(event)
	}
}