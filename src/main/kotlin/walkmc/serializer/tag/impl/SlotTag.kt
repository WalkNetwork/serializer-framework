@file:Suppress("NOTHING_TO_INLINE")

package walkmc.serializer.tag.impl

import walkmc.*
import walkmc.extensions.*
import walkmc.serializer.tag.*
import java.io.*

/**
 * A tag implementation to stores [Slot].
 */
open class SlotTag : Tag(17) {
	lateinit var value: Slot
	
	override fun write(data: DataOutput) {
		if (::value.isInitialized) {
			data.writeSlot(value)
		}
	}
	
	override fun read(data: DataInput) {
		value = data.readSlot()
	}
	
	fun copy() = Slot(value.slot, value.item.clone())
	
	override fun toString(): String = value.toString()
	override fun equals(other: Any?): Boolean = other is SlotTag && value == other.value
	override fun hashCode(): Int = value.hashCode()
	
	companion object {
		fun of(value: Slot) = SlotTag().apply { this.value = value }
	}
}

/**
 * Converts this slot to a [SlotTag].
 */
inline fun Slot.toTag() = SlotTag.of(this)
