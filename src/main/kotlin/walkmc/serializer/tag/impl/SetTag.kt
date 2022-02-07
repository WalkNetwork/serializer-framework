@file:Suppress("NOTHING_TO_INLINE")

package walkmc.serializer.tag.impl

import org.bukkit.*
import org.bukkit.block.*
import org.bukkit.inventory.*
import walkmc.*
import walkmc.extensions.*
import walkmc.extensions.collections.*
import walkmc.serializer.tag.*
import kotlin.contracts.*
import java.io.*
import java.util.*

/**
 * A tag implementation to stores [MutableSet].
 */
open class SetTag<T : Tag>(
	val value: MutableSet<T> = newMutableSet(),
) : Tag(11), MutableSet<T> by value {
	
	/**
	 * Constructs a new tag set by converting a map to a set.
	 */
	constructor(map: Map<T, Boolean>) : this(newSetFromMap(map))
	
	/**
	 * The type id of this tag set generic type.
	 */
	var typeId: Short = 0
	
	override fun write(data: DataOutput) {
		if (isEmpty())
			return
		
		typeId = elementAt(0).id
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
	
	fun copy() = HashSet(value)
	fun copy(set: Set<*>) = set + value
	
	override fun toString(): String = value.toString()
	override fun equals(other: Any?): Boolean = other is SetTag<*> && value == other.value
	override fun hashCode(): Int = value.hashCode()
}

/**
 * Builds a tag set by the specified [builder] function block.
 */
inline fun <T : Tag> buildTagSet(builder: SetTag<T>.() -> Unit): SetTag<T> {
	contract {
		callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
	}
	
	return SetTag<T>().apply(builder)
}

/**
 * Creates a new tag set with the specified elements.
 */
inline fun <T : Tag> tagSetOf(vararg elements: T) = SetTag(elements.toMutableSet())

/**
 * Adds a string value to this string tag set.
 */
inline fun SetTag<StringTag>.add(value: String) = add(value.toTag())

/**
 * Adds a byte value to this byte tag set.
 */
inline fun SetTag<ByteTag>.add(value: Byte) = add(value.toTag())

/**
 * Adds a short value to this short tag set.
 */
inline fun SetTag<ShortTag>.add(value: Short) = add(value.toTag())

/**
 * Adds a int value to this int tag set.
 */
inline fun SetTag<IntTag>.add(value: Int) = add(value.toTag())

/**
 * Adds a long value to this long tag set.
 */
inline fun SetTag<LongTag>.add(value: Long) = add(value.toTag())

/**
 * Adds a float value to this float tag set.
 */
inline fun SetTag<FloatTag>.add(value: Float) = add(value.toTag())

/**
 * Adds a double value to this double tag set.
 */
inline fun SetTag<DoubleTag>.add(value: Double) = add(value.toTag())

/**
 * Adds a boolean value to this boolean tag set.
 */
inline fun SetTag<BooleanTag>.add(value: Boolean) = add(value.toTag())

/**
 * Adds a char value to this char tag set.
 */
inline fun SetTag<CharTag>.add(value: Char) = add(value.toTag())

@JvmName("toTagSetTag")
fun Iterable<Tag>.toTagSet() = SetTag(toMutableSet())

@JvmName("toTagSetSlot")
fun Iterable<Slot>.toTagSet() = SetTag(map(Slot::toTag).toMutableSet())

@JvmName("toTagSetItemStack")
fun Iterable<ItemStack>.toTagSet() = SetTag(map(ItemStack::toTag).toMutableSet())

@JvmName("toTagSetUuidTag")
fun Iterable<UUID>.toTagSet() = SetTag(map(UUID::toTag).toMutableSet())

@JvmName("toTagSetLocation")
fun Iterable<Location>.toTagSet() = SetTag(map(Location::toTag).toMutableSet())

@JvmName("toTagSetBlock")
fun Iterable<Block>.toTagSet() = SetTag(map(Block::toTag).toMutableSet())

@JvmName("toTagSetInventory")
fun Iterable<Inventory>.toTagSet() = SetTag(map(Inventory::toTag).toMutableSet())


/**
 * Converts this iterable to a [SetTag].
 */
inline fun Iterable<Byte>.toTagSet() = SetTag(map(Byte::toTag).toMutableSet())

/**
 * Converts this iterable to a [SetTag].
 */
@JvmName("toMarkShort")
inline fun Iterable<Short>.toTagSet() = SetTag(map(Short::toTag).toMutableSet())

/**
 * Converts this iterable to a [SetTag].
 */
@JvmName("toMarkInt")
inline fun Iterable<Int>.toTagSet() = SetTag(map(Int::toTag).toMutableSet())

/**
 * Converts this iterable to a [SetTag].
 */
@JvmName("toMarkLong")
inline fun Iterable<Long>.toTagSet() = SetTag(map(Long::toTag).toMutableSet())

/**
 * Converts this iterable to a [SetTag].
 */
@JvmName("toMarkFloat")
inline fun Iterable<Float>.toTagSet() = SetTag(map(Float::toTag).toMutableSet())

/**
 * Converts this iterable to a [SetTag].
 */
@JvmName("toMarkDouble")
inline fun Iterable<Double>.toTagSet() = SetTag(map(Double::toTag).toMutableSet())

/**
 * Converts this iterable to a [SetTag].
 */
@JvmName("toMarkBoolean")
inline fun Iterable<Boolean>.toTagSet() = SetTag(map(Boolean::toTag).toMutableSet())

/**
 * Converts this iterable to a [SetTag].
 */
@JvmName("toMarkChar")
inline fun Iterable<Char>.toTagSet() = SetTag(map(Char::toTag).toMutableSet())

/**
 * Converts this iterable to a [SetTag].
 */
@JvmName("toMarkString")
inline fun Iterable<String>.toTagSet() = SetTag(map(String::toTag).toMutableSet())

/**
 * Converts this array to a [SetTag].
 */
inline fun Array<Byte>.toTagSet() = SetTag(map(Byte::toTag).toMutableSet())

/**
 * Converts this array to a [SetTag].
 */
inline fun ByteArray.toTagSet() = SetTag(map(Byte::toTag).toMutableSet())

/**
 * Converts this array to a [SetTag].
 */
@JvmName("toMarkShort")
inline fun Array<Short>.toTagSet() = SetTag(map(Short::toTag).toMutableSet())

/**
 * Converts this array to a [SetTag].
 */
@JvmName("toMarkShort")
inline fun ShortArray.toTagSet() = SetTag(map(Short::toTag).toMutableSet())

/**
 * Converts this array to a [SetTag].
 */
@JvmName("toMarkInt")
inline fun Array<Int>.toTagSet() = SetTag(map(Int::toTag).toMutableSet())

/**
 * Converts this array to a [SetTag].
 */
@JvmName("toMarkInt")
inline fun IntArray.toTagSet() = SetTag(map(Int::toTag).toMutableSet())

/**
 * Converts this array to a [SetTag].
 */
@JvmName("toMarkLong")
inline fun Array<Long>.toTagSet() = SetTag(map(Long::toTag).toMutableSet())

/**
 * Converts this array to a [SetTag].
 */
@JvmName("toMarkLong")
inline fun LongArray.toTagSet() = SetTag(map(Long::toTag).toMutableSet())

/**
 * Converts this array to a [SetTag].
 */
@JvmName("toMarkFloat")
inline fun Array<Float>.toTagSet() = SetTag(map(Float::toTag).toMutableSet())

/**
 * Converts this array to a [SetTag].
 */
@JvmName("toMarkFloat")
inline fun FloatArray.toTagSet() = SetTag(map(Float::toTag).toMutableSet())

/**
 * Converts this array to a [SetTag].
 */
@JvmName("toMarkDouble")
inline fun Array<Double>.toTagSet() = SetTag(map(Double::toTag).toMutableSet())

/**
 * Converts this array to a [SetTag].
 */
@JvmName("toMarkDouble")
inline fun DoubleArray.toTagSet() = SetTag(map(Double::toTag).toMutableSet())

/**
 * Converts this array to a [SetTag].
 */
@JvmName("toMarkBoolean")
inline fun Array<Boolean>.toTagSet() = SetTag(map(Boolean::toTag).toMutableSet())

/**
 * Converts this array to a [SetTag].
 */
@JvmName("toMarkBoolean")
inline fun BooleanArray.toTagSet() = SetTag(map(Boolean::toTag).toMutableSet())

/**
 * Converts this array to a [SetTag].
 */
@JvmName("toMarkChar")
inline fun Array<Char>.toTagSet() = SetTag(map(Char::toTag).toMutableSet())

/**
 * Converts this array to a [SetTag].
 */
@JvmName("toMarkChar")
inline fun CharArray.toTagSet() = SetTag(map(Char::toTag).toMutableSet())

/**
 * Converts this array to a [SetTag].
 */
@JvmName("toMarkString")
inline fun Array<String>.toTagSet() = SetTag(map(String::toTag).toMutableSet())
