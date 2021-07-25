package io.github.uinnn.serializer.serial

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.*
import org.bukkit.enchantments.Enchantment

object EnchantmentSerializer : KSerializer<Map<Enchantment, Int>> {
  override val descriptor = listSerialDescriptor<String>()

  override fun deserialize(decoder: Decoder) = decoder.decodeStructure(descriptor) {
    val map = hashMapOf<Enchantment, Int>()
    while (true) {
      when (val index = decodeElementIndex(descriptor)) {
        CompositeDecoder.DECODE_DONE -> break
        else -> {
          val string = decodeStringElement(descriptor, index)
          val type = Enchantment.getByName(string.substringBefore('('))
          val level = string.substringAfter('(').replace(")", "").toInt()
          map.putIfAbsent(type, level)
        }
      }
    }
    map
  }

  override fun serialize(encoder: Encoder, value: Map<Enchantment, Int>) = encoder.encodeStructure(descriptor) {
    value.onEachIndexed { index, entry ->
      encodeStringElement(descriptor, index, "${entry.key.name}(${entry.value})")
    }
  }
}