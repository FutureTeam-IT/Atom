package it.futurecraft.atom.instance.generators

import it.futurecraft.atom.serializers.BlockSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit

@Serializable
@SerialName("void")
data class Void (
    @Serializable(with = BlockSerializer::class)
    val block: Block = Block.RED_CONCRETE
) : Dopant {
    override fun dope(unit: GenerationUnit) {}
}