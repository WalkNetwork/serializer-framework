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

import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * A factory object for creating serial folders.
 */
object FolderFactory {

  /**
   * Creates a yaml folder with all yaml serial files
   * from the specified [file].
   */
  fun <T : Any> createYamlFolder(file: File, model: T) = YamlFolder(file, model)

  /**
   * Creates a yaml folder with all yaml serial files
   * from the specified [file]. This uses reflection for
   * creating a model instance.
   */
  fun <T : Any> createYamlFolder(file: File, model: KClass<T>) = YamlFolder(file, model.createInstance())

  /**
   * Creates a json folder with all json serial files
   * from the specified [file].
   */
  fun <T : Any> createJsonFolder(file: File, model: T) = JsonFolder(file, model)

  /**
   * Creates a json folder with all json serial files
   * from the specified [file]. This uses reflection for
   * creating a model instance.
   */
  fun <T : Any> createJsonFolder(file: File, model: KClass<T>) = JsonFolder(file, model.createInstance())

  /**
   * Creates a protobuf folder with all protobuf serial files
   * from the specified [file].
   */
  fun <T : Any> createProtobufFolder(file: File, model: T) = ProtocolBufferFolder(file, model)

  /**
   * Creates a protobuf folder with all protobuf serial files
   * from the specified [file]. This uses reflection for
   * creating a model instance.
   */
  fun <T : Any> createProtobufFolder(file: File, model: KClass<T>) =
    ProtocolBufferFolder(file, model.createInstance())

  /**
   * Creates a nbt folder with all nbt serial files
   * from the specified [file].
   */
  fun <T : Any> createNBTFolder(file: File, model: T) = NamedBinaryTagFolder(file, model)

  /**
   * Creates a nbt folder with all nbt serial files
   * from the specified [file]. This uses reflection for
   * creating a model instance.
   */
  fun <T : Any> createNBTFolder(file: File, model: KClass<T>) = NamedBinaryTagFolder(file, model.createInstance())
}