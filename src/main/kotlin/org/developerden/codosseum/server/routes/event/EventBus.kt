package org.developerden.codosseum.server.routes.event

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class EventBus {
  private val _events = MutableSharedFlow<Event<*>>()
  val events = _events.asSharedFlow()

  suspend fun publish(event: Event<*>) {
    _events.emit(event)
  }
}