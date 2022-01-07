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

package walkmc.serializer.strategy

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

/**
 * A decoder strategy used to changes how kotlin serialization
 * should decode strings, booleans, numbers and chars.
 * Also can change the [CompositeDecoder.decodeElementIndex] with [decodeIndex]
 */
interface DecoderStrategy {
	fun decodeString(descriptor: SerialDescriptor, index: Int, value: String) = value
	fun decodeBoolean(descriptor: SerialDescriptor, index: Int, value: Boolean) = value
	fun decodeByte(descriptor: SerialDescriptor, index: Int, value: Byte) = value
	fun decodeShort(descriptor: SerialDescriptor, index: Int, value: Short) = value
	fun decodeInt(descriptor: SerialDescriptor, index: Int, value: Int) = value
	fun decodeLong(descriptor: SerialDescriptor, index: Int, value: Long) = value
	fun decodeFloat(descriptor: SerialDescriptor, index: Int, value: Float) = value
	fun decodeDouble(descriptor: SerialDescriptor, index: Int, value: Double) = value
	fun decodeChar(descriptor: SerialDescriptor, index: Int, value: Char) = value
	fun decodeIndex(descriptor: SerialDescriptor, value: Int) = value
}

/**
 * The default [DeserializationStrategy] to work with strategies.
 */
class DefaultSerialDecoder<T>(
	val strategy: DecoderStrategy,
	val model: DeserializationStrategy<T>,
) : DeserializationStrategy<T> by model {
	override fun deserialize(decoder: Decoder): T {
		return model.deserialize(DefaultDecoder(strategy, decoder))
	}
}

/**
 * The default [Decoder] to work with strategies.
 */
class DefaultDecoder(val strategy: DecoderStrategy, val model: Decoder) : Decoder by model {
	override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T {
		return deserializer.deserialize(model)
	}
	
	override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
		return DefaultCompositeDecoder(strategy, model.beginStructure(descriptor))
	}
}

/**
 * The default [CompositeDecoder] to work with strategies.
 */
class DefaultCompositeDecoder(
	val strategy: DecoderStrategy,
	val model: CompositeDecoder,
) : CompositeDecoder by model {
	override fun decodeStringElement(descriptor: SerialDescriptor, index: Int): String {
		return strategy.decodeString(descriptor, index, model.decodeStringElement(descriptor, index))
	}
	
	override fun decodeBooleanElement(descriptor: SerialDescriptor, index: Int): Boolean {
		return strategy.decodeBoolean(descriptor, index, model.decodeBooleanElement(descriptor, index))
	}
	
	override fun decodeByteElement(descriptor: SerialDescriptor, index: Int): Byte {
		return strategy.decodeByte(descriptor, index, model.decodeByteElement(descriptor, index))
	}
	
	override fun decodeShortElement(descriptor: SerialDescriptor, index: Int): Short {
		return strategy.decodeShort(descriptor, index, model.decodeShortElement(descriptor, index))
	}
	
	override fun decodeIntElement(descriptor: SerialDescriptor, index: Int): Int {
		return strategy.decodeInt(descriptor, index, model.decodeIntElement(descriptor, index))
	}
	
	override fun decodeLongElement(descriptor: SerialDescriptor, index: Int): Long {
		return strategy.decodeLong(descriptor, index, model.decodeLongElement(descriptor, index))
	}
	
	override fun decodeFloatElement(descriptor: SerialDescriptor, index: Int): Float {
		return strategy.decodeFloat(descriptor, index, model.decodeFloatElement(descriptor, index))
	}
	
	override fun decodeDoubleElement(descriptor: SerialDescriptor, index: Int): Double {
		return strategy.decodeDouble(descriptor, index, model.decodeDoubleElement(descriptor, index))
	}
	
	override fun decodeCharElement(descriptor: SerialDescriptor, index: Int): Char {
		return strategy.decodeChar(descriptor, index, model.decodeCharElement(descriptor, index))
	}
	
	override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
		return strategy.decodeIndex(descriptor, model.decodeElementIndex(descriptor))
	}
	
	override fun <T> decodeSerializableElement(
		descriptor: SerialDescriptor,
		index: Int,
		deserializer: DeserializationStrategy<T>,
		previousValue: T?,
	) = model.decodeSerializableElement(descriptor, index, DefaultSerialDecoder(strategy, deserializer))
}
