package org.developerden.codosseum.serializers

import io.github.optimumcode.json.pointer.JsonPointer
import io.github.optimumcode.json.schema.AbsoluteLocation
import io.github.optimumcode.json.schema.ValidationError
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*

object ValidationErrorSerializer : KSerializer<ValidationError> {
	override val descriptor: SerialDescriptor
		get() = buildClassSerialDescriptor("io.github.optimumcode.json.schema.ValidationError") {
			element<JsonPointer>("schemaPath")
			element<JsonPointer>("objectPath")
			element<String>("message")
			element<Map<String, String>>("details")
			element<AbsoluteLocation?>("absoluteLocation")
		}

	override fun deserialize(decoder: Decoder): ValidationError {
		return decoder.decodeStructure(descriptor) {
			var schemaPath: JsonPointer? = null
			var objectPath: JsonPointer? = null
			var message: String? = null
			var details: Map<String, String>? = null
			var absoluteLocation: AbsoluteLocation? = null

			loop@ while (true) {
				when (val index = decodeElementIndex(descriptor)) {
					CompositeDecoder.DECODE_DONE -> break@loop
					0 -> schemaPath = decodeSerializableElement(descriptor, index, JsonPointer.serializer())
					1 -> objectPath = decodeSerializableElement(descriptor, index, JsonPointer.serializer())
					2 -> message = decodeStringElement(descriptor, index)
					3 -> details = decodeSerializableElement(
						descriptor,
						index,
						MapSerializer(String.serializer(), String.serializer())
					)

					4 -> absoluteLocation =
						decodeSerializableElement(descriptor, index, AbsoluteLocation.serializer().nullable)

					else -> error("Unexpected index: $index")
				}
			}

			ValidationError(
				schemaPath = schemaPath!!,
				objectPath = objectPath!!,
				message = message!!,
				details = details!!,
				absoluteLocation = absoluteLocation!!
			)
		}
	}

	override fun serialize(encoder: Encoder, value: ValidationError) {
		encoder.encodeStructure(descriptor) {
			encodeSerializableElement(descriptor, 0, JsonPointer.serializer(), value.schemaPath)
			encodeSerializableElement(descriptor, 1, JsonPointer.serializer(), value.objectPath)
			encodeStringElement(descriptor, 2, value.message)
			encodeSerializableElement(
				descriptor,
				3,
				MapSerializer(String.serializer(), String.serializer()),
				value.details
			)
			encodeSerializableElement(descriptor, 4, AbsoluteLocation.serializer().nullable, value.absoluteLocation)
		}
	}


}