@file:Suppress("NOTHING_TO_INLINE")

package walkmc.serializer.tag.impl

import walkmc.extensions.*
import walkmc.serializer.tag.*
import java.io.*
import java.util.*

typealias UUIDTag = UuidTag

/**
 * A tag implementation to stores [UUID].
 */
open class UuidTag : Tag(13), Comparable<UuidTag> {
	lateinit var value: UUID
	
	override fun write(data: DataOutput) {
		if (::value.isInitialized) {
			data.writeUUID(value)
		}
	}
	
	override fun read(data: DataInput) {
		value = data.readUUID()
	}
	
	override fun compareTo(other: UuidTag): Int = value.compareTo(other.value)
	override fun toString(): String = value.toString()
	override fun equals(other: Any?): Boolean = other is UuidTag && value == other.value
	override fun hashCode(): Int = value.hashCode()
	
	companion object {
		fun of(value: UUID) = UuidTag().apply { this.value = value }
	}
}

/**
 * Converts this UUID to a [UuidTag].
 */
inline fun UUID.toTag() = UuidTag.of(this)
