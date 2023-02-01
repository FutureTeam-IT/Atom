package it.futurecraft.atom.coroutines.dispatcher

import it.futurecraft.atom.coroutines.MinestomDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import net.minestom.server.timer.ExecutionType

internal object AsyncDispatcher : MinestomDispatcher(ExecutionType.ASYNC)

/**
 * Launches a coroutine on the Minecraft Server's ThreadPool.
 */
val Dispatchers.Async: CoroutineDispatcher
    get() = AsyncDispatcher
