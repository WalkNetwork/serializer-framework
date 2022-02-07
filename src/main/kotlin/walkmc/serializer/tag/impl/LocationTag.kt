@file:Suppress("NOTHING_TO_INLINE")

package walkmc.serializer.tag.impl

import org.bukkit.*
import walkmc.extensions.*
import walkmc.serializer.tag.*
import java.io.*

/**
 * A tag implementation to stores [Location].
 */
open class LocationTag : Tag(16), Comparable<LocationTag> {
	lateinit var value: Location
	
	override fun write(data: DataOutput) {
		if (::value.isInitialized) {
			data.writeLocation(value)
		}
	}
	
	override fun read(data: DataInput) {
		value = data.readLocation()
	}
	
	fun copy() = value.clone()
	
	override fun compareTo(other: LocationTag): Int = value.compareTo(other.value)
	override fun toString(): String = value.toString()
	override fun equals(other: Any?): Boolean = other is LocationTag && value == other.value
	override fun hashCode(): Int = value.hashCode()
	
	companion object {
		fun of(value: Location) = LocationTag().apply { this.value = value }
	}
}

/**
 * Converts this Location to a [LocationTag].
 */
inline fun Location.toTag() = LocationTag.of(this)
