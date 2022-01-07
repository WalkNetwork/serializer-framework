@file:Suppress("NOTHING_TO_INLINE")

package walkmc.serializer.tag

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.modules.*
import net.minecraft.server.*
import org.bukkit.*
import org.bukkit.inventory.ItemStack
import walkmc.extensions.*
import walkmc.serializer.common.*
import walkmc.serializer.tag.impl.writeCompound
import kotlin.reflect.*
import java.io.*
import java.util.*

/**
 * Represents a base encoder to serializing and encoding objects to a tag file.
 * This is created to be compatible with `Kotlin Serialization`.
 */
class TagEncoder(val output: DataOutput, var size: Int = 0) : AbstractEncoder() {
	override val serializersModule: SerializersModule = FrameworkModule
	
	override fun encodeBoolean(value: Boolean) = output.writeBoolean(value)
	override fun encodeByte(value: Byte) = output.writeByte(value.toInt())
	override fun encodeShort(value: Short) = output.writeShort(value.toInt())
	override fun encodeInt(value: Int) = output.writeInt(value)
	override fun encodeLong(value: Long) = output.writeLong(value)
	override fun encodeFloat(value: Float) = output.writeFloat(value)
	override fun encodeDouble(value: Double) = output.writeDouble(value)
	override fun encodeChar(value: Char) = output.writeChar(value.code)
	override fun encodeString(value: String) = output.writeUTF(value)
	override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) = output.writeInt(index)
	
	override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int): CompositeEncoder {
		encodeInt(collectionSize)
		return this
	}
	
	override fun encodeValue(value: Any) = when (value) {
		is ItemStack -> output.writeItem(value)
		is Location -> output.writeLocation(value)
		is NBTTagCompound -> output.writeCompound(value)
		is KClass<*> -> output.writeClass(value)
		is Class<*> -> output.writeClass(value.kotlin)
		is UUID -> output.writeUUID(value)
		else -> super.encodeValue(value)
	}
	
	override fun encodeNull() = encodeBoolean(false)
	override fun encodeNotNullMark() = encodeBoolean(true)
}

/**
 * Encodes the tag object of this output stream and deserializes them with the given [deserializer].
 *
 * @param close optionallys closes the output stream. By default is true.
 */
fun <T> OutputStream.encodeTag(
	deserializer: SerializationStrategy<T>,
	value: T,
	close: Boolean = true,
) = with(toTagStream()) {
	TagEncoder(this).encodeSerializableValue(deserializer, value)
	
	if (close)
		close()
}

/**
 * Encodes the tag object of this file opening the output stream
 * and deserializes them with the given [deserializer].
 *
 * @param close optionallys closes the output stream. By default is true.
 */
inline fun <T> File.encodeTag(
	deserializer: SerializationStrategy<T>,
	value: T,
	close: Boolean = true,
) = outputStream().encodeTag(deserializer, value, close)

/**
 * Encodes the tag object of this output stream and deserializes them.
 *
 * @param close optionallys closes the output stream. By default is true.
 */
inline fun <reified T> OutputStream.encodeTag(value: T, close: Boolean = true) =
	encodeTag(serializer(), value, close)

/**
 * Encodes the tag object of this file opening the output stream and deserializes them.
 *
 * @param close optionallys closes the output stream. By default is true.
 */
inline fun <reified T> File.encodeTag(value: T, close: Boolean = true) =
	outputStream().encodeTag(value, close)
