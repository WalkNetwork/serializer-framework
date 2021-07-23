package com.github.uinnn.serializer.serial

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

object PlayerSerializer : KSerializer<OfflinePlayer> {
  override val descriptor = PrimitiveSerialDescriptor("Player", PrimitiveKind.STRING)
  override fun deserialize(decoder: Decoder): OfflinePlayer {
    val name = decoder.decodeString()
    return runCatching {
      Bukkit.getPlayer(name)
    }.getOrDefault(Bukkit.getOfflinePlayer(name))
  }
  
  override fun serialize(encoder: Encoder, value: OfflinePlayer) = encoder.encodeString(value.name)
}