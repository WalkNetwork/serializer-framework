package walkmc.serializer.tag.impl

import walkmc.serializer.tag.*
import java.io.*

/**
 * A tag implementation to stores [Double].
 */
open class DoubleTag(var value: Double = 0.0) : Tag(6), NumberTag, Comparable<DoubleTag> {
	override fun toByte(): Byte = value.toInt().toByte()
	override fun toShort(): Short = value.toInt().toShort()
	override fun toInt(): Int = value.toInt()
	override fun toLong(): Long = value.toLong()
	override fun toFloat(): Float = value.toFloat()
	override fun toDouble(): Double = value
	
	override fun write(data: DataOutput) {
		try {
			data.writeDouble(value)
		} catch (e: Exception) {
			data.writeDouble(0.0)
		}
	}
	
	override fun read(data: DataInput) {
		value = try {
			data.readDouble()
		} catch (e: Exception) {
			0.0
		}
	}
	
	fun copy() = value
	
	override fun compareTo(other: DoubleTag): Int = value.compareTo(other.value)
	override fun equals(other: Any?): Boolean = other is DoubleTag && value == other.value
	override fun hashCode(): Int = value.hashCode()
	override fun toString(): String = value.toString()
}
