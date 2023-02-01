package it.futurecraft.atom.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import net.minestom.server.MinecraftServer
import net.minestom.server.ServerProcess
import net.minestom.server.timer.ExecutionType
import kotlin.coroutines.CoroutineContext

abstract class MinestomDispatcher(
    val type: ExecutionType
) : CoroutineDispatcher() {
    companion object {
        @JvmStatic
        internal val process: ServerProcess by lazy {
            MinecraftServer.process()
        }
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (!process.isAlive) {
            return
        }

        process.scheduler().scheduleNextProcess(block, type)
    }
}