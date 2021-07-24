package com.github.uinnn.serializer.formatter

import com.github.uinnn.serializer.AlterableSerialFormat
import com.github.uinnn.serializer.strategy.DecoderStrategy
import com.github.uinnn.serializer.strategy.EncoderStrategy
import kotlinx.serialization.SerialFormat

open class StrategyFormatter(
  open val model: SerialFormat,
  open var encoder: EncoderStrategy,
  open var decoder: DecoderStrategy,
) : AlterableSerialFormat by model as AlterableSerialFormat