@file:Suppress("NOTHING_TO_INLINE")

package walkmc.serializer.tag

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.modules.*
import walkmc.serializer.common.*
import java.io.*

/**
 * Represents a base decoder to deserializing and decoding objects to a tag file.
 * This is created to be compatible with `Kotlin Serialization`.
 */
class TagDecoder(val input: DataInput, var size: Int = 0) : AbstractDecoder() {
	private var elementIndex = 0
	override val serializersModule: SerializersModule = FrameworkModule
	
	override fun decodeBoolean(): Boolean = input.readBoolean()
	override fun decodeByte(): Byte = input.readByte()
	override fun decodeShort(): Short = input.readShort()
	override fun decodeInt(): Int = input.readInt()
	override fun decodeLong(): Long = input.readLong()
	override fun decodeFloat(): Float = input.readFloat()
	override fun decodeDouble(): Double = input.readDouble()
	override fun decodeChar(): Char = input.readChar()
	override fun decodeString(): String = input.readUTF()
	override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = input.readInt()
	
	override fun decodeElementIndex(descriptor: SerialDescriptor): Int =
		if (elementIndex == size) CompositeDecoder.DECODE_DONE else elementIndex++
	
	override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder =
		TagDecoder(input, descriptor.elementsCount)
	
	override fun decodeCollectionSize(descriptor: SerialDescriptor): Int =
		decodeInt().also { size = it }
	
	override fun decodeSequentially(): Boolean = true
	override fun decodeNotNullMark(): Boolean = decodeBoolean()
}

/**
 * Decodes the tag object of this input stream and deserializes them with the given [deserializer].
 *
 * @param close optionallys closes the input stream. By default is true.
 */
fun <T> InputStream.decodeTag(
	deserializer: DeserializationStrategy<T>,
	close: Boolean = true,
): T = with(toTagStream()) {
	val value = TagDecoder(this).decodeSerializableValue(deserializer)
	
	if (close)
		close()
	
	value
}

/**
 * Decodes the tag object of this file opening the input stream
 * and deserializes them with the given [deserializer].
 *
 * @param close optionallys closes the input stream. By default is true.
 */
inline fun <T> File.decodeTag(
	deserializer: DeserializationStrategy<T>,
	close: Boolean = true,
): T = inputStream().decodeTag(deserializer, close)

/**
 * Decodes the tag object of this input stream and deserializes them.
 *
 * @param close optionallys closes the input stream. By default is true.
 */
inline fun <reified T> InputStream.decodeTag(close: Boolean = true): T = decodeTag(serializer(), close)

/**
 * Decodes the tag object of this file opening the input stream and deserializes them.
 *
 * @param close optionallys closes the input stream. By default is true.
 */
inline fun <reified T> File.decodeTag(close: Boolean = true): T = inputStream().decodeTag(close)
