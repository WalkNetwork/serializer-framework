package io.github.uinnn.serializer.strategy

import kotlinx.serialization.descriptors.SerialDescriptor

/**
 * Default strategy of all serializations formats!
 * When the Kotlin Serialization encode a Serializable object
 * this will try to encode all strings or list of strings by
 * replacing then to '&', and decoding to '§'
 *
 * This object is equals to:
 * ```kt
 * override fun encodeString(descriptor: SerialDescriptor, index: Int, value: String) =
 *   value.replace('§', '&')
 *
 * override fun decodeString(descriptor: SerialDescriptor, index: Int, value: String) =
 *   value.replace('&', '§')
 * ```
 */
object ColorStrategy : Strategy {
  override fun encodeString(descriptor: SerialDescriptor, index: Int, value: String) =
    value.replace('§', '&')

  override fun decodeString(descriptor: SerialDescriptor, index: Int, value: String) =
    value.replace('&', '§')
}

object ReverseBooleanStrategy : EncoderStrategy, DecoderStrategy {
  override fun encodeBoolean(descriptor: SerialDescriptor, index: Int, value: Boolean): Boolean {
    return value.not()
  }

  override fun decodeBoolean(descriptor: SerialDescriptor, index: Int, value: Boolean): Boolean {
    return value.not()
  }
}