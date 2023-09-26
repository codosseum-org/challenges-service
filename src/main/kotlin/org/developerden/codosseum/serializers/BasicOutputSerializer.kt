package org.developerden.codosseum.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import net.pwall.json.schema.output.BasicOutput

object BasicOutputSerializer : KSerializer<BasicOutput> {

	override val descriptor: SerialDescriptor
		get() = PrimitiveSerialDescriptor("basic-output", PrimitiveKind.STRING)

	override fun deserialize(decoder: Decoder): BasicOutput {
		return BasicOutput(
			decoder.decodeBoolean(),
			Json.decodeFromString(decoder.decodeString())
		)
	}

	override fun serialize(encoder: Encoder, value: BasicOutput) {
		encoder.encodeBoolean(value.valid)
		encoder.encodeString(Json.encodeToString(value.errors))
	}
}