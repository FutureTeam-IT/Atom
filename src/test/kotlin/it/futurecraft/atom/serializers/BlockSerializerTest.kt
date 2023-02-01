package it.futurecraft.atom.serializers

import com.akuleshov7.ktoml.Toml
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import net.minestom.server.instance.block.Block
import kotlin.test.Test
import kotlin.test.assertEquals

internal class BlockSerializerTest {

    @Serializable
    data class BlockContainer(
        @Serializable(with = BlockSerializer::class)
        val block: Block
    )

    @Test
    fun serialize() {
        val block = BlockContainer(Block.STONE)
        val string = Toml.encodeToString(block);

        assertEquals(string, "block = \"minecraft:stone\"")
    }

    @Test
    fun deserializeNamespaced() {
        val string = "block = 'minecraft:stone'"
        val block = Toml.decodeFromString<BlockContainer>(string)

        assertEquals(block.block, Block.STONE)
    }

    @Test
    fun deserializeNoNamespace() {
        val string = "block = 'stone'"
        val block = Toml.decodeFromString<BlockContainer>(string)

        assertEquals(block.block, Block.STONE)
    }
}