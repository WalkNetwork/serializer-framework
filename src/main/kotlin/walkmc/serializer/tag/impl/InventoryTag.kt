package walkmc.serializer.tag.impl

import org.bukkit.inventory.*
import walkmc.extensions.*
import walkmc.serializer.tag.*
import java.io.*

/**
 * A tag implementation to stores [Inventory].
 */
open class InventoryTag : Tag(19), Iterable<ItemStack> {
	lateinit var value: Inventory
	
	override fun write(data: DataOutput) {
		if (::value.isInitialized) {
			data.writeInventory(value)
		}
	}
	
	override fun read(data: DataInput) {
		value = data.readInventory()
	}
	
	fun copy() = inventory(value.title, value.size, value.holder).apply {
		value.forEachIndexed { slot, item -> setItem(slot, item) }
	}
	
	override fun toString(): String = value.toString()
	override fun hashCode(): Int = value.hashCode()
	override fun equals(other: Any?): Boolean = other is InventoryTag && value == other.value
	override fun iterator(): Iterator<ItemStack> = value.iterator()
	
	companion object {
		fun of(value: Inventory) = InventoryTag().apply { this.value = value }
	}
}

/**
 * Converts this inventory to a [InventoryTag].
 */
fun Inventory.toTag() = InventoryTag.of(this)
