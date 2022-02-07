package walkmc.serializer.tag.impl

import walkmc.serializer.tag.*
import java.io.*

/**
 * A tag implementation to stores [Byte].
 */
open class ByteTag(var value: Byte = 0) : Tag(1), NumberTag, Comparable<ByteTag> {
	override fun toByte(): Byte = value
	override fun toShort(): Short = value.toShort()
	override fun toInt(): Int = value.toInt()
	override fun toLong(): Long = value.toLong()
	override fun toFloat(): Float = value.toFloat()
	override fun toDouble(): Double = value.toDouble()
	
	override fun write(data: DataOutput) {
		try {
			data.writeByte(toInt())
		} catch (e: Exception) {
			data.writeByte(0)
		}
	}
	
	override fun read(data: DataInput) {
		value = try {
			data.readByte()
		} catch (e: Exception) {
			0
		}
	}
	
	fun copy() = value
	
	override fun compareTo(other: ByteTag): Int = value.compareTo(other.value)
	override fun equals(other: Any?): Boolean = other is ByteTag && value == other.value
	override fun hashCode(): Int = value.hashCode()
	override fun toString(): String = value.toString()
}
