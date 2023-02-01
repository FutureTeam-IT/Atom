package it.futurecraft.atom.terminal

import it.futurecraft.atom.utils.ColorConverter
import org.fusesource.jansi.AnsiConsole
import org.tinylog.core.LogEntry
import org.tinylog.writers.AbstractFormatPatternWriter

/**
 * The TinyLog writer
 *
 * Anion since it does not integrate the terminal itself but write onto it.
 */
class AnionWriter(properties: Map<String?, String>) : AbstractFormatPatternWriter(properties) {
    override fun write(logEntry: LogEntry) {
        val rendered = render(logEntry)
        val formatted = ColorConverter.format(rendered)

        if (Ion.isInitialized) {
            Ion.reader.printAbove(formatted)
        } else {
            AnsiConsole.out().print(formatted)
        }
    }

    override fun flush() {
        if (Ion.isInitialized) {
            Ion.terminal.flush()
        } else {
            AnsiConsole.out().flush()
            System.out.flush()
        }
    }

    override fun close() {}
}