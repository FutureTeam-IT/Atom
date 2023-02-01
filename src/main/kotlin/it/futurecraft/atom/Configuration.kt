package it.futurecraft.atom

import com.akuleshov7.ktoml.annotations.TomlComments
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minestom.server.world.Difficulty

@Serializable
data class Configuration(
    @SerialName("ticks-per-second")
    val tps: Long = 20,

    @SerialName("online-mode")
    val onlineMode: Boolean = true,

    @SerialName("render-distance")
    val renderDistance: Int = 8,

    @SerialName("simulation-distance")
    val simulationDistance: Int = 8,

    @SerialName("optifine-support")
    val optifine: Boolean = true,

    val difficulty: Difficulty = Difficulty.NORMAL,

    val network: Network = Network(),

    val proxy: Proxy = Proxy()
)

@Serializable
data class Network(
    val address: String = "127.0.0.1",

    val port: Int = 25565,

    @SerialName("open-to-lan")
    val lan: Boolean = false,

    @SerialName("compression-threshold")
    val threshold: Int = 256
)

@Serializable
enum class Forwarder {
    OFF, BUNGEECORD, VELOCITY
}

@Serializable
data class Proxy(
    val type: Forwarder = Forwarder.OFF,

    val secret: String = ""
)
