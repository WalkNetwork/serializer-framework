package io.github.uinnn.serializer.serial

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.*

/**
 * A serializer for String Lists. By default
 * kotlin serialization already adds supports for
 * lists serializers. So this is just a "strategy" to
 * encode and decode color symbols, like '&' to '§' and vice-versa
 */
object StringListSerializer : KSerializer<List<String>> {
  override val descriptor = listSerialDescriptor<String>()

  override fun deserialize(decoder: Decoder) = decoder.decodeStructure(descriptor) {
    val list = arrayListOf<String>()
    while (true) {
      when (val index = decodeElementIndex(descriptor)) {
        CompositeDecoder.DECODE_DONE -> break
        else -> list.add(decodeStringElement(descriptor, index))
      }
    }
    list
  }

  override fun serialize(encoder: Encoder, value: List<String>) = encoder.encodeStructure(descriptor) {
    for ((index, str) in value.withIndex()) encodeStringElement(descriptor, index, str.replace('§', '&'))
  }
}