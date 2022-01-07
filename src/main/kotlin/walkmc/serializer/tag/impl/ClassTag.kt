@file:Suppress("NOTHING_TO_INLINE")

package walkmc.serializer.tag.impl

import walkmc.extensions.*
import walkmc.serializer.tag.*
import kotlin.reflect.*
import java.io.*

/**
 * A mark implementation to stores [KClass].
 * This allows getting almost any class.
 */
open class ClassTag : Tag(18) {
	lateinit var value: KClass<*>
	
	override fun write(data: DataOutput) {
		if (::value.isInitialized) {
			data.writeClass(value)
		}
	}
	
	override fun read(data: DataInput) {
		value = data.readClass()
	}
	
	override fun toString(): String = value.qualifiedName ?: value.toString()
	override fun equals(other: Any?): Boolean = other is ClassTag && value == other.value
	override fun hashCode(): Int = value.hashCode()
	
	companion object {
		fun of(value: KClass<*>) = ClassTag().apply { this.value = value }
	}
}

/**
 * Converts this kotlin class to a [ClassTag].
 */
inline fun KClass<*>.toTag() = ClassTag.of(this)

/**
 * Converts this java class to a [ClassTag].
 */
inline fun Class<*>.toTag() = ClassTag.of(kotlin)

