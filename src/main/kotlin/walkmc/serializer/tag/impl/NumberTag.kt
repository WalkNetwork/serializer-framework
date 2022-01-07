@file:Suppress("NOTHING_TO_INLINE")

package walkmc.serializer.tag.impl

/**
 * A skeletal interface used to create tags that holds numbers.
 */
interface NumberTag {
	
	/**
	 * Converts this tag number to a byte.
	 */
	fun toByte(): Byte
	
	/**
	 * Converts this tag number to a short.
	 */
	fun toShort(): Short
	
	/**
	 * Converts this tag number to a int.
	 */
	fun toInt(): Int
	
	/**
	 * Converts this tag number to a long.
	 */
	fun toLong(): Long
	
	/**
	 * Converts this tag number to a float.
	 */
	fun toFloat(): Float
	
	/**
	 * Converts this tag number to a double.
	 */
	fun toDouble(): Double
}

/**
 * Converts this tag number to [UByte]
 */
inline fun NumberTag.toUByte(): UByte = toByte().toUByte()

/**
 * Converts this tag number to [UShort]
 */
inline fun NumberTag.toUShort(): UShort = toShort().toUShort()

/**
 * Converts this tag number to [UInt]
 */
inline fun NumberTag.toUInt(): UInt = toInt().toUInt()

/**
 * Converts this tag number to [ULong]
 */
inline fun NumberTag.toULong(): ULong = toLong().toULong()

/**
 * Converts this tag number to [ByteTag]
 */
inline fun NumberTag.toTagByte(): ByteTag = ByteTag(toByte())

/**
 * Converts this tag number to [ShortTag]
 */
inline fun NumberTag.toTagShort(): ShortTag = ShortTag(toShort())

/**
 * Converts this tag number to [IntTag]
 */
inline fun NumberTag.toTagInt(): IntTag = IntTag(toInt())

/**
 * Converts this tag number to [LongTag]
 */
inline fun NumberTag.toTagLong(): LongTag = LongTag(toLong())

/**
 * Converts this tag number to [FloatTag]
 */
inline fun NumberTag.toTagFloat(): FloatTag = FloatTag(toFloat())

/**
 * Converts this tag number to [DoubleTag]
 */
inline fun NumberTag.toTagDouble(): DoubleTag = DoubleTag(toDouble())

/**
 * Converts this byte to a [ByteTag].
 */
inline fun Byte.toTag() = ByteTag(this)

/**
 * Converts this short to a [ShortTag].
 */
inline fun Short.toTag() = ShortTag(this)

/**
 * Converts this int to a [IntTag].
 */
inline fun Int.toTag() = IntTag(this)

/**
 * Converts this long to a [LongTag].
 */
inline fun Long.toTag() = LongTag(this)

/**
 * Converts this float to a [FloatTag].
 */
inline fun Float.toTag() = FloatTag(this)

/**
 * Converts this double to a [DoubleTag].
 */
inline fun Double.toTag() = DoubleTag(this)
