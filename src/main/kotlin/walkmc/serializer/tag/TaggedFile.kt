package walkmc.serializer.tag

import walkmc.extensions.*
import walkmc.serializer.tag.impl.*
import java.io.*

/**
 * The serial file for encoding with Mark files. This allows only the [CompoundTag] tag type.
 *
 * Use this instead of [CompoundTag] to store the key value and insert it into a file, because
 * this has a little of performance and uses a little less space compared to [CompoundTag].
 */
open class TaggedFile(var file: File) : CompoundTag() {
	init {
		file.create()
	}
	
	/**
	 * Writes all content of the [CompoundTag] of this tag file to the [file].
	 *
	 * @param close optionallys closes the opened output stream of the [file]. By default, is true.
	 */
	fun save(close: Boolean = true) = with(file.outputStream().toTagStream()) {
		write(this)
		if (close) close()
	}
	
	/**
	 * Reads the content stored in [file] and puts them in the [CompoundTag] of this tag file.
	 *
	 * @param close optionallys closes the opened input stream of the [file]. By default, is true.
	 */
	fun load(close: Boolean = true) = with(file.inputStream().toTagStream()) {
		read(this)
		if (close) close()
	}
	
	/**
	 * Clears the current stored contents in [file].
	 */
	fun clearFile() = file.writeText("")
}
