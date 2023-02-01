package it.futurecraft.atom.instance

import it.futurecraft.atom.instance.generators.Dopant
import it.futurecraft.atom.serializers.PosSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minestom.server.coordinate.Pos

/**
 * An electron is a wrapper for InstanceContainers
 * @see InstanceContainer
 */
@Serializable
data class Electron(
    val name: String,

    @SerialName("spawning-instance")
    val isSpawningInstance: Boolean = true,

    @Serializable(with = PosSerializer::class)
    val spawn: Pos = Pos(.0, 1.0, .0),

    @SerialName("generator")
    val dopant: Dopant,
)
