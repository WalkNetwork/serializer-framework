/*
                             MIT License

                        Copyright (c) 2021 uin

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package io.github.uinnn.serializer.serial

import io.github.uinnn.serializer.common.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*

/**
 * A serializer for ItemStack.
 * This will serialize something like this (IN YAML)
 * ```yaml
 * item:
 *   name: "&eItem"
 *   material: "2:0"
 *   amount: 1
 *   glow: false
 *   lore:
 *     - "&7Hi! I'm a lore!"
 *   enchantments:
 *     - "DURABILITY(1)"
 * ```
 */
object ItemSerializer : Serializer<ItemStack> {
  override val descriptor = buildClassSerialDescriptor("ItemStack") {
    element<String>("name")
    element("material", MaterialDataSerializer.descriptor)
    element("lore", StringListSerializer.descriptor)
    element<Int>("amount", isOptional = true)
    element<Boolean>("glow", isOptional = true)
    element("enchantments", EnchantmentSerializer.descriptor, isOptional = true)
  }

  override fun deserialize(decoder: Decoder): ItemStack = decoder.decodeStructure(descriptor) {
    val name = decodeStringElement(descriptor, decodeElementIndex(descriptor))
    val material = decodeSerializableElement(descriptor, decodeElementIndex(descriptor), MaterialDataSerializer)
    val lore = decodeSerializableElement(descriptor, decodeElementIndex(descriptor), StringListSerializer)
    val amount = decodeIntElement(descriptor, decodeElementIndex(descriptor))
    val glow = decodeBooleanElement(descriptor, decodeElementIndex(descriptor))
    val enchantments = decodeSerializableElement(descriptor, decodeElementIndex(descriptor), EnchantmentSerializer)
    material.toItemStack(amount).apply {
      val meta = itemMeta
      meta.displayName = name
      meta.lore = lore
      enchantments.forEach { (type, level) -> addUnsafeEnchantment(type, level) }
      if (glow && enchantments.isEmpty()) {
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        addUnsafeEnchantment(Enchantment.LUCK, 0)
      }
      itemMeta = meta
    }
  }

  override fun serialize(encoder: Encoder, value: ItemStack) = encoder.encodeStructure(descriptor) {
    val meta = value.itemMeta
    encodeStringElement(descriptor, 0, value.itemMeta.displayName ?: "")
    encodeSerializableElement(descriptor, 1, MaterialDataSerializer, value.data)
    encodeIntElement(descriptor, 3, value.amount)
    encodeBooleanElement(descriptor, 4, false)
    encodeSerializableElement(descriptor, 2, StringListSerializer, value.itemMeta.lore ?: listOf())
    if (value.enchantments.isNotEmpty())
      encodeSerializableElement(descriptor, 5, EnchantmentSerializer, value.enchantments)
  }
}

/**
 * A item stack binary serializer. This is for use in databases.
 */
object BinaryItemSerializer : Serializer<ItemStack> {
  override val descriptor = PrimitiveSerialDescriptor("BinaryItemStack", PrimitiveKind.STRING)

  override fun deserialize(decoder: Decoder): ItemStack {
    val value = decoder.decodeString()
    return ByteArrayInputStream(Base64.getDecoder().decode(value)).use {
      BukkitObjectInputStream(it).use { data ->
        data.readObject() as ItemStack
      }
    }
  }

  override fun serialize(encoder: Encoder, value: ItemStack) {
    ByteArrayOutputStream().use {
      BukkitObjectOutputStream(it).use { data ->
        data.writeObject(value)
        encoder.encodeString(Base64.getEncoder().encodeToString(it.toByteArray()))
      }
    }
  }
}