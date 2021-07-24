package com.github.uinnn.serializer.formatter

import com.github.uinnn.serializer.AlterableStringFormat
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
) : StrategyFormatter(model, encoder, decoder), AlterableStringFormat by model as AlterableStringFormat {
  override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String {
    return model.encodeToString(DefaultSerialEncoder(encoder, serializer), value)
  }

  override fun <T> decodeFromString(deserializer: DeserializationStrategy<T>, string: String): T {
    return model.decodeFromString(DefaultSerialDecoder(decoder, deserializer), string)
  }

  override var serializersModule: SerializersModule = super.serializersModule
}