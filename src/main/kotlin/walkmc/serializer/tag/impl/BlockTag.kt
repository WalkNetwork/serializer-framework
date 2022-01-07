package walkmc.serializer.tag.impl

import org.bukkit.block.*
import walkmc.extensions.*
import walkmc.serializer.tag.*
import java.io.*

open class BlockTag : Tag(20), Comparable<BlockTag> {
	lateinit var value: Block
	
	override fun write(data: DataOutput) {
		if (::value.isInitialized) {
			data.writeBlock(value)
		}
	}
	
	override fun read(data: DataInput) {
		value = data.readBlock()
	}
	
	override fun equals(other: Any?): Boolean = other is BlockTag && value == other.value
	
	override fun hashCode(): Int = value.hashCode()
	
	override fun toString(): String = value.toString()
	
	override fun compareTo(other: BlockTag): Int = value.compareTo(other.value)
	
	companion object {
		fun of(block: Block) = BlockTag().apply { value = block }
	}
}

fun Block.toTag() = BlockTag.of(this)
