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

package walkmc.serializer

import walkmc.collections.*
import walkmc.serializer.common.*
import java.io.*
import java.util.*

/**
 * Represents a folder of serial lists. This is useful when you
 * have to store more than one serial files in a folder.
 */
abstract class Folder<K : Any, T : SerialFile<K>>(
	var folder: File,
	var model: K,
) : IndexList<T>() {
	init {
		if (!folder.exists()) {
			folder.mkdirs()
		}
	}
	
	fun implementModel(file: String) = implement(file, model)
	
	/**
	 * Searchs all files inside this serial folder and adds them.
	 */
	abstract fun search(): List<T>
	
	/**
	 * Implements, add and creates a serial file inside this serial folder.
	 */
	abstract fun implement(file: String, model: K)
	
	/**
	 * Implements and add a fully molded model serial file
	 * inside this serial folder.
	 */
	fun implement(model: T, load: Boolean = true) {
		add(model.moveTo(File(folder, model.file.name), load))
	}
	
	/**
	 * Implements all searcheds serial files inside this serial folder.
	 */
	fun implementsAll() = addAll(search())
	
	/**
	 * Loads all serial files from this folder.
	 */
	fun loadAll() = forEach(SerialFile<*>::load)
	
	/**
	 * Reloads all serial files from this folder.
	 */
	fun reloadAll() = forEach(SerialFile<*>::reload)
	
	/**
	 * Saves all serial files from this folder.
	 */
	fun saveAll() = forEach(SerialFile<*>::save)
	
	/**
	 * Saves all serial files models from this folder.
	 */
	fun saveAllModel() = forEach(SerialFile<*>::saveModel)
}

/**
 * Represents a string serial folder, composts of [StringSerialFile].
 */
abstract class StringFolder<T : Any>(folder: File, model: T) : Folder<T, StringSerialFile<T>>(folder, model)

/**
 * Represents a binary serial folder, composts of [BinarySerialFile].
 */
abstract class BinaryFolder<T : Any>(folder: File, model: T) : Folder<T, BinarySerialFile<T>>(folder, model)

/**
 * Represents a mark serial folder, composts of [TagSerialFile].
 */
abstract class MarkFolderBase<T : Any>(folder: File, model: T) : Folder<T, TagSerialFile<T>>(folder, model)

/**
 * Represents a stream serial folder, composts of [StreamSerialFile].
 */
abstract class StreamFolder<T : Any>(folder: File, model: T) : Folder<T, StreamSerialFile<T>>(folder, model)
