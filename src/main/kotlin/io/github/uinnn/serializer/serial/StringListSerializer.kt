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