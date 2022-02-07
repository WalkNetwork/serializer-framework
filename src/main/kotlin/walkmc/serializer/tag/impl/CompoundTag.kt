@file:Suppress("NOTHING_TO_INLINE")

package walkmc.serializer.tag.impl

import net.minecraft.server.*
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.inventory.*
import org.bukkit.inventory.ItemStack
import walkmc.Slot
import walkmc.extensions.*
import walkmc.serializer.tag.*
import kotlin.contracts.*
import kotlin.reflect.*
import java.io.*
import java.util.*

/**
 * A tag implementation to stores [MutableMap].
 */
open class CompoundTag(
	val delegate: MutableMap<String, Tag> = LinkedHashMap()
) : Tag(12), MutableMap<String, Tag> by delegate {
	
	/**
	 * Constructs a tag group from a nbt tag compound.
	 */
	constructor(tag: NBTTagCompound) : this(tag.toCompoundTag())
	
	override fun write(data: DataOutput) {
		for ((key, value) in this) {
			data.writeShort(value.id.toInt()) // id of the tag value
			data.writeUTF(key) // key of the tag value
			value.write(data)
		}
		data.writeShort(-1) // end of the group.
	}
	
	override fun read(data: DataInput) {
		clear()
		
		while (true) {
			when (val id = data.readShort()) {
				(-1).toShort() -> break
				else -> {
					val key = data.readUTF()
					val tag = createTagOrNull(id) ?: error("No tag type found with id $id")
					tag.read(data)
					put(key, tag)
				}
			}
		}
	}
	
	/**
	 * Puts the specified key and value to this group and returns the newly added value.
	 */
	override fun put(key: String, value: Tag): Tag? {
		this.delegate[key] = value
		return value
	}
	
	fun copy() = LinkedHashMap(delegate)
	
	fun <T : Tag> by(key: String): T = get(key)!!.cast()
	fun <T : Tag> byOrNull(key: String): T? = get(key)?.cast()
	fun byteOrNull(key: String) = getByOrNull<ByteTag>(key)?.value
	fun byte(key: String, default: Byte = 0) = getByteOrNull(key) ?: default
	fun shortOrNull(key: String) = getByOrNull<ShortTag>(key)?.value
	fun short(key: String, default: Short = 0) = getShortOrNull(key) ?: default
	fun intOrNull(key: String) = getByOrNull<IntTag>(key)?.value
	fun int(key: String, default: Int = 0) = getIntOrNull(key) ?: default
	fun longOrNull(key: String) = getByOrNull<LongTag>(key)?.value
	fun long(key: String, default: Long = 0) = getLongOrNull(key) ?: default
	fun floatOrNull(key: String) = getByOrNull<FloatTag>(key)?.value
	fun float(key: String, default: Float = 0f) = getFloatOrNull(key) ?: default
	fun doubleOrNull(key: String) = getByOrNull<DoubleTag>(key)?.value
	fun double(key: String, default: Double = 0.0) = getDoubleOrNull(key) ?: default
	fun booleanOrNull(key: String) = getByOrNull<BooleanTag>(key)?.value
	fun boolean(key: String, default: Boolean = false) = getBooleanOrNull(key) ?: default
	fun charOrNull(key: String) = getByOrNull<CharTag>(key)?.value
	fun char(key: String, default: Char = ' ') = getCharOrNull(key) ?: default
	fun stringOrNull(key: String) = getByOrNull<StringTag>(key)?.value
	fun string(key: String, default: String = "") = getStringOrNull(key) ?: default
	fun <T : Tag> listOrNull(key: String) = getByOrNull<ListTag<T>>(key)?.value
	fun <T : Tag> list(key: String) = getListOrNull<T>(key) ?: ArrayList()
	fun <T : Tag> setOrNull(key: String) = getByOrNull<SetTag<T>>(key)?.value
	fun <T : Tag> set(key: String) = getSetOrNull<T>(key) ?: HashSet()
	fun compoundOrNull(key: String) = getByOrNull<CompoundTag>(key)
	fun compound(key: String, default: CompoundTag = CompoundTag()) = getCompoundOrNull(key) ?: default
	fun uuidOrNull(key: String) = getByOrNull<UuidTag>(key)?.value
	fun uuid(key: String, default: UUID? = null) = getUuidOrNull(key) ?: default ?: error("No uuid tag value found with key $key")
	fun nbtCompoundOrNull(key: String) = getByOrNull<NbtCompoundTag>(key)?.value
	fun nbtCompound(key: String, default: NBTTagCompound = NBTTagCompound()) = getNbtCompoundOrNull(key) ?: default
	fun itemOrNull(key: String) = getByOrNull<ItemTag>(key)?.value
	fun item(key: String, default: ItemStack? = null) = getItemOrNull(key) ?: default ?: error("No item tag value found with key $key")
	fun classOrNull(key: String) = getByOrNull<ClassTag>(key)?.value
	fun slotOrNull(key: String) = getByOrNull<SlotTag>(key)?.value
	fun slot(key: String, default: Slot? = null) = getSlotOrNull(key) ?: default ?: error("No class tag value found with key $key")
	inline fun <reified T : Enum<T>> enumOrNull(key: String) = runCatching { enumValueOf<T>(getString(key)) }.getOrNull()
	inline fun <reified T : Enum<T>> enum(key: String, default: T? = null) = getEnumOrNull(key) ?: default ?: error("No class tag value found with key $key")
	fun inventoryOrNull(key: String) = getByOrNull<InventoryTag>(key)?.value
	fun inventory(key: String, default: Inventory? = null) = getInventoryOrNull(key) ?: default ?: error("No class tag value found with key $key")
	fun blockOrNull(key: String) = getByOrNull<BlockTag>(key)?.value
	fun block(key: String, default: Block? = null) = getBlockOrNull(key) ?: default ?: error("No block tag value found with key $key")
	fun locationOrNull(key: String) = getByOrNull<LocationTag>(key)?.value
	fun location(key: String, default: Location? = null) = getLocationOrNull(key) ?: default ?: error("No location tag value found with key $key")
	
	override fun equals(other: Any?) = other is CompoundTag && delegate == other.delegate
	override fun toString() = delegate.toString()
	override fun hashCode() = delegate.hashCode()
}

/**
 * Builds a tag group by the specified [builder] function block.
 */
inline fun buildCompoundTag(builder: CompoundTag.() -> Unit): CompoundTag {
	contract {
		callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
	}
	
	return CompoundTag().apply(builder)
}

/**
 * Creates a new tag group with the specified elements.
 */
fun tagCompoundOf(vararg elements: Pair<String, Tag>) = CompoundTag(elements.toMap(HashMap()))

/**
 * Converts this map to a [CompoundTag].
 */
fun Map<String, Tag>.toCompoundTag() = CompoundTag(toMutableMap())

/**
 * Converts this [NBTTagCompound] to a tag group.
 */
fun NBTTagCompound.toCompoundTag(): CompoundTag {
	val result = all().mapValues { it.value.toTag() }
	return result.toCompoundTag()
}

/**
 * Returns if this tag group contains the specified key and is the type [T].
 */
fun <T : Tag> CompoundTag.hasKey(key: String): Boolean = getByOrNull<T>(key) != null

/**
 * Gets a value by the specified [key] and casts them to generic [T],
 * or throws a exception if the group not contains the specified key.
 */
fun <T : Tag> CompoundTag.getBy(key: String): T = get(key)!!.cast()

/**
 * Gets a value by the specified [key] and casts them to generic [T],
 * or nulls if the group not contains the specified key.
 */
fun <T : Tag> CompoundTag.getByOrNull(key: String): T? = get(key)?.cast()

/**
 * Gets a byte value by the specified [key], or returns nulls if the group not contains the specified key.
 */
fun CompoundTag.getByteOrNull(key: String) = getByOrNull<ByteTag>(key)?.value

/**
 * Gets a byte value by the specified [key], or 0 if the group not contains the specified key.
 */
fun CompoundTag.getByte(key: String, default: Byte = 0) = getByteOrNull(key) ?: default

/**
 * Gets a short value by the specified [key], or returns nulls if the group not contains the specified key.
 */
fun CompoundTag.getShortOrNull(key: String) = getByOrNull<ShortTag>(key)?.value

/**
 * Gets a short value by the specified [key], or 0 if the group not contains the specified key.
 */
fun CompoundTag.getShort(key: String, default: Short = 0) = getShortOrNull(key) ?: default

/**
 * Gets a int value by the specified [key], or returns nulls if the group not contains the specified key.
 */
fun CompoundTag.getIntOrNull(key: String) = getByOrNull<IntTag>(key)?.value

/**
 * Gets a int value by the specified [key], or 0 if the group not contains the specified key.
 */
fun CompoundTag.getInt(key: String, default: Int = 0) = getIntOrNull(key) ?: default

/**
 * Gets a long value by the specified [key], or returns nulls if the group not contains the specified key.
 */
fun CompoundTag.getLongOrNull(key: String) = getByOrNull<LongTag>(key)?.value

/**
 * Gets a long value by the specified [key], or 0 if the group not contains the specified key.
 */
fun CompoundTag.getLong(key: String, default: Long = 0) = getLongOrNull(key) ?: default

/**
 * Gets a float value by the specified [key], or returns nulls if the group not contains the specified key.
 */
fun CompoundTag.getFloatOrNull(key: String) = getByOrNull<FloatTag>(key)?.value

/**
 * Gets a float value by the specified [key], or 0 if the group not contains the specified key.
 */
fun CompoundTag.getFloat(key: String, default: Float = 0f) = getFloatOrNull(key) ?: default

/**
 * Gets a double value by the specified [key], or returns nulls if the group not contains the specified key.
 */
fun CompoundTag.getDoubleOrNull(key: String) = getByOrNull<DoubleTag>(key)?.value

/**
 * Gets a double value by the specified [key], or 0 if the group not contains the specified key.
 */
fun CompoundTag.getDouble(key: String, default: Double = 0.0) = getDoubleOrNull(key) ?: default

/**
 * Gets a boolean value by the specified [key], or returns nulls if the group not contains the specified key.
 */
fun CompoundTag.getBooleanOrNull(key: String) = getByOrNull<BooleanTag>(key)?.value

/**
 * Gets a boolean value by the specified [key], or false if the group not contains the specified key.
 */
fun CompoundTag.getBoolean(key: String, default: Boolean = false) = getBooleanOrNull(key) ?: default

/**
 * Gets a char value by the specified [key], or returns nulls if the group not contains the specified key.
 */
fun CompoundTag.getCharOrNull(key: String) = getByOrNull<CharTag>(key)?.value

/**
 * Gets a char value by the specified [key], or throws a exception
 * if the group not contains the specified key.
 */
fun CompoundTag.getChar(key: String, default: Char = ' ') = getCharOrNull(key) ?: default

/**
 * Gets a string value by the specified [key], or returns nulls if the group not contains the specified key.
 */
fun CompoundTag.getStringOrNull(key: String) = getByOrNull<StringTag>(key)?.value

/**
 * Gets a string value by the specified [key], or a empty string if the group not contains the specified key.
 */
fun CompoundTag.getString(key: String, default: String = "") = getStringOrNull(key) ?: default

/**
 * Gets a tag list value by the specified [key], or returns nulls if the group not contains the specified key.
 */
fun <T : Tag> CompoundTag.getListOrNull(key: String) = getByOrNull<ListTag<T>>(key)?.value

/**
 * Gets a tag list value by the specified [key], or a empty list
 * if the group not contains the specified key.
 */
fun <T : Tag> CompoundTag.getList(key: String) = getListOrNull<T>(key) ?: ArrayList()

/**
 * Gets a tag set value by the specified [key], or returns nulls if the group not contains the specified key.
 */
fun <T : Tag> CompoundTag.getSetOrNull(key: String) = getByOrNull<SetTag<T>>(key)?.value

/**
 * Gets a tag set value by the specified [key], or a empty set
 * if the group not contains the specified key.
 */
fun <T : Tag> CompoundTag.getSet(key: String) = getSetOrNull<T>(key) ?: HashSet()

/**
 * Gets a tag group value by the specified [key], or returns nulls if the group not contains the specified key.
 */
fun CompoundTag.getCompoundOrNull(key: String) = getByOrNull<CompoundTag>(key)

/**
 * Gets a tag group value by the specified [key], or a empty tag group
 * if the group not contains the specified key.
 */
fun CompoundTag.getCompound(key: String, default: CompoundTag = CompoundTag()) =
	getCompoundOrNull(key) ?: default

/**
 * Gets a uuid value by the specified [key], or returns nulls if the group not contains the specified key.
 */
fun CompoundTag.getUuidOrNull(key: String) = getByOrNull<UuidTag>(key)?.value

/**
 * Gets a uuid value by the specified [key], or throws a exception
 * if the group not contains the specified key.
 */
fun CompoundTag.getUuid(key: String, default: UUID? = null) =
	getUuidOrNull(key) ?: default ?: error("No uuid tag value found with key $key")

/**
 * Gets a uuid value by the specified [key], or returns nulls if the group not contains the specified key.
 */
fun CompoundTag.getUUIDOrNull(key: String) = getUuidOrNull(key)

/**
 * Gets a uuid value by the specified [key], or throws a exception
 * if the group not contains the specified key.
 */
fun CompoundTag.getUUID(key: String, default: UUID? = null) = getUuid(key, default)

/**
 * Gets a compound value by the specified [key], or returns nulls if the group not contains the specified key.
 */
fun CompoundTag.getNbtCompoundOrNull(key: String) = getByOrNull<NbtCompoundTag>(key)?.value

/**
 * Gets a compound value by the specified [key], or a empty compound
 * if the group not contains the specified key.
 */
fun CompoundTag.getNbtCompound(key: String, default: NBTTagCompound = NBTTagCompound()) =
	getNbtCompoundOrNull(key) ?: default

/**
 * Gets a item value by the specified [key], or returns nulls if the group not contains the specified key.
 */
fun CompoundTag.getItemOrNull(key: String) = getByOrNull<ItemTag>(key)?.value

/**
 * Gets a item value by the specified [key], or throws a exception
 * if the group not contains the specified key.
 */
fun CompoundTag.getItem(key: String, default: ItemStack? = null) =
	getItemOrNull(key) ?: default ?: error("No item tag value found with key $key")

/**
 * Gets a class value by the specified [key], or returns nulls if the group not contains the specified key.
 */
fun CompoundTag.getClassOrNull(key: String) = getByOrNull<ClassTag>(key)?.value

/**
 * Gets a class value by the specified [key], or throws a exception
 * if the group not contains the specified key.
 */
fun CompoundTag.getClass(key: String, default: KClass<*>? = null) =
	getClassOrNull(key) ?: default ?: error("No class tag value found with key $key")

/**
 * Gets a slot value by the specified [key], or returns nulls if the group not contains the specified key.
 */
fun CompoundTag.getSlotOrNull(key: String) = getByOrNull<SlotTag>(key)?.value

/**
 * Gets a slot value by the specified [key], or throws a exception
 * if the group not contains the specified key.
 */
fun CompoundTag.getSlot(key: String, default: Slot? = null) =
	getSlotOrNull(key) ?: default ?: error("No class tag value found with key $key")

/**
 * Gets a enum value by the specified [key], or returns nulls if the group not contains the specified key.
 */
inline fun <reified T : Enum<T>> CompoundTag.getEnumOrNull(key: String) =
	runCatching { enumValueOf<T>(getString(key)) }.getOrNull()

/**
 * Gets a enum value by the specified [key], or throws a exception
 * if the group not contains the specified key.
 */
inline fun <reified T : Enum<T>> CompoundTag.getEnum(key: String, default: T? = null) =
	getEnumOrNull(key) ?: default ?: error("No class tag value found with key $key")

/**
 * Gets a inventory value by the specified [key], or returns nulls if the group not contains the specified key.
 */
fun CompoundTag.getInventoryOrNull(key: String) = getByOrNull<InventoryTag>(key)?.value

/**
 * Gets a inventory value by the specified [key], or throws a exception
 * if the group not contains the specified key.
 */
fun CompoundTag.getInventory(key: String, default: Inventory? = null) =
	getInventoryOrNull(key) ?: default ?: error("No class tag value found with key $key")

/**
 * Gets a block value by the specified [key], or returns nulls if the group not contains the specified key.
 */
fun CompoundTag.getBlockOrNull(key: String) = getByOrNull<BlockTag>(key)?.value

/**
 * Gets a block value by the specified [key], or throws a exception
 * if the group not contains the specified key.
 */
fun CompoundTag.getBlock(key: String, default: Block? = null) =
	getBlockOrNull(key) ?: default ?: error("No block tag value found with key $key")

/**
 * Gets a location value by the specified [key], or returns nulls if the group not contains the specified key.
 */
fun CompoundTag.getLocationOrNull(key: String) = getByOrNull<LocationTag>(key)?.value

/**
 * Gets a location value by the specified [key], or throws a exception
 * if the group not contains the specified key.
 */
fun CompoundTag.getLocation(key: String, default: Location? = null) =
	getLocationOrNull(key) ?: default ?: error("No location tag value found with key $key")

/**
 * Puts the specified byte value to this group with the specified [key].
 */
inline fun CompoundTag.put(key: String, value: Byte) = put(key, value.toTag())

/**
 * Puts the specified short value to this group with the specified [key].
 */
inline fun CompoundTag.put(key: String, value: Short) = put(key, value.toTag())

/**
 * Puts the specified int value to this group with the specified [key].
 */
inline fun CompoundTag.put(key: String, value: Int) = put(key, value.toTag())

/**
 * Puts the specified long value to this group with the specified [key].
 */
inline fun CompoundTag.put(key: String, value: Long) = put(key, value.toTag())

/**
 * Puts the specified float value to this group with the specified [key].
 */
inline fun CompoundTag.put(key: String, value: Float) = put(key, value.toTag())

/**
 * Puts the specified double value to this group with the specified [key].
 */
inline fun CompoundTag.put(key: String, value: Double) = put(key, value.toTag())

/**
 * Puts the specified boolean value to this group with the specified [key].
 */
inline fun CompoundTag.put(key: String, value: Boolean) = put(key, value.toTag())

/**
 * Puts the specified char value to this group with the specified [key].
 */
inline fun CompoundTag.put(key: String, value: Char) = put(key, value.toTag())

/**
 * Puts the specified uuid value to this group with the specified [key].
 */
inline fun CompoundTag.put(key: String, value: UUID) = put(key, value.toTag())

/**
 * Puts the specified string value to this group with the specified [key].
 */
inline fun CompoundTag.put(key: String, value: String) = put(key, value.toTag())

/**
 * Puts the specified compound value to this group with the specified [key].
 */
inline fun CompoundTag.put(key: String, value: NBTTagCompound) = put(key, value.toTag())

/**
 * Puts the specified item value to this group with the specified [key].
 */
inline fun CompoundTag.put(key: String, value: ItemStack) = put(key, value.toTag())

/**
 * Puts the specified slot value to this group with the specified [key].
 */
inline fun CompoundTag.put(key: String, value: Slot) = put(key, value.toTag())

/**
 * Puts the specified enum value to this group with the specified [key].
 */
inline fun CompoundTag.put(key: String, value: Enum<*>) = put(key, value.name.toTag())

/**
 * Puts the specified kotlin class value to this group with the specified [key].
 */
inline fun CompoundTag.put(key: String, value: KClass<*>) = put(key, value.toTag())

/**
 * Puts the specified location value to this group with the specified [key].
 */
inline fun CompoundTag.put(key: String, value: Location) = put(key, value.toTag())

/**
 * Puts the specified java class value to this group with the specified [key].
 */
inline fun CompoundTag.put(key: String, value: Class<*>) = put(key, value.toTag())

/**
 * Puts the specified inventory value to this group with the specified [key].
 */
inline fun CompoundTag.put(key: String, value: Inventory) = put(key, value.toTag())

/**
 * Puts the specified [key] and [value] in this compound tag or removes
 * a possible existent value if the [value] is false.
 */
fun CompoundTag.setBooleanOrRemove(key: String, value: Boolean) {
	if (!value) remove(key) else put(key, true)
}

inline operator fun CompoundTag.set(key: String, value: Enum<*>) {
	put(key, value.name.toTag())
}

/**
 * Puts the specified inventory value to this group with the specified [key].
 */
inline operator fun CompoundTag.set(key: String, value: Inventory) {
	put(key, value.toTag())
}

/**
 * Puts the specified compound value to this group with the specified [key].
 */
inline operator fun CompoundTag.set(key: String, value: NBTTagCompound) {
	put(key, value.toTag())
}

/**
 * Puts the specified item value to this group with the specified [key].
 */
inline operator fun CompoundTag.set(key: String, value: ItemStack) {
	put(key, value.toTag())
}

/**
 * Puts the specified slot value to this group with the specified [key].
 */
inline operator fun CompoundTag.set(key: String, value: Slot) {
	put(key, value.toTag())
}

/**
 * Puts the specified location value to this group with the specified [key].
 */
inline operator fun CompoundTag.set(key: String, value: Location) {
	put(key, value.toTag())
}

/**
 * Puts the specified kotlin class value to this group with the specified [key].
 */
inline operator fun CompoundTag.set(key: String, value: KClass<*>) {
	put(key, value.toTag())
}

/**
 * Puts the specified java class value to this group with the specified [key].
 */
inline operator fun CompoundTag.set(key: String, value: Class<*>) {
	put(key, value.toTag())
}

/**
 * Puts the specified byte value to this group with the specified [key].
 */
inline operator fun CompoundTag.set(key: String, value: Byte) {
	put(key, value.toTag())
}

/**
 * Puts the specified short value to this group with the specified [key].
 */
inline operator fun CompoundTag.set(key: String, value: Short) {
	put(key, value.toTag())
}

/**
 * Puts the specified int value to this group with the specified [key].
 */
inline operator fun CompoundTag.set(key: String, value: Int) {
	put(key, value.toTag())
}

/**
 * Puts the specified long value to this group with the specified [key].
 */
inline operator fun CompoundTag.set(key: String, value: Long) {
	put(key, value.toTag())
}

/**
 * Puts the specified float value to this group with the specified [key].
 */
inline operator fun CompoundTag.set(key: String, value: Float) {
	put(key, value.toTag())
}

/**
 * Puts the specified double value to this group with the specified [key].
 */
inline operator fun CompoundTag.set(key: String, value: Double) {
	put(key, value.toTag())
}

/**
 * Puts the specified boolean value to this group with the specified [key].
 */
inline operator fun CompoundTag.set(key: String, value: Boolean) {
	put(key, value.toTag())
}

/**
 * Puts the specified char value to this group with the specified [key].
 */
inline operator fun CompoundTag.set(key: String, value: Char) {
	put(key, value.toTag())
}

/**
 * Puts the specified uuid value to this group with the specified [key].
 */
inline operator fun CompoundTag.set(key: String, value: UUID) {
	put(key, value.toTag())
}

/**
 * Puts the specified string value to this group with the specified [key].
 */
inline operator fun CompoundTag.set(key: String, value: String) {
	put(key, value.toTag())
}

/**
 * Puts all entries of the specified [group] to this group.
 */
inline fun CompoundTag.allFrom(group: CompoundTag) = putAll(group)
