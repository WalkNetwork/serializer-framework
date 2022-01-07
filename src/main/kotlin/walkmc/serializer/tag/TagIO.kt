@file:Suppress("NOTHING_TO_INLINE")

package walkmc.serializer.tag

import walkmc.extensions.*
import walkmc.serializer.tag.impl.*
import java.io.*
import java.util.zip.*

/**
 * A tag IO represents a writer and reader for tag types.
 */
object TagIO {
	
	/**
	 * Writes the specified [tag] to the [output] stream, and optionallys closes the output stream.
	 */
	fun write(tag: Tag, output: OutputStream, close: Boolean = true) = with(output.toTagStream()) {
		writeShort(tag.id.toInt())
		tag.write(this)
		
		if (close)
			close()
	}
	
	/**
	 * Reads the specified [input] stream, and optionallys closes the input stream.
	 */
	fun read(input: InputStream, close: Boolean = true): Tag = with(input.toTagStream()) {
		val id = readShort()
		val tag = createTagOrNull(id) ?: error("No tag type found with id $id")
		
		tag.read(this)
		if (close)
			close()
		
		tag
	}
}

/**
 * Converts this output stream to an output that's tags uses.
 */
inline fun OutputStream.toTagStream(): DataOutputStream =
	DataOutputStream(BufferedOutputStream(GZIPOutputStream(this)))

/**
 * Converts this input stream to an input that's tags uses.
 */
inline fun InputStream.toTagStream(): DataInputStream =
	DataInputStream(BufferedInputStream(GZIPInputStream(this)))

/**
 * Firstly converts this output stream to a tag output stream and
 * writes the content of the specified [tag] to this output stream.
 *
 * @param close optionallys closes this output stream, by default is true.
 */
inline fun OutputStream.writeTag(tag: Tag, close: Boolean = true) = TagIO.write(tag, this, close)

/**
 * Firstly open the output stream of this file and converts them to a tag output stream and
 * writes the content of the specified [tag] to this file.
 *
 * @param close optionallys closes the opened output stream, by default is true.
 */
inline fun File.writeTag(tag: Tag, close: Boolean = true) = outputStream().writeTag(tag, close)

/**
 * Firstly converts this output stream to a tag input stream and
 * reads the content of this input stream and converts to a [Tag].
 *
 * @param close optionallys closes this input stream, by default is true.
 */
inline fun InputStream.readTag(close: Boolean = true) = TagIO.read(this, close)

/**
 * Firstly open the input stream of this file and converts them to a tag input stream and
 * reads the content of this file and converts to a [Tag].
 *
 * @param close optionallys closes the opened input stream, by default is true.
 */
inline fun File.readTag(close: Boolean = true) = inputStream().readTag(close)

/**
 * Firstly converts this output stream to a tag input stream and
 * reads the content of this input stream and converts to a [Tag] and casts them.
 *
 * @param close optionallys closes this input stream, by default is true.
 */
@JvmName("readMarkOf")
inline fun <T : Tag> InputStream.readTag(close: Boolean = true): T = TagIO.read(this, close).cast()

/**
 * Firstly open the input stream of this file and converts them to a tag input stream and
 * reads the content of this file and converts to a [Tag] and casts them.
 *
 * @param close optionallys closes the opened input stream, by default is true.
 */
@JvmName("readMarkOf")
inline fun <T : Tag> File.readTag(close: Boolean = true): T = inputStream().readTag<T>(close)

/**
 * Firstly converts this output stream to a tag input stream and
 * reads the content of this input stream and converts to a [Tag] and casts them to a [CompoundTag].
 *
 * @param close optionallys closes this input stream, by default is true.
 */
@JvmName("readMarkGroup")
inline fun InputStream.readTagGroup(close: Boolean = true): CompoundTag = TagIO.read(this, close).cast()

/**
 * Firstly open the input stream of this file and converts them to a tag input stream and
 * reads the content of this file and converts to a [Tag] and casts them to a [CompoundTag].
 *
 * @param close optionallys closes the opened input stream, by default is true.
 */
@JvmName("readMarkGroup")
inline fun File.readTagGroup(close: Boolean = true): CompoundTag = inputStream().readTagGroup(close)

/**
 * Writes this tag to the specified [output] stream by converting to a tag output stream and
 * finally writing the content of this tag to the output.
 *
 * @param close optionallys closes the output stream, by default is true.
 */
inline fun Tag.writeTo(output: OutputStream, close: Boolean = true) = output.writeTag(this, close)

/**
 * Writes this tag to the specified [file] by opening an output stream and them
 * converting to a tag output stream, and finally writing the content of this tag to the file.
 *
 * @param close optionallys closes the open output stream, by default is true.
 */
inline fun Tag.writeTo(file: File, close: Boolean = true) = file.writeTag(this, close)
