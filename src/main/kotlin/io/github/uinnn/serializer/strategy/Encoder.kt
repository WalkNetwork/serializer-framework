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

package io.github.uinnn.serializer.strategy

import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder

/**
 * A encoder strategy used to changes how kotlin serialization
 * should encode strings, booleans, numbers and chars.
 */
interface EncoderStrategy {
  fun encodeString(descriptor: SerialDescriptor, index: Int, value: String) = value
  fun encodeBoolean(descriptor: SerialDescriptor, index: Int, value: Boolean) = value
  fun encodeByte(descriptor: SerialDescriptor, index: Int, value: Byte) = value
  fun encodeShort(descriptor: SerialDescriptor, index: Int, value: Short) = value
  fun encodeInt(descriptor: SerialDescriptor, index: Int, value: Int) = value
  fun encodeLong(descriptor: SerialDescriptor, index: Int, value: Long) = value
  fun encodeFloat(descriptor: SerialDescriptor, index: Int, value: Float) = value
  fun encodeDouble(descriptor: SerialDescriptor, index: Int, value: Double) = value
  fun encodeChar(descriptor: SerialDescriptor, index: Int, value: Char) = value
}

/**
 * The default [SerializationStrategy] to work with strategies.
 */
class DefaultSerialEncoder<in T>(
  val strategy: EncoderStrategy,
  val model: SerializationStrategy<T>,
) : SerializationStrategy<T> by model {
  override fun serialize(encoder: Encoder, value: T) {
    model.serialize(DefaultEncoder(strategy, encoder), value)
  }
}

/**
 * The default [Encoder] to work with strategies.
 */
class DefaultEncoder(val strategy: EncoderStrategy, val model: Encoder) : Encoder by model {
  override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T) {
    serializer.serialize(model, value)
  }

  override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
    return DefaultCompositeEncoder(strategy, model.beginStructure(descriptor))
  }

  override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int): CompositeEncoder {
    return DefaultCompositeEncoder(strategy, model.beginCollection(descriptor, collectionSize))
  }
}

/**
 * The default [CompositeEncoder] to work with strategies.
 */
class DefaultCompositeEncoder(
  val strategy: EncoderStrategy,
  val model: CompositeEncoder,
) : CompositeEncoder by model {
  override fun encodeStringElement(descriptor: SerialDescriptor, index: Int, value: String) {
    model.encodeStringElement(descriptor, index, strategy.encodeString(descriptor, index, value))
  }

  override fun encodeBooleanElement(descriptor: SerialDescriptor, index: Int, value: Boolean) {
    model.encodeBooleanElement(descriptor, index, strategy.encodeBoolean(descriptor, index, value))
  }

  override fun encodeByteElement(descriptor: SerialDescriptor, index: Int, value: Byte) {
    model.encodeByteElement(descriptor, index, strategy.encodeByte(descriptor, index, value))
  }

  override fun encodeShortElement(descriptor: SerialDescriptor, index: Int, value: Short) {
    model.encodeShortElement(descriptor, index, strategy.encodeShort(descriptor, index, value))
  }

  override fun encodeIntElement(descriptor: SerialDescriptor, index: Int, value: Int) {
    model.encodeIntElement(descriptor, index, strategy.encodeInt(descriptor, index, value))
  }

  override fun encodeLongElement(descriptor: SerialDescriptor, index: Int, value: Long) {
    model.encodeLongElement(descriptor, index, strategy.encodeLong(descriptor, index, value))
  }

  override fun encodeFloatElement(descriptor: SerialDescriptor, index: Int, value: Float) {
    model.encodeFloatElement(descriptor, index, strategy.encodeFloat(descriptor, index, value))
  }

  override fun encodeDoubleElement(descriptor: SerialDescriptor, index: Int, value: Double) {
    model.encodeDoubleElement(descriptor, index, strategy.encodeDouble(descriptor, index, value))
  }

  override fun encodeCharElement(descriptor: SerialDescriptor, index: Int, value: Char) {
    model.encodeCharElement(descriptor, index, strategy.encodeChar(descriptor, index, value))
  }

  override fun <T> encodeSerializableElement(
    descriptor: SerialDescriptor,
    index: Int,
    serializer: SerializationStrategy<T>,
    value: T,
  ) = model.encodeSerializableElement(descriptor, index, DefaultSerialEncoder(strategy, serializer), value)
}