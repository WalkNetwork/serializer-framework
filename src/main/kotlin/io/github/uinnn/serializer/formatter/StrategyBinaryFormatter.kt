package io.github.uinnn.serializer.formatter

import io.github.uinnn.serializer.AlterableBinaryFormat
import io.github.uinnn.serializer.strategy.DecoderStrategy
import io.github.uinnn.serializer.strategy.DefaultSerialDecoder
import io.github.uinnn.serializer.strategy.DefaultSerialEncoder
import io.github.uinnn.serializer.strategy.EncoderStrategy
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.SerializersModule

/**
 * A binary strategy formatter.
 * This is used to modify how kotlin serialization
 * should encode or decode strings, numbers, chars and booleans.
 * This formatter is only able to use with [BinaryFormat]
 * serial files, suchs as Protocol Buffers file.
 */
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
