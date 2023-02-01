package it.futurecraft.atom.terminal

import net.minestom.server.MinecraftServer
import net.minestom.server.listener.TabCompleteListener
import org.jline.reader.Candidate
import org.jline.reader.Completer
import org.jline.reader.LineReader
import org.jline.reader.ParsedLine

/**
 * The [Ion]'s command completer.
 *
 * Cation because it charges [Ion] with a positive charge :)
 */
class Cation : Completer {
    override fun complete(reader: LineReader, line: ParsedLine, candidates: MutableList<Candidate>) {
        val commandManager = MinecraftServer.getCommandManager()

        // If we are at the first word
        if (line.wordIndex() == 0) {
            // We suggest command that starts with the line entry
            candidates.addAll(commandManager.commands
                .filter { it.name.startsWith(line.word()) }
                .map { Candidate(it.name) }
            )
        } else {
            // We get the completation from the command if found
            val sender = commandManager.consoleSender
            val command = line.line()

            val suggestions = TabCompleteListener.getSuggestion(sender, command)
            // If there were suggestions for the current line
            if (suggestions != null) {
                candidates.addAll(suggestions.entries.map { Candidate(it.entry) })
            }
        }
    }
}