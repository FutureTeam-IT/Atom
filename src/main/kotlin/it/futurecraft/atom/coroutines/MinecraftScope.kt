package it.futurecraft.atom.coroutines

import it.futurecraft.atom.coroutines.dispatcher.Minecraft
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

internal class MinecraftScope : CoroutineScope {
    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Minecraft
}

fun minecraftScope(): CoroutineScope = MinecraftScope()
