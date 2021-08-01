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
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.*
import org.bukkit.enchantments.Enchantment

/**
 * A serializer for Enchantment.
 * This will serialize something like this (IN YAML)
 * ```yaml
 * enchantments:
 *   - DIG_SPEED(5)
 *   - LOOT_BONUS_BLOCKS(25)
 * ```
 */
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