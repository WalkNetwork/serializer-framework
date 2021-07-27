package io.github.uinnn.serializer.serial

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

/**
 * A serializer for UUID.
 * This just encode to string the uuid and decode back from string.
 */
object UUIDSerializer : KSerializer<UUID> {
  override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)
  override fun deserialize(decoder: Decoder): UUID = UUID.fromString(decoder.decodeString())
  override fun serialize(encoder: Encoder, value: UUID) = encoder.encodeString(value.toString())
}