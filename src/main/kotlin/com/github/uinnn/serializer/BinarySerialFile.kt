package com.github.uinnn.serializer

import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.decodeFromHexString
import kotlinx.serialization.encodeToHexString

interface BinarySerialFile<T : Any> : SerialFile<T> {
  override val format: BinaryFormat

  override fun load() {
    createFile()
    reload()
  }

  override fun reload() {
    settings = format.decodeFromHexString(serial, file.readText())
  }

  override fun save() = file.writeText(format.encodeToHexString(serial, settings))
  override fun saveModel() = file.writeText(format.encodeToHexString(serial, model))
}