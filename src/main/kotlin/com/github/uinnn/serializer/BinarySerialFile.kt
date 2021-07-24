package com.github.uinnn.serializer

import kotlinx.serialization.decodeFromHexString
import kotlinx.serialization.encodeToHexString

interface BinarySerialFile<T : Any> : SerialFile<T> {
  override var format: AlterableBinaryFormat

  override fun load() {
    super.load()
    createFile()
    reload()
  }

  override fun reload() {
    super.reload()
    settings = format.decodeFromHexString(serial, file.readText())
  }

  override fun save() {
    super.save()
    file.writeText(format.encodeToHexString(serial, settings))
  }

  override fun saveModel() {
    super.saveModel()
    file.writeText(format.encodeToHexString(serial, model))
  }
}