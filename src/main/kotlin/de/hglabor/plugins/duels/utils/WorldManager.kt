package de.hglabor.plugins.duels.utils

import org.bukkit.*
import org.bukkit.block.Biome
import org.bukkit.generator.BlockPopulator
import org.bukkit.generator.ChunkGenerator
import java.io.File
import java.util.*


object WorldManager {
    fun createFightWorld() {
        val creator = WorldCreator("FightWorld")
        creator.generator(VoidGenerator())
        creator.generateStructures(false)
        creator.createWorld()
    }

    fun deleteFightWorld() {
        Bukkit.unloadWorld("FightWorld", false)
        File("FightWorld//").deleteRecursively()
    }

    fun createBuildWorld() {
        if (Bukkit.getWorld("BuildWorld") == null) {
            val creator = WorldCreator("BuildWorld")
            creator.generateStructures(false)
            creator.type(WorldType.FLAT)
            creator.createWorld()
        }
    }
    
    fun configureWorld(world: World) {
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false)
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true)
        world.setGameRule(GameRule.DISABLE_RAIDS, true)
        world.time = 0
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true)
        world.setGameRule(GameRule.DO_FIRE_TICK, false)
        world.isThundering = false
        world.setStorm(false)
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false)
        world.setGameRule(GameRule.MOB_GRIEFING, false)
        world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false)
        world.setGameRule(GameRule.DO_INSOMNIA, false)
        world.difficulty = Difficulty.NORMAL
    }
}

class VoidGenerator : ChunkGenerator() {
    override fun getDefaultPopulators(world: World): List<BlockPopulator> {
        return emptyList()
    }

    override fun generateChunkData(
        world: World,
        random: Random,
        chunkX: Int,
        chunkZ: Int,
        biome: BiomeGrid
    ): ChunkData {
        val chunkData = super.createChunkData(world)

        for (x in 0..15) {
            for (z in 0..15) {
                for (y in 0..15) {
                    biome.setBiome(x, y, z, Biome.PLAINS)
                }
            }
        }
        return chunkData
    }

    override fun canSpawn(world: World, x: Int, z: Int): Boolean {
        return true
    }

    override fun getFixedSpawnLocation(world: World, random: Random): Location? {
        return Location(world, 0.0, 128.0, 0.0)
    }
}

