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
import net.benwoodworth.knbt.*
import org.bukkit.plugin.*
import walkmc.extensions.*
import walkmc.serializer.common.*
import walkmc.serializer.formatter.*
import walkmc.serializer.strategy.*
import kotlin.reflect.*
import kotlin.reflect.full.*
import java.io.*

/**
 * Creates a nbt file with all provided params.
 *
 * This is a shortcut for non-creating any class/object for the config file.
 */
fun <T : Any> nbt(
	file: File,
	model: T,
	serial: KSerializer<T> = model::class.serializer().cast(),
	format: StrategyBinaryFormatter = NBTStrategy,
	callback: NBTFile<T>.() -> Unit = {}
) = NBTFile(file, model, serial, format).apply(callback)

/**
 * The default Named Binary Tag (NBT) format.
 * This contains [FrameworkModule] as main module.
 * This also is lazy init.
 */
val NBT by lazy {
	Alterables.binary(FrameworkModule,
		Nbt {
			variant = NbtVariant.Java
			compression = NbtCompression.Gzip
			encodeDefaults = true
			compressionLevel = 3
			serializersModule = FrameworkModule
		}
	)
}

/**
 * The default Named Binary Tag (NBT) format.
 * This contains [FrameworkModule] as main module and
 * [ColorStrategy] as backend strategy encoder/decoder.
 * This also is lazy init.
 */
val NBTStrategy by lazy {
	StrategyBinaryFormatter(NBT, ColorStrategy, ColorStrategy)
}

/**
 * The serial file for coding with Named Binary Tag (NBT) files.
 * By default [format] is [NBTStrategy].
 * Also loads the file when constructs a new instance of this class.
 */
open class NBTFile<T : Any>(
	override var file: File,
	override var model: T,
	override var serial: KSerializer<T> = model::class.serializer().cast(),
	override var format: AlterableBinaryFormat = NBTStrategy,
) : BinarySerialFile<T> {
	override var data = model
	override var observers: Observers<T> = Observers()
	
	init {
		load()
	}
}

/**
 * Represents a binary folder compost only with named binary tag serial files.
 * All named binary tag files loaded by the folder is compost of the model [T].
 */
open class NBTFolder<T : Any>(folder: File, model: T) : BinaryFolder<T>(folder, model) {
	init {
		implementsAll()
	}
	
	override fun implement(file: String, model: T) {
		implement(createNBTFile(File(folder, "$file.dat"), model))
	}
	
	override fun search() = folder
		.files { extension == "dat" }
		.map { createNBTFile(it, model) }
}

/**
 * Constructs and loads a Named Binary Tag (NBT) file.
 * The default format for Named Binary Tag (NBT) files is [NBTStrategy],
 * that's contains a set of serializers and [ColorStrategy] as a
 * backend strategy, that's replaces all '§' to '&' and vice-versa
 * in strings and lists of strings!
 */
fun <T : Any> createNBTFile(
	file: File,
	model: T,
	serial: KSerializer<T> = model::class.serializer().cast(),
	format: AlterableBinaryFormat = NBTStrategy,
): BinarySerialFile<T> = NBTFile(file, model, serial, format)

/**
 * Constructs and loads a Named Binary Tag (NBT) file inside the datafolder of this plugin.
 * This will insert the [file] in the datafolder of
 * this plugin and with .dat extension.
 * The default format for Named Binary Tag (NBT) files is [NBTStrategy],
 * that's contains a set of serializers and [ColorStrategy] as a
 * backend strategy, that's replaces all '§' to '&' and vice-versa
 * in strings and lists of strings!
 */
fun <T : Any> Plugin.createNBTFile(
	file: String,
	model: T,
	serial: KSerializer<T> = model::class.serializer().cast(),
	format: AlterableBinaryFormat = NBTStrategy,
): BinarySerialFile<T> = NBTFile(File(dataFolder, "$file.dat"), model, serial, format)

/**
 * Constructs and loads a Named Binary Tag (NBT) file.
 * The default format for Named Binary Tag (NBT) files is [NBTStrategy],
 * that's contains a set of serializers and [ColorStrategy] as a
 * backend strategy, that's replaces all '§' to '&' and vice-versa
 * in strings and lists of strings!
 *
 * ### Note:
 * This need the [model] kclass with all default constructors
 * or will be thrown an error, because this just create an instance
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
fun <T : Any> createNBTFile(
	file: File,
	model: KClass<T>,
	format: AlterableBinaryFormat = NBTStrategy,
): BinarySerialFile<T> = NBTFile(file, model.createInstance(), model.serializer(), format)

/**
 * Constructs and loads a Named Binary Tag (NBT) file inside the datafolder of this plugin.
 * This will insert the [file] in the datafolder of
 * this plugin and with .dat extension.
 * The default format for Named Binary Tag (NBT) files is [NBTStrategy],
 * that's contains a set of serializers and [ColorStrategy] as a
 * backend strategy, that's replaces all '§' to '&' and vice-versa
 * in strings and lists of strings!
 *
 *
 * ### Note:
 * This need the [model] kclass with all default constructors
 * or will be thrown an error, because this just create an instance
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
fun <T : Any> Plugin.createNBTFile(
	file: String,
	model: KClass<T>,
	format: AlterableBinaryFormat = NBTStrategy,
): BinarySerialFile<T> =
	NBTFile(File(dataFolder, "$file.dat"), model.createInstance(), model.serializer(), format)

/**
 * Creates a nbt folder with all nbt serial files
 * from the specified [file].
 */
fun <T : Any> createNBTFolder(file: File, model: T) = NBTFolder(file, model)

/**
 * Creates a nbt folder with all nbt serial files
 * from the specified [file].
 */
fun <T : Any> Plugin.createNBTFolder(file: String, model: T) = NBTFolder(File(dataFolder, "$file.dat"), model)
