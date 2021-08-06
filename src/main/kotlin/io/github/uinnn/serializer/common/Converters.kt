package io.github.uinnn.serializer.common

import io.github.uinnn.serializer.DefaultJsonStrategyFormat
import io.github.uinnn.serializer.DefaultProtocolBufferStrategyFormat
import io.github.uinnn.serializer.DefaultYamlStrategyFormat
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import net.benwoodworth.knbt.Nbt
import net.benwoodworth.knbt.decodeFromByteArray

/**
 * Converts this string to a value from a JSON string.
 * Note that the string must be a JSON string.
 */
inline fun <reified T : Any> String.convertFromJson(serializer: KSerializer<T> = T::class.serializer()): T {
  return DefaultJsonStrategyFormat.decodeFromString(serializer, this)
}

/**
 * Converts this string to a value from a YAML string.
 * Note that the string must be a YAML string.
 */
inline fun <reified T : Any> String.convertFromYaml(serializer: KSerializer<T> = T::class.serializer()): T {
  return DefaultYamlStrategyFormat.decodeFromString(serializer, this)
}

/**
 * Converts this byte array to a value from a Protobuf binary type.
 * Note that the byte array must be a protobuf binary type.
 */
inline fun <reified T : Any> ByteArray.convertFromProtobuf(serializer: KSerializer<T> = T::class.serializer()): T {
  return DefaultProtocolBufferStrategyFormat.decodeFromByteArray(serializer, this)
}

/**
 * Converts this byte array to a value from a NBT byte array.
 * Note that the byte array must be a nbt byte arrat.
 */
inline fun <reified T : Any> ByteArray.convertFromNBT(serializer: KSerializer<T> = T::class.serializer()): T {
  return Nbt.decodeFromByteArray(serializer, this)
}
