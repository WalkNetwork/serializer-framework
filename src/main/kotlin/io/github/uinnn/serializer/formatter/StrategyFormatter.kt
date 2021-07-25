package io.github.uinnn.serializer.formatter

import io.github.uinnn.serializer.AlterableSerialFormat
import io.github.uinnn.serializer.strategy.DecoderStrategy
import io.github.uinnn.serializer.strategy.EncoderStrategy

open class StrategyFormatter(
  open val model: AlterableSerialFormat,
  open var encoder: EncoderStrategy,
  open var decoder: DecoderStrategy,
) : AlterableSerialFormat by model