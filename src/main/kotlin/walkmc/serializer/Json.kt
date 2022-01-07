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

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.bukkit.plugin.*
import walkmc.serializer.common.*
import walkmc.serializer.formatter.*
import walkmc.serializer.strategy.*
import kotlin.reflect.*
import kotlin.reflect.full.*
import java.io.*

/**
 * The default JSON format.
 * This contains [FrameworkModule] as main module.
 * This also is lazy init.
 */
val Json by lazy {
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
val JsonSave by lazy {
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
val JsonStrategy by lazy {
	StrategyStringFormatter(Json, ColorStrategy, ColorStrategy)
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
val JsonStrategySave by lazy {
	StrategyStringFormatter(JsonSave, ColorStrategy, ColorStrategy)
}

/**
 * The serial file for coding with JSON files.
 * By default [format] is [JsonStrategy].
 * Also loads the file when constructs a new instance of this class.
 */
open class JsonFile<T : Any>(
	override var file: File,
	override var model: T,
	override var serial: KSerializer<T> = model::class.serializer() as KSerializer<T>,
	override var format: AlterableStringFormat = JsonStrategy,
) : StringSerialFile<T> {
	override var data: T = model
	override var observers: Observers<T> = Observers()
	
	init {
		load()
	}
}

/**
 * Represents a string folder compost only with json serial files.
 * All json files loaded by the folder is compost of the model [T].
 */
open class JsonFolder<T : Any>(folder: File, model: T) : StringFolder<T>(folder, model) {
	init {
		implementsAll()
	}
	
	override fun implement(file: String, model: T) {
		implement(createJsonFile(File(folder, "$file.json"), model))
	}
	
	override fun search() = folder
		.files { extension == "json" }
		.map { createJsonFile(it, model) }
}

/**
 * Constructs and loads a JSON file.
 * The default format for JSON files is [JsonStrategy],
 * thats contains a set of serializers and [ColorStrategy] as a
 * backend strategy, thats replaces all '§' to '&' and vice-versa
 * in strings and lists of strings!
 */
fun <T : Any> createJsonFile(
	file: File,
	model: T,
	serial: KSerializer<T> = model::class.serializer() as KSerializer<T>,
	format: AlterableStringFormat = JsonStrategy,
): StringSerialFile<T> = JsonFile(file, model, serial, format)

/**
 * Constructs and loads a JSON file inside of the datafolder of this plugin.
 * This will inserts the [file] in the datafolder of
 * this plugin and with .json extension.
 * The default format for JSON files is [JsonStrategy],
 * thats contains a set of serializers and [ColorStrategy] as a
 * backend strategy, thats replaces all '§' to '&' and vice-versa
 * in strings and lists of strings!
 */
fun <T : Any> Plugin.createJsonFile(
	file: String,
	model: T,
	serial: KSerializer<T> = model::class.serializer() as KSerializer<T>,
	format: AlterableStringFormat = JsonStrategy,
): StringSerialFile<T> = JsonFile(File(dataFolder, "$file.json"), model, serial, format)

/**
 * Constructs and loads a JSON file.
 * The default format for JSON files is [JsonStrategy],
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
fun <T : Any> createJsonFile(
	file: File,
	model: KClass<T>,
	format: AlterableStringFormat = JsonStrategy,
): StringSerialFile<T> = JsonFile(file, model.createInstance(), model.serializer(), format)

/**
 * Constructs and loads a JSON file inside of the datafolder of this plugin.
 * This will inserts the [file] in the datafolder of
 * this plugin and with .json extension.
 * The default format for JSON files is [JsonStrategy],
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
 * ```
 * @Serializable
 * data class Settings(var name: String) // will be throw a error.
 * ```
 *
 * ```
 * @Serializable
 * data class Settings(var name: String = "uinnn") // works!
 * ```
 */
fun <T : Any> Plugin.createJsonFile(
	file: String,
	model: KClass<T>,
	format: AlterableStringFormat = JsonStrategy,
): StringSerialFile<T> =
	JsonFile(File(dataFolder, "$file.json"), model.createInstance(), model.serializer(), format)

/**
 * Creates a json folder with all json serial files
 * from the specified [file].
 */
fun <T : Any> createJsonFolder(file: File, model: T) = JsonFolder(file, model)

/**
 * Creates a json folder with all json serial files
 * from the specified [file].
 */
fun <T : Any> Plugin.createJsonFolder(file: String, model: T) = JsonFolder(File(dataFolder, "$file.json"), model)

