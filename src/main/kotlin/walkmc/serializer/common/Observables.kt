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

/**
 * Shortcut for adding a observer event
 * handler for pre-saving a serial file.
 */
fun <T : Any> Observable<T>.onPreSave(action: ObserverAction<T>) = onObserve(ObserverKind.PRE_SAVE, action)

/**
 * Shortcut for adding a observer event
 * handler for saving a serial file.
 */
fun <T : Any> Observable<T>.onSave(action: ObserverAction<T>) = onObserve(ObserverKind.SAVE, action)

/**
 * Shortcut for adding a observer event
 * handler for pre-loading a serial file.
 */
fun <T : Any> Observable<T>.onPreLoad(action: ObserverAction<T>) = onObserve(ObserverKind.PRE_LOAD, action)

/**
 * Shortcut for adding a observer event
 * handler for loading a serial file.
 */
fun <T : Any> Observable<T>.onLoad(action: ObserverAction<T>) = onObserve(ObserverKind.LOAD, action)

/**
 * Shortcut for adding a observer event
 * handler for pre-reloading a serial file.
 */
fun <T : Any> Observable<T>.onPreReload(action: ObserverAction<T>) = onObserve(ObserverKind.PRE_RELOAD, action)

/**
 * Shortcut for adding a observer event
 * handler for reloading a serial file.
 */
fun <T : Any> Observable<T>.onReload(action: ObserverAction<T>) = onObserve(ObserverKind.RELOAD, action)

/**
 * Shortcut for adding a observer event
 * handler for pre-saving model of a serial file.
 */
fun <T : Any> Observable<T>.onPreSaveModel(action: ObserverAction<T>) = onObserve(ObserverKind.PRE_SAVE_MODEL, action)

/**
 * Shortcut for adding a observer event
 * handler for saving model of a serial file.
 */
fun <T : Any> Observable<T>.onSaveModel(action: ObserverAction<T>) = onObserve(ObserverKind.SAVE_MODEL, action)

/**
 * Shortcut for adding a observer event
 * handler for creating a serial file.
 */
fun <T : Any> Observable<T>.onCreate(action: ObserverAction<T>) = onObserve(ObserverKind.CREATE, action)
