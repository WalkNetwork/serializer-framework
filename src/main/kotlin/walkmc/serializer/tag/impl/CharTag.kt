@file:Suppress("NOTHING_TO_INLINE")

package walkmc.serializer.tag.impl

import walkmc.*
import walkmc.serializer.tag.*
import java.io.*

/**
 * A tag implementation to stores [Char].
 */
open class CharTag : Tag(8), Comparable<CharTag> {
	var value: Char by notnull()
	
	override fun write(data: DataOutput) {
		data.writeChar(value.code)
	}
	
	override fun read(data: DataInput) {
		value = data.readChar()
	}
	
	override fun compareTo(other: CharTag): Int = value.compareTo(other.value)
	override fun toString(): String = value.toString()
	override fun hashCode(): Int = value.hashCode()
	override fun equals(other: Any?): Boolean = other is CharTag && value == other.value
	
	companion object {
		fun of(value: Char) = CharTag().apply { this.value = value }
	}
}

/**
 * Converts this char to a [CharTag].
 */
inline fun Char.toTag() = CharTag.of(this)
