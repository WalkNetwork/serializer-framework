package com.github.uinnn.serializer

import kotlinx.serialization.StringFormat

interface StringSerialFile<T : Any> : SerialFile<T> {
  override val format: StringFormat

  override fun load() {
    createFile()
    settings = format.decodeFromString(serial, file.readText())
  }

  override fun reload() {
    settings = format.decodeFromString(serial, file.readText())
  }

  override fun saveModel() = file.writeText(format.encodeToString(serial, model))
  override fun save() = file.writeText(format.encodeToString(serial, settings))
}