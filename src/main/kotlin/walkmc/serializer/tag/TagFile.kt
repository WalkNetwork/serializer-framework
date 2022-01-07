package walkmc.serializer.tag

import kotlinx.serialization.*
import walkmc.serializer.*
import walkmc.serializer.common.*
import java.io.*

/**
 * The serial file for coding with Mark files.
 * Also loads the file when constructs a new instance of this class.
 */
open class TagFile<T : Any>(
	override var file: File,
	override var model: T,
	override var serial: KSerializer<T> = model::class.serializer() as KSerializer<T>,
	override var format: AlterableTagFormat = TagFormat,
) : TagSerialFile<T> {
	override var data: T = model
	override var observers: Observers<T> = Observers()
	
	init {
		load()
	}
}
