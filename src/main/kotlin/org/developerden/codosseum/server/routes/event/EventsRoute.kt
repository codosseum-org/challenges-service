package org.developerden.codosseum.server.routes.event

import io.github.perracodex.kopapi.annotation.Schema
import io.github.perracodex.kopapi.dsl.operation.api
import io.github.perracodex.kopapi.type.Composition
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import io.ktor.server.sse.*
import io.ktor.sse.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.developerden.codosseum.server.Events
import org.developerden.codosseum.validation.FailedTest
import org.koin.ktor.ext.inject

@Serializable
@Schema
sealed interface Event<T : Event<T>> {
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

fun Routing.events() {
    val eventBus by inject<EventBus>()
    val json by inject<Json>()

    get<Events> {
        sse {
            eventBus.events.collectLatest { event ->
                send(
                    ServerSentEvent(event = event.type, data = json.encodeToString(event))
                )
            }
        }
    } api {
        summary = "Subscribe to server-sent events"
        description = "Subscribe to server-sent events."
        response<Event<*>>(status = OK) {
            description = "Events successfully subscribed to server-sent events."
            contentType = setOf(ContentType.Text.EventStream)
            composition = Composition.ONE_OF
            addType<TestCompleteEvent>() // TODO: make sure to add new event types here
        }
    }
}