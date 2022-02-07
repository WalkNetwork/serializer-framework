@file:Suppress("NOTHING_TO_INLINE")

package walkmc.serializer.tag.impl

import net.minecraft.server.*
import org.bukkit.craftbukkit.inventory.*
import org.bukkit.inventory.ItemStack
import walkmc.serializer.tag.*
import java.io.*

/**
 * A tag implementation to stores [ItemStack].
 */
open class ItemTag : Tag(15) {
	lateinit var value: ItemStack
	
	override fun write(data: DataOutput) {
		if (::value.isInitialized) {
			data.writeCompound(value.saveTo())
		}
	}
	
	override fun read(data: DataInput) {
		value = data.readCompound().createItem()
	}
	
	fun copy() = value.clone()
	
	override fun toString(): String = value.toString()
	override fun equals(other: Any?): Boolean = other is ItemTag && value == other.value
	override fun hashCode(): Int = value.hashCode()
	
	companion object {
		fun of(value: ItemStack) = ItemTag().apply { this.value = value }
	}
}

/**
 * Converts this item stack to a [ItemTag].
 */
inline fun ItemStack.toTag() = ItemTag.of(this)

/**
 * Saves this item stack to the specified [tag] compound.
 */
fun ItemStack.saveTo(tag: NBTTagCompound = NBTTagCompound()): NBTTagCompound {
	handler.writeToNBT(tag)
	return tag
}

/**
 * Creates a item stack from this tag compound.
 */
fun NBTTagCompound.createItem(): ItemStack =
	CraftItemStack.asBukkitCopy(net.minecraft.server.ItemStack.createStack(this))
