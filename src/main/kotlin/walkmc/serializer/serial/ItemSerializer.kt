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

package walkmc.serializer.serial

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import org.bukkit.inventory.*
import org.bukkit.material.*
import walkmc.*
import walkmc.extensions.*
import walkmc.extensions.constants.*
import walkmc.extensions.strings.*
import walkmc.serializer.common.*
import walkmc.serializer.tag.*

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
object ItemSerializer : KSerializer<ItemStack> {
	override val descriptor = buildClassSerialDescriptor("ItemStack") {
		element("material", MaterialDataSerializer.descriptor)
		element<String>("name")
		element("lore", StringListSerializer.descriptor)
		element<Int>("amount", isOptional = true)
		element<Boolean>("glow", isOptional = true)
		element("enchantments", EnchantmentSerializer.descriptor, isOptional = true)
	}
	
	override fun deserialize(decoder: Decoder): ItemStack {
		return if (decoder is TagDecoder) {
			decoder.input.readItem()
		} else {
			decoder.decode(descriptor) {
				var item = newItem(Materials.STONE)
				var glow = false
				
				while (true) {
					when (val index = decodeElementIndex(descriptor)) {
						0 -> {
							when (val data = decodeSerializableElement(descriptor, index, MaterialDataSerializer)) {
								is SkullItem -> {
									val default = data.url
									val normalized = data.url.substringAfter('(').substringBefore(')')
									item = if (default.startsWith("head")) newHead(normalized) else newHeadOwner(normalized)
								}
								else -> item.type(data.itemType).durability(data.data.toInt())
							}
						}
						1 -> item.name(decodeStringElement(descriptor, index).reverseColorize())
						2 -> item.lore(decodeSerializableElement(descriptor, index, StringListSerializer))
						3 -> item.amount(decodeIntElement(descriptor, index))
						4 -> glow = decodeBooleanElement(descriptor, index)
						5 -> item.addUnsafeEnchantments(decodeSerializableElement(descriptor, index, EnchantmentSerializer))
						else -> break
					}
				}
				
				item.apply {
					if (glow && enchantments.isEmpty()) {
						flags(ItemFlag.HIDE_ENCHANTS)
						enchantments(LUCK to 0)
					}
				}
			}
		}
	}
	
	override fun serialize(encoder: Encoder, value: ItemStack) {
		if (encoder is TagEncoder) {
			encoder.output.writeItem(value)
		} else {
			
			encoder.encodeStructure(descriptor) {
				
				encodeSerializableElement(descriptor, 0, MaterialDataSerializer, value.data)
				encodeStringElement(descriptor, 1, value.name?.colorize() ?: "")
				encodeSerializableElement(descriptor, 2, StringListSerializer, value.lore ?: listOf())
				
				if (value.amount != 1)
					encodeIntElement(descriptor, 3, value.amount)
				
				if (value.enchantments.isNotEmpty())
					encodeSerializableElement(descriptor, 5, EnchantmentSerializer, value.enchantments)
			}
		}
	}
}
