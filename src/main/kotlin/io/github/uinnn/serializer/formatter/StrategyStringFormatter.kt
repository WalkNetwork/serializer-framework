package io.github.uinnn.serializer.formatter

import io.github.uinnn.serializer.AlterableStringFormat
import io.github.uinnn.serializer.strategy.DecoderStrategy
import io.github.uinnn.serializer.strategy.DefaultSerialDecoder
import io.github.uinnn.serializer.strategy.DefaultSerialEncoder
import io.github.uinnn.serializer.strategy.EncoderStrategy
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.SerializersModule

open class StrategyStringFormatter(
  override val model: AlterableStringFormat,
  encoder: EncoderStrategy,
  decoder: DecoderStrategy,
) : StrategyFormatter(model, encoder, decoder), AlterableStringFormat by model {
  override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String {
    return model.encodeToString(DefaultSerialEncoder(encoder, serializer), value)
  }

  override fun <T> decodeFromString(deserializer: DeserializationStrategy<T>, string: String): T {
    return model.decodeFromString(DefaultSerialDecoder(decoder, deserializer), string)
  }

  override var serializersModule: SerializersModule = super.serializersModule
}