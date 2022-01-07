package walkmc.serializer.tag

import kotlin.reflect.*
import kotlin.reflect.full.*

/**
 * Represents a registered tag.
 */
data class RegisteredTag<T : Tag>(val id: Short, val tagClass: KClass<T>) {
	fun create() = tagClass.objectInstance ?: tagClass.createInstance()
}
