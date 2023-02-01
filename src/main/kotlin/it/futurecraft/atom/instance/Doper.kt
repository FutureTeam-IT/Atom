package it.futurecraft.atom.instance

import it.futurecraft.atom.Atom
import it.futurecraft.atom.instance.generators.Flat
import it.futurecraft.atom.instance.generators.Layer
import it.futurecraft.atom.instance.generators.Void
import it.futurecraft.atom.io.FileManager
import it.futurecraft.atom.io.Format
import it.futurecraft.atom.io.JSON
import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.instance.InstanceManager
import net.minestom.server.instance.block.Block

val default = Electron(
    name = "world",
    dopant = Flat(
        listOf(
            Layer(Block.BEDROCK, 1),
            Layer(Block.DIRT, 2),
            Layer(Block.GRASS_BLOCK, 1)
        )
    )
)


object Doper {
    private lateinit var _instanceManager: InstanceManager

    // Now oly works for one instance, due to ktoml not supporting table arrays,
    // if you change serializer you can add support for multiple instances
    private val electrons = mutableListOf<Electron>()

    fun init() {
        _instanceManager = MinecraftServer.getInstanceManager()

        val electron = FileManager.json(default) {
            path = Atom.location
            name = "electrons"
            format = Format.JSON
        }

        electrons.add(electron)
    }

    fun run() {
        electrons.forEach { electron ->
            val instance = _instanceManager.createInstanceContainer()

            if (electron.dopant is Void) {
                instance.setGenerator(null)
                instance.setBlock(electron.spawn.sub(.0, 1.0, .0), electron.dopant.block)
            } else {
                instance.setGenerator(electron.dopant::dope)
                instance.enableAutoChunkLoad(true)
            }

            val node = MinecraftServer.getGlobalEventHandler()
            if (electron.isSpawningInstance) {
                node.addListener(PlayerLoginEvent::class.java) {
                    it.setSpawningInstance(instance)
                    it.player.respawnPoint = electron.spawn
                }
            }

            _instanceManager.registerInstance(instance)
        }
    }
}
