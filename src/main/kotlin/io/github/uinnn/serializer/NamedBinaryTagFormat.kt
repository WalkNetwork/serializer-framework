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

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.SerializersModule
import net.benwoodworth.knbt.Nbt
import net.benwoodworth.knbt.decodeFrom
import net.benwoodworth.knbt.encodeTo
import java.io.InputStream
import java.io.OutputStream

/**
 * A supporter class to be able to encode/decode Named Binary Tag (NBT) files
 * from a [AlterableStreamFormat].
 */
class NamedBinaryTagFormat(
  override var serializersModule: SerializersModule,
  var model: Nbt
) : AlterableStreamFormat {
  override fun <T> decodeFrom(input: InputStream, deserializer: DeserializationStrategy<T>): T {
    return input.use {
      model.decodeFrom(it, deserializer)
    }
  }

  override fun <T> encodeTo(output: OutputStream, serializer: SerializationStrategy<T>, value: T) {
    output.use {
      model.encodeTo(it, serializer, value)
    }
  }
}