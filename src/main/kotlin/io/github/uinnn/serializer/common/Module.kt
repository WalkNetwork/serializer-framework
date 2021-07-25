package io.github.uinnn.serializer.common

import io.github.uinnn.serializer.serial.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

val FrameworkModule by lazy {
  SerializersModule {
    contextual(EnchantmentSerializer)
    contextual(ItemStackSerializer)
    contextual(LocationSerializer)
    contextual(MaterialDataSerializer)
    contextual(PlayerSerializer)
    contextual(StringListSerializer)
    contextual(UUIDSerializer)
    contextual(WorldSerializer)
  }
}