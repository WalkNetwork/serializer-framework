/*
                             MIT License

                        Copyright (c) 2021 uin

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package io.github.uinnn.serializer

import io.github.uinnn.serializer.common.FrameworkModule
import io.github.uinnn.serializer.formatter.StrategyBinaryFormatter
import io.github.uinnn.serializer.strategy.ColorStrategy
import kotlinx.serialization.KSerializer
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
  Alterables.binary(FrameworkModule,
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
 * The serial file for coding with Protocol Buffers files.
 * By default [format] is [DefaultProtocolBufferStrategyFormat].
 * Also loads the file when constructs a new instance of this class.
 */
class ProtocolBufferFile<T : Any>(
  override var file: File,
  override var model: T,
  override var serial: KSerializer<T>,
  override var format: AlterableBinaryFormat = DefaultProtocolBufferStrategyFormat
) : BinarySerialFile<T> {
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
  format: AlterableBinaryFormat = DefaultProtocolBufferStrategyFormat
): BinarySerialFile<T> = ProtocolBufferFile(file, model, serial, format)

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
  format: AlterableBinaryFormat = DefaultProtocolBufferStrategyFormat
): BinarySerialFile<T> = ProtocolBufferFile(File(dataFolder, "$file.proto"), model, serial, format)

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
  format: AlterableBinaryFormat = DefaultProtocolBufferStrategyFormat
): BinarySerialFile<T> = ProtocolBufferFile(file, model.createInstance(), model.serializer(), format)

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
  format: AlterableBinaryFormat = DefaultProtocolBufferStrategyFormat
): BinarySerialFile<T> =
  ProtocolBufferFile(File(dataFolder, "$file.proto"), model.createInstance(), model.serializer(), format)