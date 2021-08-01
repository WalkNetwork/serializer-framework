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

package io.github.uinnn.serializer.serial

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.Bukkit
import org.bukkit.Location

/**
 * A serializer for Location.
 * This will serialize something like this (IN YAML)
 * ```yaml
 * location: "world|5|66|0"
 * ```
 */
object LocationSerializer : KSerializer<Location> {
  override val descriptor = PrimitiveSerialDescriptor("Location", PrimitiveKind.STRING)
  override fun deserialize(decoder: Decoder): Location {
    val split = decoder.decodeString().split('|')
    return Location(
      Bukkit.getWorld(split[0]),
      split[1].toDouble(),
      split[2].toDouble(),
      split[3].toDouble()
    )
  }

  override fun serialize(encoder: Encoder, value: Location) {
    encoder.encodeString("${value.world.name}|${value.blockX}|${value.blockY}|${value.blockZ}")
  }
}