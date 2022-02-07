package walkmc.serializer.tag

import kotlinx.serialization.*
import kotlinx.serialization.modules.*
import walkmc.serializer.*
import walkmc.serializer.common.*
import java.io.*

/**
 * The serial format used by tag files. The tag files don't have any extra configuration
 * so no need to the object being a class.
 */
class TagFormat(override var serializersModule: SerializersModule = FrameworkModule) : AlterableTagFormat {
	override fun <T> decodeFromByteArray(deserializer: DeserializationStrategy<T>, bytes: ByteArray): T =
		ByteArrayInputStream(bytes).decodeTag(deserializer)
	
	override fun <T> encodeToByteArray(serializer: SerializationStrategy<T>, value: T): ByteArray =
		ByteArrayOutputStream().apply { encodeTag(serializer, value) }.toByteArray()
	
	fun <T> decodeFromStream(stream: InputStream, serializer: DeserializationStrategy<T>) =
		stream.decodeTag(serializer)
	
	fun <T> encodeToStream(stream: OutputStream, serializer: SerializationStrategy<T>, value: T) =
		stream.encodeTag(serializer, value)
	
	fun <T> decodeFromFile(file: File, serializer: DeserializationStrategy<T>) =
		file.decodeTag(serializer)
	
	fun <T> encodeToFile(file: File, serializer: SerializationStrategy<T>, value: T) =
		file.encodeTag(serializer, value)
}

inline fun <reified T> TagFormat.decodeFromStream(stream: InputStream): T =
	stream.decodeTag(serializer())

inline fun <reified T> TagFormat.encodeToStream(stream: OutputStream, value: T) =
	stream.encodeTag(serializer(), value)

inline fun <reified T> TagFormat.decodeFromFile(file: File): T =
	file.decodeTag(serializer())

inline fun <reified T> TagFormat.encodeToFile(file: File, value: T) =
	file.encodeTag(serializer(), value)
