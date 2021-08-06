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

package io.github.uinnn.serializer.common

import io.github.uinnn.serializer.serial.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

/**
 * The default `serializer-framework` module.
 * This contains serializers to:
 * * Enchantment
 * * ItemStack
 * * Location
 * * MaterialData
 * * Player
 * * StringList (replaces all '&' to '§' and vice-versa)
 * * UUID
 * * World
 */
val FrameworkModule by lazy {
  SerializersModule {
    contextual(EnchantmentSerializer)
    contextual(ItemSerializer)
    contextual(LocationSerializer)
    contextual(MaterialDataSerializer)
    contextual(PlayerSerializer)
    contextual(StringListSerializer)
    contextual(UUIDSerializer)
    contextual(WorldSerializer)
  }
}