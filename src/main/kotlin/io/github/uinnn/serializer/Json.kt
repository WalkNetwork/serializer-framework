package io.github.uinnn.serializer

import io.github.uinnn.serializer.common.FrameworkModule
import io.github.uinnn.serializer.formatter.StrategyStringFormatter
import io.github.uinnn.serializer.strategy.ColorStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.bukkit.plugin.Plugin
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * The default JSON format.
 * This contains [FrameworkModule] as main module.
 * This also is lazy init.
 */
val DefaultJsonFormat by lazy {
  Alterables.string(FrameworkModule,
    Json {
      prettyPrint = true
      prettyPrintIndent = "  "
      encodeDefaults = true
      serializersModule = FrameworkModule
    }
  )
}

/**
 * The default JSON save format.
 * This contains [FrameworkModule] as main module.
 * This also is lazy init.
 *
 * ### Note:
 * This should only use to save data such as
 * database or in file. Not use this to settings.
 */
val DefaultJsonSaveFormat by lazy {
  Alterables.string(FrameworkModule,
    Json {
      encodeDefaults = true
      allowStructuredMapKeys = true
      serializersModule = FrameworkModule
    }
  )
}

/**
 * The default JSON format.
 * This contains [FrameworkModule] as main module and
 * [ColorStrategy] as backend strategy encoder/decoder.
 * This also is lazy init.
 */
val DefaultJsonStrategyFormat by lazy {
  StrategyStringFormatter(DefaultJsonFormat, ColorStrategy, ColorStrategy)
}

/**
 * The default JSON save format.
 * This contains [FrameworkModule] as main module and
 * [ColorStrategy] as backend strategy encoder/decoder.
 * This also is lazy init.
 *
 * ### Note:
 * This should only use to save data such as
 * database or in file. Not use this to settings.
 */
val DefaultJsonStrategySaveFormat by lazy {
  StrategyStringFormatter(DefaultJsonSaveFormat, ColorStrategy, ColorStrategy)
}

/**
 * The serial file for coding with JSON files.
 * By default [format] is [DefaultJsonStrategyFormat].
 * Also loads the file when constructs a new instance of this class.
 */
class JsonFile<T : Any>(
  override var file: File,
  override var model: T,
  override var serial: KSerializer<T>,
  override var format: AlterableStringFormat = DefaultJsonStrategyFormat
) : StringSerialFile<T> {
  override var settings: T = model
  override var observers: Observers = Observers()

  init {
    load()
  }
}

/**
 * Constructs and loads a JSON file.
 * The default format for JSON files is [DefaultJsonStrategyFormat],
 * thats contains a set of serializers and [ColorStrategy] as a
 * backend strategy, thats replaces all '§' to '&' and vice-versa
 * in strings and lists of strings!
 */
fun <T : Any> json(
  file: File,
  model: T,
  serial: KSerializer<T>,
  format: AlterableStringFormat = DefaultJsonStrategyFormat
) = JsonFile(file, model, serial, format)

/**
 * Constructs and loads a JSON file inside of the datafolder of this plugin.
 * This will inserts the [file] in the datafolder of
 * this plugin and with .json extension.
 * The default format for JSON files is [DefaultJsonStrategyFormat],
 * thats contains a set of serializers and [ColorStrategy] as a
 * backend strategy, thats replaces all '§' to '&' and vice-versa
 * in strings and lists of strings!
 */
fun <T : Any> Plugin.json(
  file: String,
  model: T,
  serial: KSerializer<T>,
  format: AlterableStringFormat = DefaultJsonStrategyFormat
) = JsonFile(File(dataFolder, "$file.json"), model, serial, format)

/**
 * Constructs and loads a JSON file.
 * The default format for JSON files is [DefaultJsonStrategyFormat],
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
fun <T : Any> json(
  file: File,
  model: KClass<T>,
  format: AlterableStringFormat = DefaultJsonStrategyFormat
) = JsonFile(file, model.createInstance(), model.serializer(), format)

/**
 * Constructs and loads a JSON file inside of the datafolder of this plugin.
 * This will inserts the [file] in the datafolder of
 * this plugin and with .json extension.
 * The default format for JSON files is [DefaultJsonStrategyFormat],
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
fun <T : Any> Plugin.json(
  file: String,
  model: KClass<T>,
  format: AlterableStringFormat = DefaultJsonStrategyFormat
) = JsonFile(File(dataFolder, "$file.json"), model.createInstance(), model.serializer(), format)