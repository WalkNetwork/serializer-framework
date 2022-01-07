package walkmc.serializer.tag.impl

import walkmc.serializer.tag.*
import java.io.*

/**
 * A tag implementation to stores [Float].
 */
open class FloatTag(var value: Float = 0f) : Tag(5), NumberTag, Comparable<FloatTag> {
	override fun toByte(): Byte = value.toInt().toByte()
	override fun toShort(): Short = value.toInt().toShort()
	override fun toInt(): Int = value.toInt()
	override fun toLong(): Long = value.toLong()
	override fun toFloat(): Float = value
	override fun toDouble(): Double = value.toDouble()
	
	override fun write(data: DataOutput) {
		data.writeFloat(value)
	}
	
	override fun read(data: DataInput) {
		value = data.readFloat()
	}
	
	override fun compareTo(other: FloatTag): Int = value.compareTo(other.value)
	override fun equals(other: Any?): Boolean = other is FloatTag && value == other.value
	override fun hashCode(): Int = value.hashCode()
	override fun toString(): String = value.toString()
}
