package io.github.uinnn.serializer.formatter

import io.github.uinnn.serializer.AlterableBinaryFormat
import io.github.uinnn.serializer.strategy.DecoderStrategy
import io.github.uinnn.serializer.strategy.DefaultSerialDecoder
import io.github.uinnn.serializer.strategy.DefaultSerialEncoder
import io.github.uinnn.serializer.strategy.EncoderStrategy
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.SerializersModule

open class StrategyBinaryFormatter(
  override val model: AlterableBinaryFormat,
  encoder: EncoderStrategy,
  decoder: DecoderStrategy,
) : StrategyFormatter(model, encoder, decoder), AlterableBinaryFormat by model {
  override fun <T> encodeToByteArray(serializer: SerializationStrategy<T>, value: T): ByteArray {
    return model.encodeToByteArray(DefaultSerialEncoder(encoder, serializer), value)
  }

  override fun <T> decodeFromByteArray(deserializer: DeserializationStrategy<T>, bytes: ByteArray): T {
    return model.decodeFromByteArray(DefaultSerialDecoder(decoder, deserializer), bytes)
  }

  override var serializersModule: SerializersModule = super.serializersModule
}
