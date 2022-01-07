@file:Suppress("NOTHING_TO_INLINE")

package walkmc.serializer.common

import net.benwoodworth.knbt.*
import net.minecraft.server.*
import java.lang.reflect.*

/**
 * Converts this minecraft tag to a serializable tag.
 */
fun NBTBase.fromMinecraft(): NbtTag = when (this) {
	is NBTTagByte -> fromMinecraft()
	is NBTTagShort -> fromMinecraft()
	is NBTTagInt -> fromMinecraft()
	is NBTTagLong -> fromMinecraft()
	is NBTTagFloat -> fromMinecraft()
	is NBTTagDouble -> fromMinecraft()
	is NBTTagByteArray -> fromMinecraft()
	is NBTTagString -> fromMinecraft()
	is NBTTagIntArray -> fromMinecraft()
	is NBTTagList -> fromMinecraft()
	is NBTTagCompound -> fromMinecraft()
	else -> NbtByte(0) // fallback
}

/**
 * Converts this serializable tag to a minecraft tag.
 */
fun NbtTag.toMinecraft(): NBTBase = when (this) {
	is NbtByte -> toMinecraft()
	is NbtShort -> toMinecraft()
	is NbtInt -> toMinecraft()
	is NbtLong -> toMinecraft()
	is NbtFloat -> toMinecraft()
	is NbtDouble -> toMinecraft()
	is NbtByteArray -> toMinecraft()
	is NbtString -> toMinecraft()
	is NbtIntArray -> toMinecraft()
	is NbtList<*> -> toMinecraft()
	is NbtCompound -> toMinecraft()
	else -> NBTTagByte(0) // fallback
}

/**
 * Converts this minecraft tag byte to a serializable [NbtByte].
 */
inline fun NBTTagByte.fromMinecraft(): NbtByte = NbtByte(this.f())

/**
 * Converts this minecraft tag short to a serializable [NbtShort].
 */
inline fun NBTTagShort.fromMinecraft(): NbtShort = NbtShort(this.e())

/**
 * Converts this minecraft tag int to a serializable [NbtInt].
 */
inline fun NBTTagInt.fromMinecraft(): NbtInt = NbtInt(this.d())

/**
 * Converts this minecraft tag long to a serializable [NbtLong].
 */
inline fun NBTTagLong.fromMinecraft(): NbtLong = NbtLong(this.c())

/**
 * Converts this minecraft tag float to a serializable [NbtFloat].
 */
inline fun NBTTagFloat.fromMinecraft(): NbtFloat = NbtFloat(this.h())

/**
 * Converts this minecraft tag double to a serializable [NbtDouble].
 */
inline fun NBTTagDouble.fromMinecraft(): NbtDouble = NbtDouble(this.g())

/**
 * Converts this minecraft tag string to a serializable [NbtString].
 */
inline fun NBTTagString.fromMinecraft(): NbtString = NbtString(this.a_())

/**
 * Converts this minecraft tag int array to a serializable [NbtIntArray].
 */
inline fun NBTTagIntArray.fromMinecraft(): NbtIntArray = NbtIntArray(this.c())

/**
 * Converts this minecraft tag byte array to a serializable [NbtByteArray].
 */
inline fun NBTTagByteArray.fromMinecraft(): NbtByteArray = NbtByteArray(this.c())

/**
 * Converts this minecraft tag list to a serializable [NbtList].
 */
fun NBTTagList.fromMinecraft(): NbtList<NbtTag> = buildNbtList {
	val method = addListMethod
	for (value in content()) {
		method.invoke(this, value.fromMinecraft())
	}
}

internal val addListMethod: Method by lazy {
	NbtListBuilder::class.java.getDeclaredMethod("add")
}

/**
 * Converts this minecraft tag compound to a serializable [NbtCompound].
 */
fun NBTTagCompound.fromMinecraft(): NbtCompound = buildNbtCompound {
	for ((key, value) in all()) {
		put(key, value.fromMinecraft())
	}
}

/**
 * Converts this serializable tag byte to a minecraft [NBTTagByte].
 */
inline fun NbtByte.toMinecraft(): NBTTagByte = NBTTagByte(this.value)

/**
 * Converts this serializable tag short to a minecraft [NBTTagShort].
 */
inline fun NbtShort.toMinecraft(): NBTTagShort = NBTTagShort(this.value)

/**
 * Converts this serializable tag int to a minecraft [NBTTagInt].
 */
inline fun NbtInt.toMinecraft(): NBTTagInt = NBTTagInt(this.value)

/**
 * Converts this serializable tag long to a minecraft [NBTTagLong].
 */
inline fun NbtLong.toMinecraft(): NBTTagLong = NBTTagLong(this.value)

/**
 * Converts this serializable tag float to a minecraft [NBTTagFloat].
 */
inline fun NbtFloat.toMinecraft(): NBTTagFloat = NBTTagFloat(this.value)

/**
 * Converts this serializable tag double to a minecraft [NBTTagDouble].
 */
inline fun NbtDouble.toMinecraft(): NBTTagDouble = NBTTagDouble(this.value)

/**
 * Converts this serializable tag string to a minecraft [NBTTagString].
 */
inline fun NbtString.toMinecraft(): NBTTagString = NBTTagString(this.value)

/**
 * Converts this serializable tag int array to a minecraft [NBTTagIntArray].
 */
inline fun NbtIntArray.toMinecraft(): NBTTagIntArray = NBTTagIntArray(this.toIntArray())

/**
 * Converts this serializable tag byte array to a minecraft [NBTTagByteArray].
 */
inline fun NbtByteArray.toMinecraft(): NBTTagByteArray = NBTTagByteArray(this.toByteArray())

/**
 * Converts this serializable tag list to a minecraft [NBTTagList].
 */
fun <T : NbtTag> NbtList<T>.toMinecraft(): NBTTagList = NBTTagList(map(NbtTag::toMinecraft))

/**
 * Converts this serializable tag compound to a minecraft [NBTTagCompound].
 */
fun NbtCompound.toMinecraft(): NBTTagCompound = NBTTagCompound().apply {
	for ((key, value) in this@toMinecraft) {
		this.set(key, value.toMinecraft())
	}
}
