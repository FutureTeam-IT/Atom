package it.futurecraft.atom.instance.generators

import it.futurecraft.atom.serializers.BlockSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit

@Serializable
@SerialName("flat")
data class Flat(val layers: List<Layer>) : Dopant {
    override fun dope(unit: GenerationUnit) {
        var y = 0

        layers.forEach {
            unit.modifier().fillHeight(y, y + it.height, it.block)
            y += it.height
        }
    }
}

@Serializable
data class Layer(
    @Serializable(with = BlockSerializer::class)
    val block: Block,
    val height: Int
)
