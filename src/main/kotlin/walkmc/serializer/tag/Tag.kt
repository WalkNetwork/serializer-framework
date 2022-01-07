package walkmc.serializer.tag

import net.minecraft.server.*
import java.io.*

/**
 * Represents a abstract tag object. The Mark is used to stores data in a similar way of [NBTTagCompound].
 * This can be called as more extensible representation of NBT's.
 */
abstract class Tag(val id: Short) : Serializable, Cloneable {
	
	/**
	 * Writes this tag to the given data output.
	 */
	abstract fun write(data: DataOutput)
	
	/**
	 * Reads this tag of the given data output.
	 */
	abstract fun read(data: DataInput)
	
	override fun hashCode(): Int = id.hashCode()
	override fun toString(): String = id.toString()
	override fun equals(other: Any?): Boolean = other is Tag && id == other.id
}
