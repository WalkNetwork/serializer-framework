@file:Suppress("NOTHING_TO_INLINE")

package walkmc.serializer.tag

import walkmc.extensions.*
import walkmc.extensions.collections.*
import walkmc.serializer.tag.impl.*
import java.util.*

/**
 * A tag track representing all registed type of [Tag].
 */
object TagManager : HashMap<Short, RegisteredTag<Tag>>() {
	
	/**
	 * Represents the empty tag [EmptyTag].
	 */
	@JvmField
	val EMPTY = register<EmptyTag>(0)
	
	/**
	 * Represents the tag [ByteTag].
	 */
	@JvmField
	val BYTE = register<ByteTag>(1)
	
	/**
	 * Represents the tag [ShortTag].
	 */
	@JvmField
	val SHORT = register<ShortTag>(2)
	
	/**
	 * Represents the tag [IntTag].
	 */
	@JvmField
	val INT = register<IntTag>(3)
	
	/**
	 * Represents the tag [LongTag].
	 */
	@JvmField
	val LONG = register<LongTag>(4)
	
	/**
	 * Represents the tag [FloatTag].
	 */
	@JvmField
	val FLOAT = register<FloatTag>(5)
	
	/**
	 * Represents the tag [DoubleTag].
	 */
	@JvmField
	val DOUBLE = register<DoubleTag>(6)
	
	/**
	 * Represents the tag [BooleanTag].
	 */
	@JvmField
	val BOOLEAN = register<BooleanTag>(7)
	
	/**
	 * Represents the tag [CharTag].
	 */
	@JvmField
	val CHAR = register<CharTag>(8)
	
	/**
	 * Represents the tag [StringTag].
	 */
	@JvmField
	val STRING = register<StringTag>(9)
	
	/**
	 * Represents the tag [ListTag].
	 */
	@JvmField
	val LIST = register<ListTag<*>>(10)
	
	/**
	 * Represents the tag [SetTag].
	 */
	@JvmField
	val SET = register<SetTag<*>>(11)
	
	/**
	 * Represents the tag [CompoundTag].
	 */
	@JvmField
	val COMPOUND = register<CompoundTag>(12)
	
	/**
	 * Represents the tag [UuidTag].
	 */
	@JvmField
	val UUID = register<UuidTag>(13)
	
	/**
	 * Represents the tag [NbtCompoundTag].
	 */
	@JvmField
	val NBT_COMPOUND = register<NbtCompoundTag>(14)
	
	/**
	 * Represents the tag [ItemTag].
	 */
	@JvmField
	val ITEM = register<ItemTag>(15)
	
	/**
	 * Represents the tag [LocationTag].
	 */
	@JvmField
	val LOCATION = register<LocationTag>(16)
	
	/**
	 * Represents the tag [SlotTag].
	 */
	@JvmField
	val SLOT = register<SlotTag>(17)
	
	/**
	 * Represents the tag [ClassTag].
	 */
	@JvmField
	val CLASS = register<ClassTag>(18)
	
	/**
	 * Represents the tag [InventoryTag].
	 */
	@JvmField
	val INVENTORY = register<InventoryTag>(19)
	
	/**
	 * Represents the block tag [BlockTag]
	 */
	@JvmField
	val BLOCK = register<BlockTag>(20)
	
	/**
	 * Register a new tag to the track. If the specified [id] already exists, do nothing.
	 */
	@JvmStatic
	inline fun <reified T : Tag> register(): RegisteredTag<T> {
		val id = nextFreeId()
		val tag = RegisteredTag(id, T::class)
		putIfAbsent(id, tag.cast())
		return tag
	}
	
	/**
	 * Returns the next free id, can be useful if you is implemeting your custom tag.
	 */
	@JvmStatic
	fun nextFreeId(): Short {
		val next = keys.last() + 1
		if (next > Short.MAX_VALUE)
			error("No more free id has found.")
		return next.toShort()
	}
	
	/**
	 * Register a new tag to the track. If the specified [id] already exists, do nothing.
	 */
	private inline fun <reified T : Tag> register(id: Short): RegisteredTag<T> {
		val tag = RegisteredTag(id, T::class)
		putIfAbsent(id, tag.cast())
		return tag
	}
}

/**
 * Creates a tag by the specified [id], or throws a exception if the id is not registered in [TagManager].
 */
fun createTag(id: Short): Tag {
	return TagManager[id]!!.create()
}

/**
 * Creates a tag by the specified [id], or null if the id is not registered in [TagManager].
 */
fun createTagOrNull(id: Short): Tag? {
	return TagManager[id]?.create()
}

/**
 * Creates a tag by the specified [id] and casts them to [T],
 * or throws a exception if the id is not registered in [TagManager].
 */
@JvmName("createTagBy")
fun <T : Tag> createTag(id: Short): T = createTag(id).cast()

/**
 * Creates a tag by the specified [id] and casts them to [T],
 * or null if the id is not registered in [TagManager].
 */
@JvmName("createTagOrNullBy")
fun <T : Tag> createTagOrNull(id: Short): T? = createTagOrNull(id)?.cast()
