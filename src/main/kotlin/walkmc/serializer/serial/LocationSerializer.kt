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

package walkmc.serializer.serial

import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import org.bukkit.*
import walkmc.extensions.*
import walkmc.extensions.strings.*
import walkmc.serializer.common.*
import walkmc.serializer.tag.*

/**
 * A serializer for Location.
 * This will serialize something like this (IN YAML)
 * ```yaml
 * location: "world:5:66:0"
 * ```
 */
object LocationSerializer : Serializer<Location> {
	override val descriptor = PrimitiveSerialDescriptor("Location", PrimitiveKind.STRING)
	
	override fun deserialize(decoder: Decoder): Location {
		return if (decoder is TagDecoder) {
			decoder.input.readLocation()
		} else {
			val split = decoder.decodeString().split(':', limit = 6)
			Location(split[0].toWorld(), split[1].toDouble(), split[2].toDouble(), split[3].toDouble(), split[4].toFloat(), split[5].toFloat())
		}
	}
	
	override fun serialize(encoder: Encoder, value: Location) {
		if (encoder is TagEncoder) {
			encoder.output.writeLocation(value)
		} else {
			encoder.encodeString("${value.world.name}:${value.x}:${value.y}:${value.z}:${value.yaw}:${value.pitch}")
		}
	}
}
