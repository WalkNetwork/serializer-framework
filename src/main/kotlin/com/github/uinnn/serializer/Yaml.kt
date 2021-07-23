package com.github.uinnn.serializer

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import com.github.uinnn.serializer.common.FrameworkModule
import com.github.uinnn.serializer.formatter.StrategyStringFormatter
import com.github.uinnn.serializer.strategy.ColorStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import kotlinx.serialization.serializer
import org.bukkit.plugin.Plugin
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * The default YAML format.
 * This contains [FrameworkModule] as main module and
 * property as PolymorphismStyle.
 * This also is lazy init.
 */
val DefaultYamlFormat by lazy {
  Yaml(FrameworkModule, YamlConfiguration(polymorphismStyle = PolymorphismStyle.Property))
}

/**
 * The default YAML strategy format.
 * This contains [FrameworkModule] as main module,
 * property as PolymorphismStyle and [ColorStrategy] as
 * backend strategy encoder/decoder.
 * This also is lazy init.
 */
val DefaultYamlStrategyFormat by lazy {
  StrategyStringFormatter(DefaultYamlFormat, ColorStrategy, ColorStrategy)
}

/**
 * The serial file for coding with YAML files.
 * By default [format] is [DefaultYamlStrategyFormat].
 * Also loads the file when constructs a new instance of this class.
 */
class YamlFile<T : Any>(
  override val file: File,
  override var model: T,
  override val serial: KSerializer<T>,
  override val format: StringFormat = DefaultYamlStrategyFormat
) : StringSerialFile<T> {
  override var settings: T = model

  init {
    load()
  }
}

/**
 * Constructs and loads a YAML file.
 * The default format for YAML files is [DefaultYamlStrategyFormat],
 * thats contains a set of serializers and [ColorStrategy] as a
 * backend strategy, thats replaces all '§' to '&' and vice-versa
 * in strings and lists of strings!
 */
fun <T : Any> yaml(
  file: File,
  model: T,
  serial: KSerializer<T>,
  format: StringFormat = DefaultYamlStrategyFormat
) = YamlFile(file, model, serial, format)

/**
 * Constructs and loads a YAML file inside of the datafolder of this plugin.
 * This will inserts the [file] in the datafolder of
 * this plugin and with .yaml extension.
 * The default format for YAML files is [DefaultYamlStrategyFormat],
 * thats contains a set of serializers and [ColorStrategy] as a
 * backend strategy, thats replaces all '§' to '&' and vice-versa
 * in strings and lists of strings!
 */
fun <T : Any> Plugin.yaml(
  file: String,
  model: T,
  serial: KSerializer<T>,
  format: StringFormat = DefaultYamlStrategyFormat
) = YamlFile(File(dataFolder, "$file.yaml"), model, serial, format)

/**
 * Constructs and loads a YAML file.
 * The default format for YAML files is [DefaultYamlStrategyFormat],
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
fun <T : Any> yaml(
  file: File,
  model: KClass<T>,
  format: StringFormat = DefaultYamlStrategyFormat
) = YamlFile(file, model.createInstance(), model.serializer(), format)

/**
 * Constructs and loads a YAML file inside of the datafolder of this plugin.
 * This will inserts the [file] in the datafolder of
 * this plugin and with .yaml extension.
 * The default format for YAML files is [DefaultYamlStrategyFormat],
 * thats contains a set of serializers and [ColorStrategy] as a
 * backend strategy, thats replaces all '§' to '&' and vice-versa
 * in strings and listsa of strings!
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
fun <T : Any> Plugin.yaml(
  file: String,
  model: KClass<T>,
  format: StringFormat = DefaultYamlStrategyFormat
) = YamlFile(File(dataFolder, "$file.yaml"), model.createInstance(), model.serializer(), format)