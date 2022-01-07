@file:Suppress("NOTHING_TO_INLINE")

package walkmc.serializer.tag.impl

import net.minecraft.server.*
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.inventory.*
import org.bukkit.inventory.ItemStack
import walkmc.Slot
import walkmc.extensions.*
import walkmc.extensions.collections.*
import walkmc.serializer.tag.*
import kotlin.contracts.*
import java.io.*
import java.util.*

/**
 * A tag implementation to stores [MutableList].
 */
open class ListTag<T : Tag>(
	val value: MutableList<T> = newMutableList()
) : Tag(10), MutableList<T> by value {
	
	/**
	 * The type id of this tag list generic type.
	 */
	var typeId: Short = 0
	
	override fun write(data: DataOutput) {
		if (isEmpty())
			return
		
		typeId = get(0).id
		data.writeShort(typeId.toInt())
		data.writeInt(size)
		for (tag in this)
			tag.write(data)
		
	}
	
	override fun read(data: DataInput) {
		typeId = data.readShort()
		val size = data.readInt()
		for (i in 0 until size) {
			val tag = createTag(typeId).cast<T>()
			tag.read(data)
			add(tag)
		}
	}
	
	override fun toString(): String = value.toString()
	override fun equals(other: Any?): Boolean = other is ListTag<*> && value == other.value
	override fun hashCode(): Int = value.hashCode()
}

/**
 * Builds a tag list by the specified [builder] function block.
 */
inline fun <T : Tag> buildTagList(builder: ListTag<T>.() -> Unit): ListTag<T> {
	contract {
		callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
	}
	
	return ListTag<T>().apply(builder)
}

/**
 * Creates a new tag list with the specified elements.
 */
fun <T : Tag> tagListOf(vararg elements: T) = ListTag(elements.toMutableList())

/**
 * Adds a string value to this string tag list.
 */
inline fun ListTag<StringTag>.add(value: String) = add(value.toTag())

/**
 * Adds a byte value to this byte tag list.
 */
inline fun ListTag<ByteTag>.add(value: Byte) = add(value.toTag())

/**
 * Adds a short value to this short tag list.
 */
inline fun ListTag<ShortTag>.add(value: Short) = add(value.toTag())

/**
 * Adds a int value to this int tag list.
 */
inline fun ListTag<IntTag>.add(value: Int) = add(value.toTag())

/**
 * Adds a long value to this long tag list.
 */
inline fun ListTag<LongTag>.add(value: Long) = add(value.toTag())

/**
 * Adds a float value to this float tag list.
 */
inline fun ListTag<FloatTag>.add(value: Float) = add(value.toTag())

/**
 * Adds a double value to this double tag list.
 */
inline fun ListTag<DoubleTag>.add(value: Double) = add(value.toTag())

/**
 * Adds a boolean value to this boolean tag list.
 */
inline fun ListTag<BooleanTag>.add(value: Boolean) = add(value.toTag())

/**
 * Adds a char value to this char tag list.
 */
inline fun ListTag<CharTag>.add(value: Char) = add(value.toTag())

@JvmName("toTagListSlot")
fun Iterable<Slot>.toTagList() = ListTag(map(Slot::toTag).toMutableList())

@JvmName("toTagListItemStack")
fun Iterable<ItemStack>.toTagList() = ListTag(map(ItemStack::toTag).toMutableList())

@JvmName("toTagListUUID")
fun Iterable<UUID>.toTagList() = ListTag(map(UUID::toTag).toMutableList())

@JvmName("toTagListLocation")
fun Iterable<Location>.toTagList() = ListTag(map(Location::toTag).toMutableList())

@JvmName("toTagListBlock")
fun Iterable<Block>.toTagList() = ListTag(map(Block::toTag).toMutableList())

@JvmName("toTagListInventory")
fun Iterable<Inventory>.toTagList() = ListTag(map(Inventory::toTag).toMutableList())

/**
 * Converts this array to a [ListTag].
 */
fun Array<Tag>.toTagList() = ListTag(toMutableList())

/**
 * Converts this iterable to a [ListTag].
 */
fun Iterable<Tag>.toTagList() = ListTag(toMutableList())

/**
 * Converts this nbt list to a [ListTag].
 */
fun NBTTagList.toTagList() = ListTag(content().map(NBTBase::toTag).toMutableList())

/**
 * Converts this iterable to a [ListTag].
 */
@JvmName("toTagListByte")
fun Iterable<Byte>.toTagList() = ListTag(map(Byte::toTag).toMutableList())

/**
 * Converts this iterable to a [ListTag].
 */
@JvmName("toTagListShort")
fun Iterable<Short>.toTagList() = ListTag(map(Short::toTag).toMutableList())

/**
 * Converts this iterable to a [ListTag].
 */
@JvmName("toTagListInt")
fun Iterable<Int>.toTagList() = ListTag(map(Int::toTag).toMutableList())

/**
 * Converts this iterable to a [ListTag].
 */
@JvmName("toTagListLong")
fun Iterable<Long>.toTagList() = ListTag(map(Long::toTag).toMutableList())

/**
 * Converts this iterable to a [ListTag].
 */
@JvmName("toTagListFloat")
fun Iterable<Float>.toTagList() = ListTag(map(Float::toTag).toMutableList())

/**
 * Converts this iterable to a [ListTag].
 */
@JvmName("toTagListDouble")
fun Iterable<Double>.toTagList() = ListTag(map(Double::toTag).toMutableList())

/**
 * Converts this iterable to a [ListTag].
 */
@JvmName("toTagListBoolean")
fun Iterable<Boolean>.toTagList() = ListTag(map(Boolean::toTag).toMutableList())

/**
 * Converts this iterable to a [ListTag].
 */
@JvmName("toTagListChar")
fun Iterable<Char>.toTagList() = ListTag(map(Char::toTag).toMutableList())

/**
 * Converts this iterable to a [ListTag].
 */
@JvmName("toTagListString")
fun Iterable<String>.toTagList() = ListTag(map(String::toTag).toMutableList())

/**
 * Converts this array to a [ListTag].
 */
fun Array<Byte>.toTagList() = ListTag(map(Byte::toTag).toMutableList())

/**
 * Converts this array to a [ListTag].
 */
fun ByteArray.toTagList() = ListTag(map(Byte::toTag).toMutableList())

/**
 * Converts this array to a [ListTag].
 */
@JvmName("toTagListShort")
fun Array<Short>.toTagList() = ListTag(map(Short::toTag).toMutableList())

/**
 * Converts this array to a [ListTag].
 */
@JvmName("toTagListShort")
fun ShortArray.toTagList() = ListTag(map(Short::toTag).toMutableList())

/**
 * Converts this array to a [ListTag].
 */
@JvmName("toTagListInt")
fun Array<Int>.toTagList() = ListTag(map(Int::toTag).toMutableList())

/**
 * Converts this array to a [ListTag].
 */
@JvmName("toTagListInt")
fun IntArray.toTagList() = ListTag(map(Int::toTag).toMutableList())

/**
 * Converts this array to a [ListTag].
 */
@JvmName("toTagListLong")
fun Array<Long>.toTagList() = ListTag(map(Long::toTag).toMutableList())

/**
 * Converts this array to a [ListTag].
 */
@JvmName("toTagListLong")
fun LongArray.toTagList() = ListTag(map(Long::toTag).toMutableList())

/**
 * Converts this array to a [ListTag].
 */
@JvmName("toTagListFloat")
fun Array<Float>.toTagList() = ListTag(map(Float::toTag).toMutableList())

/**
 * Converts this array to a [ListTag].
 */
@JvmName("toTagListFloat")
fun FloatArray.toTagList() = ListTag(map(Float::toTag).toMutableList())

/**
 * Converts this array to a [ListTag].
 */
@JvmName("toTagListDouble")
fun Array<Double>.toTagList() = ListTag(map(Double::toTag).toMutableList())

/**
 * Converts this array to a [ListTag].
 */
@JvmName("toTagListDouble")
fun DoubleArray.toTagList() = ListTag(map(Double::toTag).toMutableList())

/**
 * Converts this array to a [ListTag].
 */
@JvmName("toTagListBoolean")
fun Array<Boolean>.toTagList() = ListTag(map(Boolean::toTag).toMutableList())

/**
 * Converts this array to a [ListTag].
 */
@JvmName("toTagListBoolean")
fun BooleanArray.toTagList() = ListTag(map(Boolean::toTag).toMutableList())

/**
 * Converts this array to a [ListTag].
 */
@JvmName("toTagListChar")
fun Array<Char>.toTagList() = ListTag(map(Char::toTag).toMutableList())

/**
 * Converts this array to a [ListTag].
 */
@JvmName("toTagListChar")
fun CharArray.toTagList() = ListTag(map(Char::toTag).toMutableList())

/**
 * Converts this array to a [ListTag].
 */
@JvmName("toTagListString")
fun Array<String>.toTagList() = ListTag(map(String::toTag).toMutableList())
