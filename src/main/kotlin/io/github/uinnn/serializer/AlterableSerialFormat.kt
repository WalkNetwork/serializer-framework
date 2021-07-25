package io.github.uinnn.serializer

import kotlinx.serialization.*
import kotlinx.serialization.modules.SerializersModule

interface AlterableSerialFormat : SerialFormat {
  override var serializersModule: SerializersModule
}

interface AlterableStringFormat : AlterableSerialFormat, StringFormat {
  override var serializersModule: SerializersModule
}

interface AlterableBinaryFormat : AlterableSerialFormat, BinaryFormat {
  override var serializersModule: SerializersModule
}

abstract class AbstractAlterableSerialFormat(
  override var serializersModule: SerializersModule,
  open var model: SerialFormat
) : AlterableSerialFormat

abstract class AbstractAlterableStringFormat(
  override var serializersModule: SerializersModule,
  open var model: StringFormat
) : AlterableStringFormat {
  override fun <T> decodeFromString(deserializer: DeserializationStrategy<T>, string: String): T {
    return model.decodeFromString(deserializer, string)
  }

  override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String {
    return model.encodeToString(serializer, value)
  }
}

abstract class AbstractAlterableBinaryFormat(
  override var serializersModule: SerializersModule,
  open var model: BinaryFormat
) : AlterableBinaryFormat {
  override fun <T> decodeFromByteArray(deserializer: DeserializationStrategy<T>, bytes: ByteArray): T {
    return model.decodeFromByteArray(deserializer, bytes)
  }

  override fun <T> encodeToByteArray(serializer: SerializationStrategy<T>, value: T): ByteArray {
    return model.encodeToByteArray(serializer, value)
  }
}

internal class AlterableSerialFormatImpl(
  module: SerializersModule,
  model: SerialFormat
) : AbstractAlterableSerialFormat(module, model)

internal class AlterableStringFormatImpl(
  module: SerializersModule,
  model: StringFormat
) : AbstractAlterableStringFormat(module, model)

internal class AlterableBinaryFormatImpl(
  module: SerializersModule,
  model: BinaryFormat
) : AbstractAlterableBinaryFormat(module, model)

/**
 * Object instance to create alterable serial formats.
 */
object Alterables {
  fun serial(module: SerializersModule, model: SerialFormat): AlterableSerialFormat {
    return AlterableSerialFormatImpl(module, model)
  }

  fun string(module: SerializersModule, model: StringFormat): AlterableStringFormat {
    return AlterableStringFormatImpl(module, model)
  }

  fun binary(module: SerializersModule, model: BinaryFormat): AlterableBinaryFormat {
    return AlterableBinaryFormatImpl(module, model)
  }
}