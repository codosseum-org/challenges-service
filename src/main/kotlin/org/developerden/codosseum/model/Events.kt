package org.developerden.codosseum.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import org.developerden.codosseum.validation.FailedTest



interface SSE<T : SSE<T>> {
	val type: String
	val serializationStrategy: SerializationStrategy<T>
	val self: T

	fun toJson(json: Json = Json) = json.encodeToString(serializationStrategy, this.self)
}


@Serializable
data class TestCompleteEvent(
	val challengeName: String,
	val testName: String,
	val success: Boolean,
	val failedTest: FailedTest?
) : SSE<TestCompleteEvent> {
	override val type = "testComplete"
	override val serializationStrategy = serializer()
	override val self = this
}