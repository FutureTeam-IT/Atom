package it.futurecraft.atom.serializers

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import net.minestom.server.instance.block.Block

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Block::class)
class BlockSerializer : KSerializer<Block> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(
            "net.minestom.server.instance.block.Block",
            PrimitiveKind.STRING
        )

    override fun deserialize(decoder: Decoder): Block =
        Block.fromNamespaceId(decoder.decodeString()) ?: error("Invalid block ID.")


    override fun serialize(encoder: Encoder, value: Block) =
        encoder.encodeString("${value.namespace()}")
}