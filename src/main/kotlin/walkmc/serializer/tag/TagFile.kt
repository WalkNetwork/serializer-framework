package walkmc.serializer.tag

import kotlinx.serialization.*
import walkmc.extensions.*
import walkmc.serializer.*
import walkmc.serializer.common.*
import java.io.*

/**
 * Creates a tag file with all provided params.
 *
 * This is a shortcut for non-creating any class/object for the config file.
 */
fun <T : Any> tag(
	file: File,
	model: T,
	serial: KSerializer<T> = model::class.serializer().cast(),
	format: AlterableTagFormat = TagFormat(),
	callback: TagFile<T>.() -> Unit = {}
) = TagFile(file, model, serial, format).apply(callback)

/**
 * The serial file for coding with Mark files.
 * Also loads the file when constructs a new instance of this class.
 */
open class TagFile<T : Any>(
    override var file: File,
    override var model: T,
    override var serial: KSerializer<T> = model::class.serializer().cast(),
    override var format: AlterableTagFormat = TagFormat(),
) : TagSerialFile<T> {
    override var data: T = model
    override var observers: Observers<T> = Observers()
    
    init {
        load()
    }
}
