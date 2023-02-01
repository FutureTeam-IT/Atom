package it.futurecraft.atom.serializers

import com.akuleshov7.ktoml.Toml
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Vec
import kotlin.test.Test
import kotlin.test.assertEquals

class PointSerializerTest {
    @Serializable
    data class PointContainer(
        @Serializable(with = PosSerializer::class)
        val point: Point = Vec(.0, .0, .0)
    )

    val container = PointContainer()

    @Test
    fun serialize() {
        val string = Toml.encodeToString(container)
        println(string)
    }

    @Test
    fun deserialize() {
        val string = "[point]\n    x = 0.0\n    y = 0.0\n    z = 0.0"
        val decoded = Toml.decodeFromString<PointContainer>(string)

        assertEquals(decoded, container)
    }
}