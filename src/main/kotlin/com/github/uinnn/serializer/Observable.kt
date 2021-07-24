package com.github.uinnn.serializer

import java.util.*

typealias ObserverAction = SerialFile<*>.() -> Unit
typealias ObserverSet = HashSet<ObserverAction>

interface Observable {
  var observers: Observers

  /**
   * Trigger a observe event with selected kind.
   */
  fun observe(kind: ObserverKind)

  /**
   * Registers a handler to be triggered
   * when observes a serial file event.
   */
  fun onObserve(kind: ObserverKind, action: ObserverAction) = observers.getOrPut(kind) {
    ObserverSet()
  }.add(action)
}

/**
 * All events that a serial file can made.
 */
enum class ObserverKind {
  LOAD, RELOAD, SAVE, SAVE_MODEL, CREATE
}

/**
 * This class is responsable to observes
 * all events that occours when a serial file changes.
 * Suchs as loads, reloads, saves and saves model.
 * @see ObserverKind
 * @see Observable
 */
class Observers : EnumMap<ObserverKind, ObserverSet>(ObserverKind::class.java)