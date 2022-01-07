package walkmc.serializer.serial

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import walkmc.extensions.collections.*

/**
 * A serializer of a int range.
 */
object IntRangeSerializer : KSerializer<IntRange> {
	override val descriptor = PrimitiveSerialDescriptor("SerialRangeIntRange", PrimitiveKind.STRING)
	
	override fun deserialize(decoder: Decoder): IntRange = decoder.decodeStructure(descriptor) {
		val split = decodeStringElement(descriptor, decodeElementIndex(descriptor)).split("..", limit = 2)
		split[0].toInt()..split[1].toInt()
	}
	
	override fun serialize(encoder: Encoder, value: IntRange) = encoder.encodeString(value.toString())
}

/**
 * A serializer of a long range.
 */
object LongRangeSerializer : KSerializer<LongRange> {
	override val descriptor = PrimitiveSerialDescriptor("SerialRangeLongRange", PrimitiveKind.STRING)
	
	override fun deserialize(decoder: Decoder): LongRange = decoder.decodeStructure(descriptor) {
		val split = decodeStringElement(descriptor, decodeElementIndex(descriptor)).split("..", limit = 2)
		return split[0].toLong()..split[1].toLong()
	}
	
	override fun serialize(encoder: Encoder, value: LongRange) = encoder.encodeString(value.toString())
}

/**
 * A serializer of a float range.
 */
object FloatRangeSerializer : KSerializer<FloatRange> {
	override val descriptor = PrimitiveSerialDescriptor("SerialRangeFloatRange", PrimitiveKind.STRING)
	
	override fun deserialize(decoder: Decoder): FloatRange = decoder.decodeStructure(descriptor) {
		val split = decodeStringElement(descriptor, decodeElementIndex(descriptor)).split("..", limit = 2)
		return split[0].toFloat()..split[1].toFloat()
	}
	
	override fun serialize(encoder: Encoder, value: FloatRange) = encoder.encodeString(value.toString())
}

/**
 * A serializer of a float range.
 */
object DoubleRangeSerializer : KSerializer<DoubleRange> {
	override val descriptor = PrimitiveSerialDescriptor("SerialRangeDoubleRange", PrimitiveKind.STRING)
	
	override fun deserialize(decoder: Decoder): DoubleRange = decoder.decodeStructure(descriptor) {
		val split = decodeStringElement(descriptor, decodeElementIndex(descriptor)).split("..", limit = 2)
		return split[0].toDouble()..split[1].toDouble()
	}
	
	override fun serialize(encoder: Encoder, value: DoubleRange) = encoder.encodeString(value.toString())
}
