package com.github.uinnn.serializer

import com.github.uinnn.serializer.common.FrameworkModule
import com.github.uinnn.serializer.formatter.StrategyStringFormatter
import com.github.uinnn.serializer.strategy.ColorStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import kotlinx.serialization.serializer
import nl.adaptivity.xmlutil.serialization.XML
import org.bukkit.plugin.Plugin
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * The default XML format.
 * This contains [FrameworkModule] as main module.
 * This also is lazy init.
 */
val DefaultXMLFormat by lazy {
  XML(FrameworkModule) {
    autoPolymorphic = true
    repairNamespaces = true
  }
}

/**
 * The default XML strategy format.
 * This contains [FrameworkModule] as main module
 * and [ColorStrategy] as backend strategy encoder/decoder.
 * This also is lazy init.
 */
val DefaultXMLStrategyFormat by lazy {
  StrategyStringFormatter(DefaultXMLFormat, ColorStrategy, ColorStrategy)
}

/**
 * The serial file for coding with XML files.
 * By default [format] is [DefaultXMLStrategyFormat].
 * Also loads the file when constructs a new instance of this class.
 */
class XMLFile<T : Any>(
  override val file: File,
  override var model: T,
  override val serial: KSerializer<T>,
  override val format: StringFormat = DefaultXMLStrategyFormat
) : StringSerialFile<T> {
  override var settings: T = model

  init {
    load()
  }
}

/**
 * Constructs and loads a XML file.
 * The default format for XML files is [DefaultXMLStrategyFormat],
 * thats contains a set of serializers and [ColorStrategy] as a
 * backend strategy, thats replaces all '§' to '&' and vice-versa
 * in strings and lists of strings!
 */
fun <T : Any> xml(
  file: File,
  model: T,
  serial: KSerializer<T>,
  format: StringFormat = DefaultXMLStrategyFormat
) = XMLFile(file, model, serial, format)

/**
 * Constructs and loads a XML file inside of the datafolder of this plugin.
 * This will inserts the [file] in the datafolder of
 * this plugin and with .xml extension.
 * The default format for XML files is [DefaultXMLStrategyFormat],
 * thats contains a set of serializers and [ColorStrategy] as a
 * backend strategy, thats replaces all '§' to '&' and vice-versa
 * in strings and lists of strings!
 */
fun <T : Any> Plugin.xml(
  file: String,
  model: T,
  serial: KSerializer<T>,
  format: StringFormat = DefaultXMLStrategyFormat
) = XMLFile(File(dataFolder, "$file.xml"), model, serial, format)

/**
 * Constructs and loads a XML file.
 * The default format for XML files is [DefaultXMLStrategyFormat],
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
fun <T : Any> xml(
  file: File,
  model: KClass<T>,
  format: StringFormat = DefaultXMLStrategyFormat
) = XMLFile(file, model.createInstance(), model.serializer(), format)

/**
 * Constructs and loads a XML file inside of the datafolder of this plugin.
 * This will inserts the [file] in the datafolder of
 * this plugin and with .xml extension.
 * The default format for XML files is [DefaultXMLStrategyFormat],
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
fun <T : Any> Plugin.xml(
  file: String,
  model: KClass<T>,
  format: StringFormat = DefaultXMLStrategyFormat
) = XMLFile(File(dataFolder, "$file.xml"), model.createInstance(), model.serializer(), format)