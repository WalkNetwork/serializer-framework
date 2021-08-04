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

package io.github.uinnn.serializer.common

import io.github.uinnn.serializer.*
import io.github.uinnn.serializer.formatter.StrategyFormatter
import io.github.uinnn.serializer.strategy.ColorStrategy
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.overwriteWith
import kotlinx.serialization.modules.plus
import java.io.File

/**
 * Converts this file to a yaml serial file.
 */
inline fun <reified T : Any> File.asYaml() = createYamlFile(this, T::class)

/**
 * Converts this file to a json serial file.
 */
inline fun <reified T : Any> File.asJson() = createJsonFile(this, T::class)

/**
 * Converts this file to a protobuf serial file.
 */
inline fun <reified T : Any> File.asProtobuf() = createProtobufFile(this, T::class)

/**
 * Converts this file to a nbt serial file.
 */
inline fun <reified T : Any> File.asNBT() = createNBTFile(this, T::class)

/**
 * Converts this file to a yaml folder.
 */
inline fun <reified T : Any> File.asYamlFolder() = FolderFactory.createYamlFolder(this, T::class)

/**
 * Converts this file to a json folder.
 */
inline fun <reified T : Any> File.asJsonFolder() = FolderFactory.createJsonFolder(this, T::class)

/**
 * Converts this file to a protobuf folder.
 */
inline fun <reified T : Any> File.asProtobufFolder() = FolderFactory.createProtobufFolder(this, T::class)

/**
 * Converts this file to a NBT folder.
 */
inline fun <reified T : Any> File.asNBTFolder() = FolderFactory.createNBTFolder(this, T::class)

/**
 * Loads this serial file and returns itself.
 */
fun <T : SerialFile<*>> T.loadAndReturns(): T = apply {
  load()
}

/**
 * Reads all content as string in the file.
 */
inline val SerialFile<*>.textContent get() = file.readText()

/**
 * Reads all content as byte array in the file.
 */
inline val SerialFile<*>.byteContent get() = file.readBytes()

/**
 * Sets the new serial file by
 * the gived file. Also loads then.
 */
fun <T : SerialFile<*>> T.transformFile(file: File, load: Boolean = false) = apply {
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
  format = DefaultJsonStrategyFormat
}

/**
 * Sets the default format for this YAML File.
 */
fun YamlFile<*>.defaultFormat() = apply {
  format = DefaultYamlStrategyFormat
}

/**
 * Sets the default format for this Protocol Buffer File.
 */
fun ProtocolBufferFile<*>.defaultFormat() = apply {
  format = DefaultProtocolBufferStrategyFormat
}

/**
 * Sets the default format for this named binary tag File.
 */
fun NamedBinaryTagFile<*>.defaultFormat() = apply {
  format = DefaultNamedBinaryTagStrategyFormat
}

/**
 * Shortcut for adding a observer event
 * handler for saving a serial file.
 */
fun Observable.onSave(action: ObserverAction) = onObserve(ObserverKind.SAVE, action)

/**
 * Shortcut for adding a observer event
 * handler for loading a serial file.
 */
fun Observable.onLoad(action: ObserverAction) = onObserve(ObserverKind.LOAD, action)

/**
 * Shortcut for adding a observer event
 * handler for reloading a serial file.
 */
fun Observable.onReload(action: ObserverAction) = onObserve(ObserverKind.RELOAD, action)

/**
 * Shortcut for adding a observer event
 * handler for saving model of a serial file.
 */
fun Observable.onSaveModel(action: ObserverAction) = onObserve(ObserverKind.SAVE_MODEL, action)

/**
 * Shortcut for adding a observer event
 * handler for creating a serial file.
 */
fun Observable.onCreate(action: ObserverAction) = onObserve(ObserverKind.CREATE, action)