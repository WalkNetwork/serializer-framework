package io.github.uinnn.serializer.serial

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.Bukkit
import org.bukkit.Location

object LocationSerializer : KSerializer<Location> {
  override val descriptor = PrimitiveSerialDescriptor("Location", PrimitiveKind.STRING)
  override fun deserialize(decoder: Decoder): Location {
    val split = decoder.decodeString().split('|')
    return Location(
      Bukkit.getWorld(split[0]),
      split[1].toDouble(),
      split[2].toDouble(),
      split[3].toDouble()
    )
  }

  override fun serialize(encoder: Encoder, value: Location) {
    encoder.encodeString("${value.world.name}|${value.blockX}|${value.blockY}|${value.blockZ}")
  }
}