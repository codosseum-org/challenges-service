package org.developerden.codosseum.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.pwall.json.schema.output.BasicErrorEntry

object BasicErrorEntrySerializer : KSerializer<BasicErrorEntry> {
	override val descriptor: SerialDescriptor
		get() = PrimitiveSerialDescriptor("basic-errpr-entry", PrimitiveKind.STRING)

	override fun deserialize(decoder: Decoder): BasicErrorEntry {
		return BasicErrorEntry(
			decoder.decodeString(),
			decoder.decodeString(),
			decoder.decodeString(),
			decoder.decodeString()
		)
	}

	override fun serialize(encoder: Encoder, value: BasicErrorEntry) {
		encoder.encodeString(value.keywordLocation)
		encoder.encodeString(value.absoluteKeywordLocation ?: "")
		encoder.encodeString(value.instanceLocation)
		encoder.encodeString(value.error)
	}

}