package io.github.uinnn.serializer.serial

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.Material
import org.bukkit.material.MaterialData

object MaterialDataSerializer : KSerializer<MaterialData> {
  override val descriptor = PrimitiveSerialDescriptor("MaterialData", PrimitiveKind.STRING)

  override fun deserialize(decoder: Decoder): MaterialData = runCatching {
    val split = decoder.decodeString().split(":")
    MaterialData(Material.getMaterial(split[0].toInt()), split[1].toByte())
  }.getOrDefault(MaterialData(Material.AIR))


  override fun serialize(encoder: Encoder, value: MaterialData) {
    encoder.encodeString("${value.itemType.id}:${value.data}")
  }
}