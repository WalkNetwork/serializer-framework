package com.github.uinnn.serializer

import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.SerialFormat
import kotlinx.serialization.StringFormat
import kotlinx.serialization.modules.SerializersModule

interface AlterableSerialFormat : SerialFormat {
  override var serializersModule: SerializersModule
}

interface AlterableStringFormat : AlterableSerialFormat, StringFormat {
  override var serializersModule: SerializersModule
}

interface AlterableBinaryFormat : AlterableSerialFormat, BinaryFormat {
  override var serializersModule: SerializersModule
}