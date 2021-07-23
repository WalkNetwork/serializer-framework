package com.github.uinnn.serializer.formatter

import com.github.uinnn.serializer.common.FrameworkModule
import com.github.uinnn.serializer.strategy.DecoderStrategy
import com.github.uinnn.serializer.strategy.EncoderStrategy
import kotlinx.serialization.SerialFormat
import kotlinx.serialization.modules.SerializersModule

open class StrategyFormatter(
  open val model: SerialFormat,
  open var encoder: EncoderStrategy,
  open var decoder: DecoderStrategy,
) : SerialFormat by model {
  override val serializersModule: SerializersModule
    get() = FrameworkModule
}