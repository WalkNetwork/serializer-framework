package io.github.uinnn.serializer.common

import io.github.uinnn.serializer.*
import io.github.uinnn.serializer.formatter.StrategyFormatter
import io.github.uinnn.serializer.strategy.ColorStrategy
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.overwriteWith
import kotlinx.serialization.modules.plus
import java.io.File

/**
 * Reads all content in the file.
 */
inline val SerialFile<*>.content get() = file.readText()

/**
 * Sets the new serial file by
 * the gived file. Also loads then.
 */
fun <T : SerialFile<*>> T.transformFile(file: File) = apply {
  this.file = file
  load()
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