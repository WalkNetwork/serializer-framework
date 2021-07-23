package com.github.uinnn.serializer.formatter

import com.github.uinnn.serializer.strategy.DecoderStrategy
import com.github.uinnn.serializer.strategy.DefaultSerialDecoder
import com.github.uinnn.serializer.strategy.DefaultSerialEncoder
import com.github.uinnn.serializer.strategy.EncoderStrategy
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.SerializersModule

class StrategyBinaryFormatter(
  override val model: BinaryFormat,
  encoder: EncoderStrategy,
  decoder: DecoderStrategy,
) : StrategyFormatter(model, encoder, decoder), BinaryFormat by model {
  override fun <T> encodeToByteArray(serializer: SerializationStrategy<T>, value: T): ByteArray {
    return model.encodeToByteArray(DefaultSerialEncoder(encoder, serializer), value)
  }

  override fun <T> decodeFromByteArray(deserializer: DeserializationStrategy<T>, bytes: ByteArray): T {
    return model.decodeFromByteArray(DefaultSerialDecoder(decoder, deserializer), bytes)
  }

  override val serializersModule: SerializersModule
    get() = model.serializersModule
}
