package walkmc.serializer.tag.impl

import walkmc.serializer.tag.*
import java.io.*

/**
 * A tag implementation to stores [Short].
 */
open class ShortTag(var value: Short = 0) : Tag(2), NumberTag, Comparable<ShortTag> {
	override fun toByte(): Byte = value.toByte()
	override fun toShort(): Short = value
	override fun toInt(): Int = value.toInt()
	override fun toLong(): Long = value.toLong()
	override fun toFloat(): Float = value.toFloat()
	override fun toDouble(): Double = value.toDouble()
	
	override fun write(data: DataOutput) {
		try {
			data.writeShort(toInt())
		} catch (e: Exception) {
			data.writeShort(0)
		}
	}
	
	override fun read(data: DataInput) {
		value = try {
			data.readShort()
		} catch (e: Exception) {
			0
		}
	}
	
	fun copy() = value
	
	override fun compareTo(other: ShortTag): Int = value.compareTo(other.value)
	override fun equals(other: Any?): Boolean = other is ShortTag && value == other.value
	override fun hashCode(): Int = value.hashCode()
	override fun toString(): String = value.toString()
}
