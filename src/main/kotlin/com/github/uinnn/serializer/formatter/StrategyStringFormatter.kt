package com.github.uinnn.serializer.formatter

import com.github.uinnn.serializer.strategy.DecoderStrategy
import com.github.uinnn.serializer.strategy.DefaultSerialDecoder
import com.github.uinnn.serializer.strategy.DefaultSerialEncoder
import com.github.uinnn.serializer.strategy.EncoderStrategy
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import kotlinx.serialization.modules.SerializersModule

class StrategyStringFormatter(
  override val model: StringFormat,
  encoder: EncoderStrategy,
  decoder: DecoderStrategy,
) : StrategyFormatter(model, encoder, decoder), StringFormat by model {
  override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String {
    return model.encodeToString(DefaultSerialEncoder(encoder, serializer), value)
  }

  override fun <T> decodeFromString(deserializer: DeserializationStrategy<T>, string: String): T {
    return model.decodeFromString(DefaultSerialDecoder(decoder, deserializer), string)
  }

  override val serializersModule: SerializersModule
    get() = model.serializersModule
}