package it.futurecraft.atom.coroutines

import it.futurecraft.atom.Atom
import it.futurecraft.atom.coroutines.dispatcher.Async
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Launch a new coroutine on the Minecraft's main Thread
 *
 * @param context The context of this coroutine
 * @param start The start policy for this coroutine
 * @param block The coroutine code which will be executed in the coroutine context
 * @return The executing job
 */
fun Atom.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job = scope.launch(context, start, block)

/**
 * Launch a new coroutine and return a future result.
 *
 * @param context The context of this coroutine
 * @param start The start policy for this coroutine
 * @param block The coroutine code which will be executed in the coroutine context
 * @return The executing deferred result.
 */
fun <R> Atom.async(
    context: CoroutineContext = Dispatchers.Async,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> R
): Deferred<R> = scope.async(context, start, block)

/**
 * Always lazily launch a new coroutine on the Minecraft's main Thread
 *
 * @param context The context of this coroutine
 * @param block The coroutine code which will be executed in the coroutine context
 * @return The executing job
 */
fun Atom.lazyLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit
): Job = scope.launch(context, CoroutineStart.LAZY, block)

/**
 * Always lazily launch a new coroutine and return a future result.
 *
 * @param context The context of this coroutine
 * @param block The coroutine code which will be executed in the coroutine context
 * @return The executing deferred result.
 */
fun <R> Atom.lazyAsync(
    context: CoroutineContext = Dispatchers.Async,
    block: suspend CoroutineScope.() -> R
): Deferred<R> = scope.async(context, CoroutineStart.LAZY, block)
