package walkmc.serializer.serial

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlin.time.*

/**
 * Represents a serializer for [Duration].
 */
object DurationSerializer : KSerializer<Duration> {
	override val descriptor = PrimitiveSerialDescriptor("Duration", PrimitiveKind.LONG)
	
	override fun deserialize(decoder: Decoder): Duration {
		return Duration.milliseconds(decoder.decodeLong())
	}
	
	override fun serialize(encoder: Encoder, value: Duration) {
		encoder.encodeLong(value.inWholeMilliseconds)
	}
}
