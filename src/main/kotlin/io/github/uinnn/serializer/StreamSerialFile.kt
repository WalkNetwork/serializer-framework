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