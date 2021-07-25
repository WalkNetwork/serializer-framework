package io.github.uinnn.serializer

import io.github.uinnn.serializer.common.FrameworkModule
import io.github.uinnn.serializer.formatter.StrategyBinaryFormatter
import io.github.uinnn.serializer.strategy.ColorStrategy
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.serializer
import org.bukkit.plugin.Plugin
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * The default Protocol Buffer format.
 * This contains [FrameworkModule] as main module.
 * This also is lazy init.
 */
val DefaultProtocolBufferFormat by lazy {
  AlterableProtocolBufferFormat(FrameworkModule,
    ProtoBuf {
      encodeDefaults = true
      serializersModule = FrameworkModule
    }
  )
}

/**
 * The default Protocol Buffer format.
 * This contains [FrameworkModule] as main module and
 * [ColorStrategy] as backend strategy encoder/decoder.
 * This also is lazy init.
 */
val DefaultProtocolBufferStrategyFormat by lazy {
  StrategyBinaryFormatter(DefaultProtocolBufferFormat, ColorStrategy, ColorStrategy)
}

/**
 * This class is equals to [ProtoBuf] + [AlterableBinaryFormat].
 * Is only for type-safe use with alterable modules.
 */
class AlterableProtocolBufferFormat(
  override var serializersModule: SerializersModule,
  val model: ProtoBuf
) : io.github.uinnn.serializer.AlterableBinaryFormat {
  override fun <T> decodeFromByteArray(deserializer: DeserializationStrategy<T>, bytes: ByteArray): T {
    return model.decodeFromByteArray(deserializer, bytes)
  }

  override fun <T> encodeToByteArray(serializer: SerializationStrategy<T>, value: T): ByteArray {
    return model.encodeToByteArray(serializer, value)
  }
}

/**
 * The serial file for coding with Protocol Buffers files.
 * By default [format] is [DefaultProtocolBufferStrategyFormat].
 * Also loads the file when constructs a new instance of this class.
 */
class ProtocolBufferFile<T : Any>(
  override var file: File,
  override var model: T,
  override var serial: KSerializer<T>,
  override var format: io.github.uinnn.serializer.AlterableBinaryFormat = DefaultProtocolBufferStrategyFormat
) : io.github.uinnn.serializer.BinarySerialFile<T> {
  override var settings = model
  override var observers: Observers = Observers()

  init {
    load()
  }
}

/**
 * Constructs and loads a Protocol Buffer file.
 * The default format for Protocol Buffer files is [DefaultProtocolBufferStrategyFormat],
 * thats contains a set of serializers and [ColorStrategy] as a
 * backend strategy, thats replaces all '§' to '&' and vice-versa
 * in strings and lists of strings!
 */
fun <T : Any> protobuf(
  file: File,
  model: T,
  serial: KSerializer<T>,
  format: io.github.uinnn.serializer.AlterableBinaryFormat = DefaultProtocolBufferStrategyFormat
) = ProtocolBufferFile(file, model, serial, format)

/**
 * Constructs and loads a Protocol Buffer file inside of the datafolder of this plugin.
 * This will inserts the [file] in the datafolder of
 * this plugin and with .proto extension.
 * The default format for Protocol Buffer files is [DefaultProtocolBufferStrategyFormat],
 * thats contains a set of serializers and [ColorStrategy] as a
 * backend strategy, thats replaces all '§' to '&' and vice-versa
 * in strings and lists of strings!
 */
fun <T : Any> Plugin.protobuf(
  file: String,
  model: T,
  serial: KSerializer<T>,
  format: io.github.uinnn.serializer.AlterableBinaryFormat = DefaultProtocolBufferStrategyFormat
) = ProtocolBufferFile(File(dataFolder, "$file.proto"), model, serial, format)

/**
 * Constructs and loads a Protocol Buffer file.
 * The default format for Protocol Buffer files is [DefaultProtocolBufferStrategyFormat],
 * thats contains a set of serializers and [ColorStrategy] as a
 * backend strategy, thats replaces all '§' to '&' and vice-versa
 * in strings and lists of strings!
 *
 * ### Note:
 * This need the [model] kclass with all default constructors
 * or will be throw a error, because this just create a instance
 * using Kotlin Reflect.
 *
 * Example:
 *
 * ```kt
 * @Serializable
 * data class Settings(var name: String) // will be throw a error.
 * ```
 *
 * ```kt
 * @Serializable
 * data class Settings(var name: String = "uinnn") // works!
 * ```
 */
fun <T : Any> protobuf(
  file: File,
  model: KClass<T>,
  format: io.github.uinnn.serializer.AlterableBinaryFormat = DefaultProtocolBufferStrategyFormat
) = ProtocolBufferFile(file, model.createInstance(), model.serializer(), format)

/**
 * Constructs and loads a Protocol Buffer file inside of the datafolder of this plugin.
 * This will inserts the [file] in the datafolder of
 * this plugin and with .proto extension.
 * The default format for Protocol Buffer files is [DefaultProtocolBufferStrategyFormat],
 * thats contains a set of serializers and [ColorStrategy] as a
 * backend strategy, thats replaces all '§' to '&' and vice-versa
 * in strings and lists of strings!
 *
 *
 * ### Note:
 * This need the [model] kclass with all default constructors
 * or will be throw a error, because this just create a instance
 * using Kotlin Reflect.
 *
 * Example:
 *
 * ```kt
 * @Serializable
 * data class Settings(var name: String) // will be throw a error.
 * ```
 *
 * ```kt
 * @Serializable
 * data class Settings(var name: String = "uinnn") // works!
 * ```
 */
fun <T : Any> Plugin.protobuf(
  file: String,
  model: KClass<T>,
  format: io.github.uinnn.serializer.AlterableBinaryFormat = DefaultProtocolBufferStrategyFormat
) = ProtocolBufferFile(File(dataFolder, "$file.proto"), model.createInstance(), model.serializer(), format)