package it.futurecraft.atom.commands

import it.futurecraft.atom.Atom
import net.minestom.server.command.ConsoleSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandExecutor
import kotlin.system.exitProcess

internal object Shutdown : Command("stop") {
    init {
        setCondition { sender, _ ->
            sender.hasPermission("atom.command.stop") || (sender is ConsoleSender)
        }

        defaultExecutor = CommandExecutor { _, _ ->
            Atom.shutdown()
            exitProcess(0)
        }
    }
}