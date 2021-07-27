package io.github.uinnn.serializer.formatter

import io.github.uinnn.serializer.AlterableSerialFormat
import io.github.uinnn.serializer.strategy.DecoderStrategy
import io.github.uinnn.serializer.strategy.EncoderStrategy

/**
 * A serial strategy formatter.
 * This is used to modify how kotlin serialization
 * should encode or decode strings, numbers, chars and booleans.
 */
open class StrategyFormatter(
  open val model: AlterableSerialFormat,
  open var encoder: EncoderStrategy,
  open var decoder: DecoderStrategy,
) : AlterableSerialFormat by model