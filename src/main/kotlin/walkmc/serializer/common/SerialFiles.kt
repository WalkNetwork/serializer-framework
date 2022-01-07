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

package walkmc.serializer.common

import kotlinx.serialization.modules.*
import walkmc.serializer.*
import walkmc.serializer.formatter.*
import walkmc.serializer.strategy.*
import java.io.*

/**
 * Loads this serial file and returns itself.
 */
fun <T : SerialFile<*>> T.loadAndReturns(): T = apply {
	load()
}

/**
 * Reads all content as string in the file.
 */
val SerialFile<*>.textContent get() = file.readText()

/**
 * Reads all content as byte array in the file.
 */
val SerialFile<*>.byteContent get() = file.readBytes()

/**
 * Sets the new serial file by
 * the gived file. Also loads then.
 */
fun <T : SerialFile<*>> T.moveTo(file: File, load: Boolean = false) = apply {
	this.file.copyTo(file, true)
	this.file = file
	if (load) load()
}

/**
 * Clears all content of the
 * file of this serial file.
 */
fun SerialFile<*>.clear() = file.writeText("")

/**
 * Appends a module with this serial file format module.
 * This will not be overwrite existing contextuals serializers
 * @see SerializersModule.plus
 */
fun SerialFile<*>.appendModule(module: SerializersModule) {
	format.serializersModule += module
}

/**
 * Operator util function to append a module to this serial file.
 */
operator fun SerialFile<*>.plus(module: SerializersModule) {
	appendModule(module)
}

/**
 * Overwrites a module with this serial file format module.
 * This will overwrite existing contextuals serializers
 * @see SerializersModule.overwriteWith
 */
fun SerialFile<*>.overwriteModule(module: SerializersModule) {
	format.serializersModule = format.serializersModule overwriteWith module
}

/**
 * Sets the encoder and decoder to defaults.
 * Default: [ColorStrategy]
 */
fun StrategyFormatter.defaults() = apply {
	encoder = ColorStrategy
	decoder = ColorStrategy
}

/**
 * Sets the default format for this JSON File.
 */
fun JsonFile<*>.defaultFormat() = apply {
	format = JsonStrategy
}

/**
 * Sets the default format for this YAML File.
 */
fun YamlFile<*>.defaultFormat() = apply {
	format = YamlStrategy
}

/**
 * Sets the default format for this Protocol Buffer File.
 */
fun ProtobufFile<*>.defaultFormat() = apply {
	format = ProtobufStrategy
}

/**
 * Sets the default format for this named binary tag File.
 */
fun NBTFile<*>.defaultFormat() = apply {
	format = NBTStrategy
}
