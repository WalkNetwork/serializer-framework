package com.github.uinnn.serializer.common

import com.github.uinnn.serializer.formatter.StrategyFormatter
import com.github.uinnn.serializer.strategy.ColorStrategy

/**
 * Sets the encoder and decoder to defaults.
 * Default: [ColorStrategy]
 */
fun StrategyFormatter.defaults() = apply {
  encoder = ColorStrategy
  decoder = ColorStrategy
}
