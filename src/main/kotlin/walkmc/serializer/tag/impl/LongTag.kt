package walkmc.serializer.tag.impl

import walkmc.serializer.tag.*
import java.io.*

/**
 * A tag implementation to stores [Long].
 */
open class LongTag(var value: Long = 0) : Tag(4), NumberTag, Comparable<LongTag> {
	override fun toByte(): Byte = value.toByte()
	override fun toShort(): Short = value.toShort()
	override fun toInt(): Int = value.toInt()
	override fun toLong(): Long = value
	override fun toFloat(): Float = value.toFloat()
	override fun toDouble(): Double = value.toDouble()
	
	override fun write(data: DataOutput) {
		try {
			data.writeLong(value)
		} catch (e: Exception) {
			data.writeLong(0)
		}
	}
	
	override fun read(data: DataInput) {
		value = try {
			data.readLong()
		} catch (e: Exception) {
			0
		}
	}
	
	fun copy() = value
	
	override fun compareTo(other: LongTag): Int = value.compareTo(other.value)
	override fun equals(other: Any?): Boolean = other is LongTag && value == other.value
	override fun hashCode(): Int = value.hashCode()
	override fun toString(): String = value.toString()
}
