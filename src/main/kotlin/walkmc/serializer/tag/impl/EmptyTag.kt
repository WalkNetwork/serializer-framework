package walkmc.serializer.tag.impl

import walkmc.serializer.tag.*
import java.io.*

/**
 * Represents an empty tag to be used when a tag is not necessary.
 */
object EmptyTag : Tag(0) {
	override fun write(data: DataOutput) = Unit
	override fun read(data: DataInput) = Unit
}
