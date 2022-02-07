@file:Suppress("NOTHING_TO_INLINE")

package walkmc.serializer.tag.impl

import walkmc.serializer.tag.*
import java.io.*

/**
 * A tag implementation to stores [Boolean].
 */
open class BooleanTag(var value: Boolean = false) : Tag(7), Comparable<BooleanTag> {
	override fun write(data: DataOutput) {
		try {
			data.writeBoolean(value)
		} catch (e: Exception) {
			data.writeBoolean(false)
		}
	}
	
	override fun read(data: DataInput) {
		value = try {
			data.readBoolean()
		} catch (e: Exception) {
			false
		}
	}
	
	fun copy() = value
	
	override fun compareTo(other: BooleanTag): Int = value.compareTo(other.value)
	override fun toString(): String = value.toString()
	override fun hashCode(): Int = value.hashCode()
	override fun equals(other: Any?): Boolean = other is BooleanTag && value == other.value
	
	companion object {
		fun of(value: Boolean) = BooleanTag().apply { this.value = value }
	}
}

/**
 * Converts this boolean to a [BooleanTag].
 */
inline fun Boolean.toTag() = BooleanTag.of(this)
