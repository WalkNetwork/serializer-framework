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

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.material.MaterialData

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
object ItemStackSerializer : KSerializer<ItemStack> {
  override val descriptor = buildClassSerialDescriptor("ItemStack") {
    element<String>("name")
    element("material", MaterialDataSerializer.descriptor)
    element("lore", StringListSerializer.descriptor)
    element<Int>("amount", isOptional = true)
    element<Boolean>("glow", isOptional = true)
    element("enchantments", EnchantmentSerializer.descriptor, isOptional = true)
  }

  override fun deserialize(decoder: Decoder): ItemStack = decoder.decodeStructure(descriptor) {
    lateinit var name: String
    lateinit var material: MaterialData
    lateinit var lore: List<String>
    var amount = 1
    var glow = false
    var enchantments: Map<Enchantment, Int> = HashMap()
    while (true) {
      when (val index = decodeElementIndex(descriptor)) {
        0 -> name = decodeStringElement(descriptor, index)
        1 -> material = decodeSerializableElement(descriptor, index, MaterialDataSerializer)
        2 -> lore = decodeSerializableElement(descriptor, index, StringListSerializer)
        3 -> amount = decodeIntElement(descriptor, index)
        4 -> glow = decodeBooleanElement(descriptor, index)
        5 -> enchantments = decodeSerializableElement(descriptor, index, EnchantmentSerializer)
        CompositeDecoder.DECODE_DONE -> break
        else -> break
      }
    }
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
    encodeStringElement(descriptor, 0, value.itemMeta.displayName)
    encodeSerializableElement(descriptor, 1, MaterialDataSerializer, value.data)
    encodeIntElement(descriptor, 3, value.amount)
    encodeBooleanElement(descriptor, 4, false)
    encodeSerializableElement(descriptor, 2, StringListSerializer, value.itemMeta.lore)
    if (value.enchantments.isNotEmpty())
      encodeSerializableElement(descriptor, 5, EnchantmentSerializer, value.enchantments)
  }
}