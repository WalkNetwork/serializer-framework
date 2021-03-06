/*
                             MIT License

                        Copyright (c) 2021 uin

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package walkmc.serializer

import kotlinx.serialization.*
import kotlinx.serialization.modules.*
import walkmc.serializer.common.*
import walkmc.serializer.tag.*
import java.io.*

/**
 * An alterable serial format is just a [SerialFormat]
 * with mutable [serializersModule] property.
 */
interface AlterableSerialFormat : SerialFormat {
	override var serializersModule: SerializersModule
}

/**
 * An alterable string format is just a [StringFormat]
 * with mutable [serializersModule] property.
 */
interface AlterableStringFormat : AlterableSerialFormat, StringFormat {
	override var serializersModule: SerializersModule
}

/**
 * An alterable binary format is just a [BinaryFormat]
 * with mutable [serializersModule] property.
 */
interface AlterableBinaryFormat : AlterableSerialFormat, BinaryFormat {
	override var serializersModule: SerializersModule
}

/**
 * An alterable mark format is just a [TagFormat]
 * with mutable [serializersModule] property.
 */
interface AlterableTagFormat : AlterableBinaryFormat

/**
 * An alterable stream format is just a [SerialFormat] with mutable
 * [serializersModule] property to use with input/output streams.
 */
interface AlterableStreamFormat : AlterableSerialFormat {
	override var serializersModule: SerializersModule
	
	fun <T> decodeFrom(input: InputStream, deserializer: DeserializationStrategy<T>): T
	fun <T> encodeTo(output: OutputStream, serializer: SerializationStrategy<T>, value: T)
}

/**
 * An abstract implementation of [AlterableSerialFormat].
 */
abstract class AbstractAlterableSerialFormat(
	override var serializersModule: SerializersModule,
	open var model: SerialFormat,
) : AlterableSerialFormat

/**
 * An abstract implementation of [AlterableStringFormat].
 * That's holds a [StringFormat] model to decode/encode strings.
 */
abstract class AbstractAlterableStringFormat(
	override var serializersModule: SerializersModule,
	open var model: StringFormat,
) : AlterableStringFormat {
	override fun <T> decodeFromString(deserializer: DeserializationStrategy<T>, string: String): T {
		return model.decodeFromString(deserializer, string)
	}
	
	override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String {
		return model.encodeToString(serializer, value)
	}
}

/**
 * AN abstract implementation of [AlterableBinaryFormat].
 * That's holds a [BinaryFormat] model to decode/encode byte arrays.
 */
abstract class AbstractAlterableBinaryFormat(
	override var serializersModule: SerializersModule,
	open var model: BinaryFormat,
) : AlterableBinaryFormat {
	override fun <T> decodeFromByteArray(deserializer: DeserializationStrategy<T>, bytes: ByteArray): T {
		return model.decodeFromByteArray(deserializer, bytes)
	}
	
	override fun <T> encodeToByteArray(serializer: SerializationStrategy<T>, value: T): ByteArray {
		return model.encodeToByteArray(serializer, value)
	}
}

/**
 * INTERNAL API
 */
internal class AlterableSerialFormatImpl(
	module: SerializersModule,
	model: SerialFormat,
) : AbstractAlterableSerialFormat(module, model)

/**
 * INTERNAL API
 */
internal class AlterableStringFormatImpl(
	module: SerializersModule,
	model: StringFormat,
) : AbstractAlterableStringFormat(module, model)

/**
 * INTERNAL API
 */
internal class AlterableBinaryFormatImpl(
	module: SerializersModule,
	model: BinaryFormat,
) : AbstractAlterableBinaryFormat(module, model)

/**
 * Object instance to create alterable serial formats,
 * such as [AlterableSerialFormat], [AlterableStringFormat] and finally
 * [AlterableBinaryFormat]
 */
object Alterables {
	
	/**
	 * Creates a [AlterableSerialFormat] with gived module and model.
	 */
	fun serial(module: SerializersModule, model: SerialFormat): AlterableSerialFormat {
		return AlterableSerialFormatImpl(module, model)
	}
	
	/**
	 * Creates a [AlterableStringFormat] with gived module and model.
	 */
	fun string(module: SerializersModule, model: StringFormat): AlterableStringFormat {
		return AlterableStringFormatImpl(module, model)
	}
	
	/**
	 * Creates a [AlterableBinaryFormat] with gived module and model.
	 */
	fun binary(module: SerializersModule, model: BinaryFormat): AlterableBinaryFormat {
		return AlterableBinaryFormatImpl(module, model)
	}
}

/**
 * Converts this serial format to a [AlterableSerialFormat]
 */
fun SerialFormat.asAlterable(module: SerializersModule = FrameworkModule): AlterableSerialFormat {
	return Alterables.serial(module, this)
}

/**
 * Converts this string format to a [AlterableStringFormat]
 */
fun StringFormat.asAlterable(module: SerializersModule = FrameworkModule): AlterableStringFormat {
	return Alterables.string(module, this)
}

/**
 * Converts this binary format to a [AlterableBinaryFormat]
 */
fun BinaryFormat.asAlterable(module: SerializersModule = FrameworkModule): AlterableBinaryFormat {
	return Alterables.binary(module, this)
}
