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

package walkmc.serializer

import com.charleskorn.kaml.*
import kotlinx.serialization.*
import walkmc.*
import walkmc.serializer.common.*
import walkmc.serializer.formatter.*
import walkmc.serializer.strategy.*
import kotlin.reflect.*
import kotlin.reflect.full.*
import java.io.*

/**
 * The default YAML format.
 * This contains [FrameworkModule] as main module and
 * property as PolymorphismStyle.
 * This also is lazy init.
 */
val Yaml by lazy {
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
val YamlStrategy by lazy {
	StrategyStringFormatter(Yaml, ColorStrategy, ColorStrategy)
}

/**
 * The serial file for coding with YAML files.
 * By default [format] is [YamlStrategy].
 * Also loads the file when constructs a new instance of this class.
 */
open class YamlFile<T : Any>(
	override var file: File,
	override var model: T,
	override var serial: KSerializer<T> = model::class.serializer() as KSerializer<T>,
	override var format: AlterableStringFormat = YamlStrategy,
) : StringSerialFile<T> {
	override var data: T = model
	override var observers: Observers<T> = Observers()
	
	init {
		load()
	}
}

/**
 * Represents a string folder compost only with yaml serial files.
 * All yaml files loaded by the folder is compost of the model [T].
 */
open class YamlFolder<T : Any>(folder: File, model: T) : StringFolder<T>(folder, model) {
	init {
		implementsAll()
	}
	
	override fun implement(file: String, model: T) {
		implement(createYamlFile(File(folder, "$file.yaml"), model))
	}
	
	override fun search() = folder
		.files { extension == "yaml" || extension == "yml" }
		.map { createYamlFile(it, model) }
}

/**
 * Constructs and loads a YAML file.
 * The default format for YAML files is [YamlStrategy],
 * thats contains a set of serializers and [ColorStrategy] as a
 * backend strategy, thats replaces all '§' to '&' and vice-versa
 * in strings and lists of strings!
 */
fun <T : Any> createYamlFile(
	file: File,
	model: T,
	serial: KSerializer<T> = model::class.serializer() as KSerializer<T>,
	format: AlterableStringFormat = YamlStrategy,
): StringSerialFile<T> = YamlFile(file, model, serial, format)

/**
 * Constructs and loads a YAML file inside of the datafolder of this plugin.
 * This will inserts the [file] in the datafolder of
 * this plugin and with .yaml extension.
 * The default format for YAML files is [YamlStrategy],
 * thats contains a set of serializers and [ColorStrategy] as a
 * backend strategy, thats replaces all '§' to '&' and vice-versa
 * in strings and lists of strings!
 */
fun <T : Any> Plugin.createYamlFile(
	file: String,
	model: T,
	serial: KSerializer<T> = model::class.serializer() as KSerializer<T>,
	format: AlterableStringFormat = YamlStrategy,
): StringSerialFile<T> = YamlFile(File(dataFolder, "$file.yaml"), model, serial, format)

/**
 * Constructs and loads a YAML file.
 * The default format for YAML files is [YamlStrategy],
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
fun <T : Any> createYamlFile(
	file: File,
	model: KClass<T>,
	format: AlterableStringFormat = YamlStrategy,
): StringSerialFile<T> = YamlFile(file, model.createInstance(), model.serializer(), format)

/**
 * Constructs and loads a YAML file inside of the datafolder of this plugin.
 * This will inserts the [file] in the datafolder of
 * this plugin and with .yaml extension.
 * The default format for YAML files is [YamlStrategy],
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
fun <T : Any> Plugin.createYamlFile(
	file: String,
	model: KClass<T>,
	format: AlterableStringFormat = YamlStrategy,
): StringSerialFile<T> =
	YamlFile(File(dataFolder, "$file.yaml"), model.createInstance(), model.serializer(), format)

/**
 * Creates a yaml folder with all yaml serial files from the specified [file].
 */
fun <T : Any> createYamlFolder(file: File, model: T) = YamlFolder(file, model)

/**
 * Creates a yaml folder with all yaml serial files from the specified [file].
 */
fun <T : Any> Plugin.createYamlFolder(file: String, model: T) = YamlFolder(File(dataFolder, "$file.yaml"), model)