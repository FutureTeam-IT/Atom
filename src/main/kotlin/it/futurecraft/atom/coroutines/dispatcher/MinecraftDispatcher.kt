package it.futurecraft.atom.coroutines.dispatcher

import it.futurecraft.atom.coroutines.MinestomDispatcher
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import net.minestom.server.timer.ExecutionType
import kotlin.coroutines.CoroutineContext

internal object MinecraftDispatcher : MinestomDispatcher(ExecutionType.SYNC) {
    private val _thread: Long by lazy {
        val id = atomic(-1L)

        process.scheduler().scheduleNextTick {
            id.value = Thread.currentThread().id
        }

        id.value
    }

    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        return _thread != Thread.currentThread().id
    }
}

/**
 * Sync the task on the Minecraft Server's main thread.
 *
 * Dispatch only if the task has been launched on a different thread from the main one.
 */
val Dispatchers.Minecraft: CoroutineDispatcher
    get() = MinecraftDispatcher