package io.github.uinnn.serializer.serial

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.Bukkit
import org.bukkit.World

object WorldSerializer : KSerializer<World> {
  override val descriptor = PrimitiveSerialDescriptor("World", PrimitiveKind.STRING)
  override fun deserialize(decoder: Decoder): World = runCatching {
    Bukkit.getWorld(decoder.decodeString())
  }.getOrDefault(Bukkit.getWorlds()[0])

  override fun serialize(encoder: Encoder, value: World) = encoder.encodeString(value.name)
}