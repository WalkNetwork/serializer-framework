@file:Suppress("NOTHING_TO_INLINE")

package walkmc.serializer.tag.impl

import net.minecraft.server.*
import walkmc.serializer.tag.*
import java.io.*

/**
 * A tag implementation to stores [NBTTagCompound].
 * This is a only for use implementation to compatibility of minecraft NBT,
 * this is not covered to uses as a key-value map, uses [CompoundTag] instead.
 */
open class NbtCompoundTag : Tag(14) {
	lateinit var value: NBTTagCompound
	
	override fun write(data: DataOutput) {
		if (::value.isInitialized) {
			data.writeCompound(value)
		}
	}
	
	override fun read(data: DataInput) {
		data.readCompound(value)
	}
	
	override fun toString(): String = value.toString()
	override fun equals(other: Any?): Boolean = other is NbtCompoundTag && value == other.value
	override fun hashCode(): Int = value.hashCode()
	
	companion object {
		fun of(value: NBTTagCompound) = NbtCompoundTag().apply { this.value = value }
	}
}

/**
 * Converts this tag compound to a [NbtCompoundTag].
 */
inline fun NBTTagCompound.toTag() = NbtCompoundTag.of(this)

/**
 * Loads a tag compound from this data input stream.
 */
fun DataInput.readCompound(tag: NBTTagCompound = NBTTagCompound()): NBTTagCompound =
	tag.apply { load(this@readCompound, 0, NBTReadLimiter.a) }

/**
 * Writes a tag compound to this data output stream.
 */
fun DataOutput.writeCompound(tag: NBTTagCompound) = tag.write(this)

/**
 * Converts this NBT base to a [Tag] or [EmptyTag] if this nbt is a [NBTTagEnd].
 */
fun NBTBase.toTag(): Tag = when (this) {
	is NBTTagByte -> this.f().toTag()
	is NBTTagShort -> this.e().toTag()
	is NBTTagInt -> this.d().toTag()
	is NBTTagLong -> this.c().toTag()
	is NBTTagFloat -> this.h().toTag()
	is NBTTagDouble -> this.g().toTag()
	is NBTTagByteArray -> this.c().toTagList()
	is NBTTagIntArray -> this.c().toTagList()
	is NBTTagString -> this.a_().toTag()
	is NBTTagList -> this.toTagList()
	is NBTTagCompound -> this.toCompoundTag()
	else -> EmptyTag
}
