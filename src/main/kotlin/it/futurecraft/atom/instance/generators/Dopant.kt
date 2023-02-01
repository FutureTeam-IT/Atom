package it.futurecraft.atom.instance.generators

import kotlinx.serialization.Serializable
import net.minestom.server.instance.generator.GenerationUnit

/**
 * The chunk generator for an instance.
 *
 * It is named after the doping technique for semiconductors where electrons are manually introduced.
 */
@Serializable
sealed interface Dopant {
    fun dope(unit: GenerationUnit)
}
