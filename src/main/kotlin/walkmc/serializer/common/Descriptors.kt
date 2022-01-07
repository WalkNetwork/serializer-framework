package walkmc.serializer.common

import kotlinx.serialization.descriptors.*
import walkmc.serializer.serial.*

/**
 * Build a new class descriptor with [block].
 */
fun buildDescriptor(
	name: String,
	vararg typeParameters: SerialDescriptor,
	block: ClassSerialDescriptorBuilder.() -> Unit
): SerialDescriptor = buildClassSerialDescriptor(name, *typeParameters, builderAction = block)

/**
 * A string version of [element].
 */
fun ClassSerialDescriptorBuilder.elementString(
	elementName: String,
	annotations: List<Annotation> = emptyList(),
	isOptional: Boolean = false
) = element<String>(elementName, annotations, isOptional)

/**
 * A char version of [element].
 */
fun ClassSerialDescriptorBuilder.elementChar(
	elementName: String,
	annotations: List<Annotation> = emptyList(),
	isOptional: Boolean = false
) = element<Char>(elementName, annotations, isOptional)

/**
 * A boolean version of [element].
 */
fun ClassSerialDescriptorBuilder.elementBoolean(
	elementName: String,
	annotations: List<Annotation> = emptyList(),
	isOptional: Boolean = false
) = element<Boolean>(elementName, annotations, isOptional)

/**
 * A byte version of [element].
 */
fun ClassSerialDescriptorBuilder.elementByte(
	elementName: String,
	annotations: List<Annotation> = emptyList(),
	isOptional: Boolean = false
) = element<Byte>(elementName, annotations, isOptional)

/**
 * A short version of [element].
 */
fun ClassSerialDescriptorBuilder.elementShort(
	elementName: String,
	annotations: List<Annotation> = emptyList(),
	isOptional: Boolean = false
) = element<Short>(elementName, annotations, isOptional)

/**
 * An int version of [element].
 */
fun ClassSerialDescriptorBuilder.elementInt(
	elementName: String,
	annotations: List<Annotation> = emptyList(),
	isOptional: Boolean = false
) = element<Int>(elementName, annotations, isOptional)

/**
 * A long version of [element].
 */
fun ClassSerialDescriptorBuilder.elementLong(
	elementName: String,
	annotations: List<Annotation> = emptyList(),
	isOptional: Boolean = false
) = element<Long>(elementName, annotations, isOptional)

/**
 * A float version of [element].
 */
fun ClassSerialDescriptorBuilder.elementFloat(
	elementName: String,
	annotations: List<Annotation> = emptyList(),
	isOptional: Boolean = false
) = element<Float>(elementName, annotations, isOptional)

/**
 * A double version of [element].
 */
fun ClassSerialDescriptorBuilder.elementDouble(
	elementName: String,
	annotations: List<Annotation> = emptyList(),
	isOptional: Boolean = false
) = element<Double>(elementName, annotations, isOptional)

/**
 * A enum version of [element].
 */
fun ClassSerialDescriptorBuilder.elementEnum(
	elementName: String,
	annotations: List<Annotation> = emptyList(),
	isOptional: Boolean = false
) = element<String>(elementName, annotations, isOptional)

/**
 * A uuid version of [element].
 */
fun ClassSerialDescriptorBuilder.elementUUID(
	elementName: String,
	annotations: List<Annotation> = emptyList(),
	isOptional: Boolean = false
) = element(elementName, UUIDSerializer.descriptor, annotations, isOptional)

/**
 * A location version of [element].
 */
fun ClassSerialDescriptorBuilder.elementLocation(
	elementName: String,
	annotations: List<Annotation> = emptyList(),
	isOptional: Boolean = false
) = element(elementName, LocationSerializer.descriptor, annotations, isOptional)

/**
 * A item stack version of [element].
 */
fun ClassSerialDescriptorBuilder.elementItem(
	elementName: String,
	annotations: List<Annotation> = emptyList(),
	isOptional: Boolean = false
) = element(elementName, ItemSerializer.descriptor, annotations, isOptional)

/**
 * A item stack list version of [element].
 */
fun ClassSerialDescriptorBuilder.elementItemList(
	elementName: String,
	annotations: List<Annotation> = emptyList(),
	isOptional: Boolean = false
) = element(elementName, listSerialDescriptor(ItemSerializer.descriptor), annotations, isOptional)

/**
 * A player version of [element].
 */
fun ClassSerialDescriptorBuilder.elementPlayer(
	elementName: String,
	annotations: List<Annotation> = emptyList(),
	isOptional: Boolean = false
) = element(elementName, PlayerSerializer.descriptor, annotations, isOptional)

/**
 * A offline player version of [element].
 */
fun ClassSerialDescriptorBuilder.elementOfflinePlayer(
	elementName: String,
	annotations: List<Annotation> = emptyList(),
	isOptional: Boolean = false
) = element(elementName, OfflinePlayerSerializer.descriptor, annotations, isOptional)
