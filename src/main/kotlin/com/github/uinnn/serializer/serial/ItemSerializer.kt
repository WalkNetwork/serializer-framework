package com.github.uinnn.serializer.serial

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.material.MaterialData

object ItemSerializer : KSerializer<ItemStack> {
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