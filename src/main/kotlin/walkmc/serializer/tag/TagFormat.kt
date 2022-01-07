package walkmc.serializer.tag

import kotlinx.serialization.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.*
import walkmc.serializer.*
import walkmc.serializer.common.*
import java.io.*

/**
 * The serial format used by tag files. The tag files dont have any extra configuration
 * so no need to the this object being a class.
 */
object TagFormat : AlterableTagFormat {
	override var serializersModule: SerializersModule = FrameworkModule
	
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


@Serializable
data class Person(var name: String, var age: Int)


fun main() {
	val person = Person("José", 16)
	val bytes = TagFormat.encodeToByteArray(person)
	
	println(bytes.contentToString())
	
	val deserialized = TagFormat.decodeFromByteArray<Person>(bytes)
	println(deserialized)
}
