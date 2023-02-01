package it.futurecraft.atom

import it.futurecraft.atom.commands.Shutdown
import it.futurecraft.atom.coroutines.minecraftScope
import it.futurecraft.atom.instance.Doper
import it.futurecraft.atom.io.FileManager
import it.futurecraft.atom.terminal.Ion
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import net.kyori.adventure.text.logger.slf4j.ComponentLogger
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.extras.MojangAuth
import net.minestom.server.extras.bungee.BungeeCordProxy
import net.minestom.server.extras.lan.OpenToLAN
import net.minestom.server.extras.optifine.OptifineSupport
import net.minestom.server.extras.velocity.VelocityProxy
import java.nio.file.Path
import kotlin.concurrent.thread

//⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡾⠋⠙⢷⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀
//⠀⠀⠀⠀⠀⠀⠀⠀⢀⡿⠁⠀⠀⠈⢿⡀⠀⠀⠀⠀⠀⠀⠀⠀
//⢀⣀⣤⣤⣀⣀⡀⠀⢸⠃⠀⠀⠀⠀⠘⡇⠀⢀⣀⣀⣤⣤⣀⡀
//⢸⠉⠀⠀⠉⠉⠛⠻⣿⣤⣀⠀⠀⣀⣤⣿⠟⠛⠉⠉⠁⠈⠉⡇
//⠘⣧⡀⠀⠀⠀⠀⠀⣇⣀⣽⠿⠿⣯⣀⣸⠀⠀⠀⠀⠀⢀⣼⠃
//⠀⠈⠻⣦⡀⠀⣠⣴⡟⠉⠀⢀⡀⠀⠉⢻⣦⣄⠀⢀⣴⠟⠁⠀
//⠀⠀⠀⢈⣿⣿⣉⠀⡇⠀⢰⣿⣿⠆⠀⢸⠀⣉⣿⣿⡁⠀⠀⠀ATOM :)
//⠀⢀⣴⠟⠁⠀⠙⠻⣧⣀⠀⠉⠉⠀⣀⣼⠟⠋⠀⠈⠻⣦⡀⠀
//⢠⡟⠁⠀⠀⠀⠀⠀⡏⠉⣻⣶⣶⣟⠉⢹⠀⠀⠀⠀⠀⠈⢻⡄
//⢸⣀⠀⠀⣀⣀⣤⣴⣿⠛⠉⠀⠀⠉⠛⣿⣦⣤⣀⣀⠀⠀⣀⡇
//⠈⠉⠛⠛⠉⠉⠁⠀⢸⡄⠀⠀⠀⠀⢠⡇⠀⠈⠉⠉⠛⠛⠉⠁
//⠀⠀⠀⠀⠀⠀⠀⠀⠈⣷⡀⠀⠀⢀⣾⠁⠀⠀⠀⠀⠀⠀⠀⠀
//⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⢷⣄⣠⡾⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀
//⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠁⠀⠀⠀⠀⠀⠀

/**
 * The basic structure for the existence.
 */
object Atom {
    const val VERSION: String = "@version@"
    const val GROUP: String = "@group@"

    const val MINECRAFT_VERSION: String = "@minecraft_version@"

    const val COMMIT: String = "@commit@"
    const val BRANCH: String = "@branch@"

    /**
     * The location of Atom's configuration files.
     *
     * You can provide it as environment variable on launch, otherwise
     * the default one (current working directory) will be used.
     */
    @JvmStatic
    val location: Path = Path.of(System.getProperty("config.location", "."))

    /**
     * The default server scope for Kotlin's Coroutine
     */
    internal val scope = minecraftScope()

    /**
     * The configuration of this server.
     */
    lateinit var configuration: Configuration
        private set

    /**
     * The (logger) of the server.
     */
    val logger = ComponentLogger.logger(Atom.javaClass)

    /**
     * The MinecraftServer instance.
     * @see MinecraftServer
     */
    lateinit var server: MinecraftServer
        private set

    private lateinit var _terminal: Thread

    fun init() {
        configuration = FileManager.toml(Configuration()) {
            path = location
            name = "atom"
        }

        with(configuration) {
            System.setProperty("minestom.tps", "$tps")
            System.setProperty("minestom.chunk-view-distance", "$renderDistance")
            System.setProperty("minestom.entity-view-distance", "$simulationDistance")
        }

        server = MinecraftServer.init()

        MinecraftServer.setDifficulty(configuration.difficulty)

        MinecraftServer.setTerminalEnabled(false)

        Doper.init()
    }

    fun launch() {
        if (configuration.network.lan)
            OpenToLAN.open()

        if (configuration.optifine)
            OptifineSupport.enable()

        if (configuration.onlineMode)
            MojangAuth.init()

        with(configuration.proxy) {
            when (type) {
                Forwarder.OFF -> Unit
                Forwarder.BUNGEECORD -> BungeeCordProxy.enable()
                Forwarder.VELOCITY -> VelocityProxy.enable(secret)
            }
        }

        MinecraftServer.getCommandManager().register(Shutdown)
        MinecraftServer.getCommandManager().register(Test)

        with(configuration.network) {
            MinecraftServer.setCompressionThreshold(threshold)

            server.start(address, port)
        }

        Doper.run()

        _terminal = thread(start = true, isDaemon = true, name = "Atom(ION)") {
            Ion.init()
            Ion.launch()
        }

        MinecraftServer.getConnectionManager().onlinePlayers
            .forEach(Player::refreshCommands)
    }

    fun shutdown() {
        logger.info("Shutting down...")

        if (Ion.running)
            Ion.shutdown()

        scope.coroutineContext.cancelChildren()
        scope.cancel()

        MinecraftServer.stopCleanly()
    }
}
