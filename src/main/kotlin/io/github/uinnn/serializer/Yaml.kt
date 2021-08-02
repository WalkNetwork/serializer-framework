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

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import io.github.uinnn.serializer.common.FrameworkModule
import io.github.uinnn.serializer.formatter.StrategyStringFormatter
import io.github.uinnn.serializer.strategy.ColorStrategy
import kotlinx.serialization.KSerializer
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
  Alterables.string(
    FrameworkModule,
    Yaml(FrameworkModule, YamlConfiguration(polymorphismStyle = PolymorphismStyle.Property))
  )
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
  override var file: File,
  override var model: T,
  override var serial: KSerializer<T>,
  override var format: AlterableStringFormat = DefaultYamlStrategyFormat
) : StringSerialFile<T> {
  override var settings: T = model
  override var observers: Observers = Observers()

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
  serial: KSerializer<T> = model::class.serializer() as KSerializer<T>,
  format: AlterableStringFormat = DefaultYamlStrategyFormat
): StringSerialFile<T> = YamlFile(file, model, serial, format)

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
  serial: KSerializer<T> = model::class.serializer() as KSerializer<T>,
  format: AlterableStringFormat = DefaultYamlStrategyFormat
): StringSerialFile<T> = YamlFile(File(dataFolder, "$file.yaml"), model, serial, format)

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
  format: AlterableStringFormat = DefaultYamlStrategyFormat
): StringSerialFile<T> = YamlFile(file, model.createInstance(), model.serializer(), format)

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
  format: AlterableStringFormat = DefaultYamlStrategyFormat
): StringSerialFile<T> =
  YamlFile(File(dataFolder, "$file.yaml"), model.createInstance(), model.serializer(), format)