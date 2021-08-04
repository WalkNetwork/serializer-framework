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

import io.github.uinnn.serializer.common.Observable
import io.github.uinnn.serializer.common.ObserverKind
import kotlinx.serialization.KSerializer
import java.io.File

/**
 * A SerialFile is a configurable file that can
 * reloads, saves, etc with [Observable]. Serials files
 * holds a model to know what he will serialize or deserialize
 * in the content of the file.
 */
interface SerialFile<T : Any> : Observable {

  /**
   * The file object of
   * this serial file.
   */
  var file: File

  /**
   * A model initializer instance of
   * this serial file.
   */
  var model: T

  /**
   * The settings of this serial file,
   * this is, all save/loads/reloads
   * will update this property.
   */
  var settings: T

  /**
   * A serial instance of
   * this serial file.
   */
  var serial: KSerializer<T>

  /**
   * The format that this
   * serial file will be
   * encode/decode.
   */
  val format: AlterableSerialFormat

  /**
   * Loads for the first time
   * this serial file.
   */
  fun load()

  /**
   * Reloads the current file
   * and updates the [settings] property.
   */
  fun reload()

  /**
   * Saves the model of this
   * serial file.
   */
  fun saveModel()

  /**
   * Saves the [settings] to the file.
   */
  fun save()

  /**
   * Creates the file if not exists.
   */
  fun createFile(savesModel: Boolean = true) {
    if (!file.exists()) {
      file.parentFile.mkdirs()
      file.createNewFile()
      observe(ObserverKind.CREATE)
      if (savesModel) saveModel()
    }
  }

  override fun observe(kind: ObserverKind) {
    observers[kind]?.forEach { action ->
      action(this)
    }
  }
}

/**
 * A StringSerialFile is a [SerialFile] for String format
 * files, such as JSON and YAML.
 */
interface StringSerialFile<T : Any> : SerialFile<T> {
  override var format: AlterableStringFormat

  override fun load() {
    createFile()
    reload()
    observe(ObserverKind.LOAD)
  }

  override fun reload() {
    settings = format.decodeFromString(serial, file.readText())
    observe(ObserverKind.RELOAD)
  }

  override fun saveModel() {
    file.writeText(format.encodeToString(serial, model))
    observe(ObserverKind.SAVE_MODEL)
  }

  override fun save() {
    file.writeText(format.encodeToString(serial, settings))
    observe(ObserverKind.SAVE)
  }
}

/**
 * A BinarySerialFile is a [SerialFile] for Binary format
 * files, such as Protocol Buffers.
 */
interface BinarySerialFile<T : Any> : SerialFile<T> {
  override var format: AlterableBinaryFormat

  override fun load() {
    createFile()
    reload()
    observe(ObserverKind.LOAD)
  }

  override fun reload() {
    settings = format.decodeFromByteArray(serial, file.readBytes())
    observe(ObserverKind.RELOAD)
  }

  override fun save() {
    file.writeBytes(format.encodeToByteArray(serial, settings))
    observe(ObserverKind.SAVE)
  }

  override fun saveModel() {
    file.writeBytes(format.encodeToByteArray(serial, model))
    observe(ObserverKind.SAVE_MODEL)
  }
}

/**
 * A StreamSerialFile is a [SerialFile] for uses with serialization
 * libraries that implements the function for encoding and decoding
 * by a output/input stream, such as Named Binary Tag (NBT) files.
 */
interface StreamSerialFile<T : Any> : SerialFile<T> {
  override val format: AlterableStreamFormat

  override fun load() {
    createFile()
    reload()
    observe(ObserverKind.LOAD)
  }

  override fun reload() {
    settings = format.decodeFrom(file.inputStream().buffered(), serial)
    observe(ObserverKind.RELOAD)
  }

  override fun save() {
    format.encodeTo(file.outputStream().buffered(), serial, settings)
    observe(ObserverKind.SAVE)
  }

  override fun saveModel() {
    format.encodeTo(file.outputStream().buffered(), serial, model)
    observe(ObserverKind.SAVE_MODEL)
  }
}