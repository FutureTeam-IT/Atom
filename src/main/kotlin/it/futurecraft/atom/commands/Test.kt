package it.futurecraft.atom.commands

import net.minestom.server.MinecraftServer
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandExecutor

object Test : Command("test") {
    init {
        defaultExecutor = CommandExecutor { _, _ ->
            MinecraftServer.LOGGER.info("Test command invoked!")
        }
    }
}