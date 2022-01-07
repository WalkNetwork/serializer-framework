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

@file:Suppress("NOTHING_TO_INLINE")

package walkmc.serializer.common

import walkmc.serializer.*
import walkmc.serializer.tag.*
import java.io.*

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
inline fun <reified T : Any> File.asYamlFolder() = createYamlFolder(this, T::class)

/**
 * Converts this file to a json folder.
 */
inline fun <reified T : Any> File.asJsonFolder() = createJsonFolder(this, T::class)

/**
 * Converts this file to a protobuf folder.
 */
inline fun <reified T : Any> File.asProtobufFolder() = createProtobufFolder(this, T::class)

/**
 * Converts this file to a NBT folder.
 */
inline fun <reified T : Any> File.asNBTFolder() = createNBTFolder(this, T::class)

/**
 * Converts this file to a mark file.
 */
inline fun File.toMark() = TaggedFile(this)

/**
 * List all files from this file directory.
 */
fun File.files(): List<File> = listFiles()?.toList() ?: emptyList()

/**
 * List all files from this file directory filtered by the given [filter].
 */
fun File.files(filter: File.() -> Boolean): List<File> = listFiles(filter)?.toList() ?: emptyList()

/**
 * List all directories from this file directory.
 */
fun File.directories(): List<File> = files { isDirectory }

/**
 * List all directories from this file directory filtered by the given [filter].
 */
fun File.directories(filter: File.() -> Boolean): List<File> = files { isDirectory && filter() }
