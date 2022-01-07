package walkmc.serializer.common

import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import walkmc.serializer.serial.*

/**
 * Represents a productivity util when creating custom serializers
 * that uses composite decoder.
 */
class WrappedCompositeDecoder(
	delegate: CompositeDecoder,
	val descriptor: SerialDescriptor
) : CompositeDecoder by delegate {
	
	fun decodeIndex() = decodeElementIndex(descriptor)
	
	fun decodeString(index: Int = decodeIndex()) = decodeStringElement(descriptor, index)
	fun decodeInt(index: Int = decodeIndex()) = decodeIntElement(descriptor, index)
	fun decodeByte(index: Int = decodeIndex()) = decodeByteElement(descriptor, index)
	fun decodeShort(index: Int = decodeIndex()) = decodeShortElement(descriptor, index)
	fun decodeLong(index: Int = decodeIndex()) = decodeLongElement(descriptor, index)
	fun decodeFloat(index: Int = decodeIndex()) = decodeFloatElement(descriptor, index)
	fun decodeDouble(index: Int = decodeIndex()) = decodeDoubleElement(descriptor, index)
	fun decodeBoolean(index: Int = decodeIndex()) = decodeBooleanElement(descriptor, index)
	fun decodeChar(index: Int = decodeIndex()) = decodeCharElement(descriptor, index)
	fun decodeInline(index: Int = decodeIndex()) = decodeInlineElement(descriptor, index)
	
	fun decodeUUID(index: Int = decodeIndex()) = decode(UUIDSerializer, index)
	fun decodeLocation(index: Int = decodeIndex()) = decode(LocationSerializer, index)
	fun decodeItem(index: Int = decodeIndex()) = decode(ItemSerializer, index)
	fun decodeItemList(index: Int = decodeIndex()) = decode(ListSerializer(ItemSerializer), index)
	fun decodePlayer(index: Int = decodeIndex()) = decode(PlayerSerializer, index)
	fun decodeOfflinePlayer(index: Int = decodeIndex()) = decode(OfflinePlayerSerializer, index)
	
	inline fun <reified T : Enum<T>> decodeEnum(index: Int = decodeIndex()) =
		enumValueOf<T>(decodeString(index))
	
	inline fun <reified T> decodeValue(
		deserializer: DeserializationStrategy<T> = serializer(),
		index: Int = decodeIndex(),
		value: T? = null,
	) = decodeSerializableElement(descriptor, index, deserializer, value)
	
	inline fun <reified T> decodeNullableValue(
		deserializer: DeserializationStrategy<T?> = serializer(),
		index: Int = decodeIndex(),
		value: T? = null,
	) = decodeNullableSerializableElement(descriptor, index, deserializer, value)
	
	inline fun <reified T> decode(
		deserializer: DeserializationStrategy<T> = serializer(),
		index: Int = decodeIndex(),
		value: T? = null,
	) = decodeSerializableElement(descriptor, index, deserializer, value)
	
	inline fun <reified T> decodeNullable(
		deserializer: DeserializationStrategy<T?> = serializer(),
		index: Int = decodeIndex(),
		value: T? = null,
	) = decodeNullableSerializableElement(descriptor, index, deserializer, value)
}

/**
 * Starts the process for encoding data in a [WrappedCompositeEncoder].
 */
inline fun <T> Decoder.decodeWrappedStructure(
	descriptor: SerialDescriptor,
	block: WrappedCompositeDecoder.() -> T
): T {
	val composite = WrappedCompositeDecoder(beginStructure(descriptor), descriptor)
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
inline fun <T> Decoder.decode(
	descriptor: SerialDescriptor,
	block: WrappedCompositeDecoder.() -> T
): T = decodeWrappedStructure(descriptor, block)
