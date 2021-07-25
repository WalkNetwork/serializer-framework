package io.github.uinnn.serializer.strategy

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder

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

class DefaultSerialDecoder<T>(
  val strategy: DecoderStrategy,
  val model: DeserializationStrategy<T>,
) : DeserializationStrategy<T> by model {
  override fun deserialize(decoder: Decoder): T {
    return model.deserialize(DefaultDecoder(strategy, decoder))
  }
}

class DefaultDecoder(val strategy: DecoderStrategy, val model: Decoder) : Decoder by model {
  override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T {
    return deserializer.deserialize(model)
  }

  override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
    return DefaultCompositeDecoder(strategy, model.beginStructure(descriptor))
  }
}

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