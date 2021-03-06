/*
                             MIT License

                        Copyright (c) 2021 uin

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package walkmc.serializer.common

import walkmc.serializer.*
import java.util.*

typealias ObserverAction<T> = SerialFile<T>.() -> Unit
typealias ObserverSet<T> = HashSet<ObserverAction<T>>

/**
 * A observable handler, this is, everything that will
 * occours with the serial file, such as loads, creates, reloads
 * and saves, will observe then. This is like a event handler.
 */
interface Observable<T : Any> {
	
	/**
	 * All registered observers of this
	 * observable object.
	 */
	var observers: Observers<T>
	
	/**
	 * Trigger a observe event with selected kind.
	 */
	fun observe(kind: ObserverKind)
	
	/**
	 * Registers a handler to be triggered
	 * when observes a serial file event.
	 */
	fun onObserve(kind: ObserverKind, action: ObserverAction<T>) = observers.getOrPut(kind) {
		ObserverSet()
	}.add(action)
}

/**
 * All events that a serial file can made.
 */
enum class ObserverKind {
	PRE_LOAD,
	LOAD,
	PRE_RELOAD,
	RELOAD,
	PRE_SAVE,
	SAVE,
	PRE_SAVE_MODEL,
	SAVE_MODEL,
	CREATE
}

/**
 * This class is responsable to observes
 * all events that occours when a serial file changes.
 * Suchs as loads, reloads, saves and saves model.
 * @see ObserverKind
 * @see Observable
 */
class Observers<T> : EnumMap<ObserverKind, ObserverSet<T>>(ObserverKind::class.java)
