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