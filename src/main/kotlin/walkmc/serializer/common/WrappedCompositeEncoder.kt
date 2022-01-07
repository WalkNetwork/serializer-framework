package walkmc.serializer.common

import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.inventory.*
import walkmc.serializer.serial.*
import java.util.*

/**
 * Represents a productivity util when creating custom serializers
 * that uses composite encoder.
 */
class WrappedCompositeEncoder(
	delegate: CompositeEncoder,
	val descriptor: SerialDescriptor
) : CompositeEncoder by delegate {
	
	var currentIndex = 0
	
	fun encodeString(value: String, index: Int = currentIndex) {
		encodeStringElement(descriptor, index, value)
		currentIndex++
	}
	
	fun encodeInt(value: Int, index: Int = currentIndex) {
		encodeIntElement(descriptor, index, value)
		currentIndex++
	}
	
	fun encodeByte(value: Byte, index: Int = currentIndex) {
		encodeByteElement(descriptor, index, value)
		currentIndex++
	}
	
	fun encodeShort(value: Short, index: Int = currentIndex) {
		encodeShortElement(descriptor, index, value)
		currentIndex++
	}
	
	fun encodeLong(value: Long, index: Int = currentIndex) {
		encodeLongElement(descriptor, index, value)
		currentIndex++
	}
	
	fun encodeDouble(value: Double, index: Int = currentIndex) {
		encodeDoubleElement(descriptor, index, value)
		currentIndex++
	}
	
	fun encodeFloat(value: Float, index: Int = currentIndex) {
		encodeFloatElement(descriptor, index, value)
		currentIndex++
	}
	
	fun encodeInline(index: Int = currentIndex) {
		encodeInlineElement(descriptor, index)
		currentIndex++
	}
	
	fun encodeChar(value: Char, index: Int = currentIndex) {
		encodeCharElement(descriptor, index, value)
		currentIndex++
	}
	
	fun encodeBoolean(value: Boolean, index: Int = currentIndex) {
		encodeBooleanElement(descriptor, index, value)
		currentIndex++
	}
	
	fun encodeEnum(value: Enum<*>, index: Int = currentIndex) {
		encodeString(value.name, index)
		currentIndex++
	}
	
	fun encodeUUID(value: UUID, index: Int = currentIndex) = encode(value, UUIDSerializer, index)
	fun encodeLocation(value: Location, index: Int = currentIndex) = encode(value, LocationSerializer, index)
	fun encodeItem(value: ItemStack, index: Int = currentIndex) = encode(value, ItemSerializer, index)
	
	fun encodeItemList(value: Iterable<ItemStack>, index: Int = currentIndex) =
		encode(value.toList(), ListSerializer(ItemSerializer), index)
	
	fun encodePlayer(value: Player, index: Int = currentIndex) = encode(value, PlayerSerializer, index)
	fun encodeOfflinePlayer(value: OfflinePlayer, index: Int = currentIndex) =
		encode(value, OfflinePlayerSerializer, index)
	
	inline fun <reified T> encodeValue(
		value: T,
		serializer: SerializationStrategy<T> = serializer(),
		index: Int = currentIndex
	) {
		encodeSerializableElement(descriptor, index, serializer, value)
		currentIndex++
	}
	
	inline fun <reified T> encodeNullableValue(
		value: T?,
		serializer: SerializationStrategy<T> = serializer(),
		index: Int = currentIndex
	) {
		encodeNullableSerializableElement(descriptor, index, serializer, value)
		currentIndex++
	}
	
	inline fun <reified T> encode(
		value: T,
		serializer: SerializationStrategy<T> = serializer(),
		index: Int = currentIndex
	) {
		encodeSerializableElement(descriptor, index, serializer, value)
		currentIndex++
	}
	
	inline fun <reified T> encodeNullable(
		value: T?,
		serializer: SerializationStrategy<T> = serializer(),
		index: Int = currentIndex
	) {
		encodeNullableSerializableElement(descriptor, index, serializer, value)
		currentIndex++
	}
}

/**
 * Starts the process for encoding data in a [WrappedCompositeEncoder].
 */
inline fun <T> Encoder.encodeWrappedStructure(
	descriptor: SerialDescriptor,
	block: WrappedCompositeEncoder.() -> T
): T {
	val composite = WrappedCompositeEncoder(beginStructure(descriptor), descriptor)
	var ex: Throwable? = null
	try {
		return composite.block()
	} catch (e: Throwable) {
		ex = e
		throw e
	} finally {
		if (ex == null) composite.endStructure(descriptor)
	}
}

/**
 * Starts the process for encoding data in a [WrappedCompositeEncoder].
 */
inline fun <T> Encoder.encode(
	descriptor: SerialDescriptor,
	block: WrappedCompositeEncoder.() -> T
): T = encodeWrappedStructure(descriptor, block)
