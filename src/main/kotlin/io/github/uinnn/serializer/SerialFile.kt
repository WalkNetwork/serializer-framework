package io.github.uinnn.serializer

import kotlinx.serialization.KSerializer
import java.io.File

/**
 * A SerialFile is a configurable file that can
 * reloads, saves, etc with [Observable]. Serials files
 * holds a model to know what he will serialize or deserialize
 * in the content of the file.
 */
interface SerialFile<T : Any> : Observable {

  /**
   * The file object of
   * this serial file.
   */
  var file: File

  /**
   * A model initializer instance of
   * this serial file.
   */
  var model: T

  /**
   * The settings of this serial file,
   * this is, all save/loads/reloads
   * will update this property.
   */
  var settings: T

  /**
   * A serial instance of
   * this serial file.
   */
  var serial: KSerializer<T>

  /**
   * The format that this
   * serial file will be
   * encode/decode.
   */
  val format: AlterableSerialFormat

  /**
   * Loads for the first time
   * this serial file.
   */
  fun load() {
    observe(ObserverKind.LOAD)
  }

  /**
   * Reloads the current file
   * and updates the [settings] property.
   */
  fun reload() {
    observe(ObserverKind.RELOAD)
  }

  /**
   * Saves the model of this
   * serial file.
   */
  fun saveModel() {
    observe(ObserverKind.SAVE_MODEL)
  }

  /**
   * Saves the [settings] to the file.
   */
  fun save() {
    observe(ObserverKind.SAVE)
  }

  /**
   * Creates the file if not exists.
   */
  fun createFile(savesModel: Boolean = true) {
    if (!file.exists()) {
      file.parentFile.mkdirs()
      file.createNewFile()
      observe(ObserverKind.CREATE)
      if (savesModel) saveModel()
    }
  }

  override fun observe(kind: ObserverKind) {
    observers[kind]?.forEach { action ->
      action(this)
    }
  }
}