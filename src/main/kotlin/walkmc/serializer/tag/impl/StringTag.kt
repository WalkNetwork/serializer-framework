@file:Suppress("NOTHING_TO_INLINE")

package walkmc.serializer.tag.impl

import walkmc.serializer.tag.*
import java.io.*

/**
 * A tag implementation to stores [String].
 */
open class StringTag(var value: String = "") : Tag(9), Comparable<StringTag>, Iterable<Char> {
	override fun write(data: DataOutput) {
		try {
			data.writeUTF(value)
		} catch (ex: Exception) {
			data.writeUTF("")
		}
	}
	
	override fun read(data: DataInput) {
		value = try {
			data.readUTF()
		} catch (ex: Exception) {
			""
		}
	}
	
	fun copy() = value
	
	override fun compareTo(other: StringTag): Int = value.compareTo(other.value)
	override fun toString(): String = value
	override fun hashCode(): Int = value.hashCode()
	override fun equals(other: Any?): Boolean = other is StringTag && value == other.value
	override fun iterator(): Iterator<Char> = value.iterator()
	
	companion object {
		fun of(value: String) = StringTag().apply { this.value = value }
	}
}

/**
 * Converts this string to a [StringTag].
 */
inline fun String.toTag(): StringTag = StringTag.of(this)
