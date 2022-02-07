package walkmc.serializer.tag.impl

import walkmc.serializer.tag.*
import java.io.*

/**
 * A tag implementation to stores [Int].
 */
open class IntTag(var value: Int = 0) : Tag(3), NumberTag, Comparable<IntTag> {
	override fun toByte(): Byte = value.toByte()
	override fun toShort(): Short = value.toShort()
	override fun toInt(): Int = value
	override fun toLong(): Long = value.toLong()
	override fun toFloat(): Float = value.toFloat()
	override fun toDouble(): Double = value.toDouble()
	
	override fun write(data: DataOutput) {
		try {
			data.writeInt(value)
		} catch (e: Exception) {
			data.writeInt(0)
		}
	}
	
	override fun read(data: DataInput) {
		value = try {
			data.readInt()
		} catch (e: Exception) {
			0
		}
	}
	
	fun copy() = value
	
	override fun compareTo(other: IntTag): Int = value.compareTo(other.value)
	override fun equals(other: Any?): Boolean = other is IntTag && value == other.value
	override fun hashCode(): Int = value.hashCode()
	override fun toString(): String = value.toString()
}
