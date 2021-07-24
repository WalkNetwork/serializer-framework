package com.github.uinnn.serializer.common

import com.github.uinnn.serializer.*
import com.github.uinnn.serializer.formatter.StrategyFormatter
import com.github.uinnn.serializer.strategy.ColorStrategy
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
