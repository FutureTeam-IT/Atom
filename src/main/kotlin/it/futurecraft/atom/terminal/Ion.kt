package it.futurecraft.atom.terminal

import it.futurecraft.atom.Atom
import it.futurecraft.atom.utils.AnsiColors
import net.minestom.server.MinecraftServer
import org.jline.reader.EndOfFileException
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.UserInterruptException
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder
import kotlin.system.exitProcess

/**
 * Ion is the Atom's Terminal powered by **JLine**.
 */
object Ion {
    @JvmStatic
    private val prompt =
        " ${AnsiColors.BLUE}Atom${AnsiColors.WHITE}•${AnsiColors.RED}${AnsiColors.BOLD}ION ${AnsiColors.GRAY}» ${AnsiColors.RESET}"

    @Volatile
    internal lateinit var terminal: Terminal

    @Volatile
    internal lateinit var reader: LineReader

    var running: Boolean = false
        private set

    val isInitialized: Boolean
        get() = ::terminal.isInitialized && ::reader.isInitialized

    fun init() {
        terminal = TerminalBuilder.builder()
            .system(true)
            .build()

        reader = LineReaderBuilder.builder()
            .appName("Atom(ION)")
            .terminal(terminal)
            .completer(Cation())
            .build()

        reader.setOpt(LineReader.Option.DISABLE_EVENT_EXPANSION)
        reader.unsetOpt(LineReader.Option.INSERT_TAB)
    }

    fun launch() {
        running = true

        while (running) {
            try {
                val line = reader.readLine(prompt)

                val commandManager = MinecraftServer.getCommandManager()
                commandManager.execute(commandManager.consoleSender, line)
            } catch (e: UserInterruptException) {
                Atom.shutdown()
                exitProcess(0)
            } catch (e: EndOfFileException) {
                // Ignores EOF Exceptions
                continue
            }
        }
    }

    fun shutdown() {
        running = false

        if (::terminal.isInitialized) {
            terminal.close()
        }
    }
}