package de.hglabor.plugins.duels.arenas

import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitWorld
import com.sk89q.worldedit.extent.clipboard.Clipboard
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat
import com.sk89q.worldedit.function.mask.Mask
import com.sk89q.worldedit.function.operation.Operations
import com.sk89q.worldedit.function.pattern.Pattern
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.math.Vector3
import com.sk89q.worldedit.regions.CuboidRegion
import com.sk89q.worldedit.session.ClipboardHolder
import com.sk89q.worldedit.world.block.BaseBlock
import com.sk89q.worldedit.world.block.BlockType
import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.utils.Data
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.io.FileInputStream

class Arena(var loc: Pair<Int, Int>, val arenaName: String) {
    val file = File("arenas//arenas.yml")
    val yamlConfiguration = YamlConfiguration.loadConfiguration(file)

    var spawn1Loc: Location
    var spawn2Loc: Location

    init {
        spawn1Loc = Location(Bukkit.getWorld("FightWorld"),
            yamlConfiguration["${arenaName}.spawns.spawnOne.x"] as Double,
            yamlConfiguration["${arenaName}.spawns.spawnOne.y"] as Double + 2,
            yamlConfiguration["${arenaName}.spawns.spawnOne.z"] as Double)
        spawn2Loc = Location(Bukkit.getWorld("FightWorld"),
            yamlConfiguration["${arenaName}.spawns.spawnTwo.x"] as Double,
            yamlConfiguration["${arenaName}.spawns.spawnTwo.y"] as Double + 2,
            yamlConfiguration["${arenaName}.spawns.spawnTwo.z"] as Double)
        spawn1Loc =
            Location(Bukkit.getWorld("FightWorld"),
                loc.first * Data.locationMultiplier,
                100.0,
                loc.second * Data.locationMultiplier).subtract(spawn1Loc).add(0.0, 2.0, 0.0)
        spawn2Loc =
            Location(Bukkit.getWorld("FightWorld"),
                loc.first * Data.locationMultiplier,
                100.0,
                loc.second * Data.locationMultiplier).subtract(spawn2Loc).add(0.0, 2.0, 0.0)
        Data.usedLocationMultipliersXZ.add(loc)
    }

    fun pasteSchematic() {
        WorldEdit.getInstance().editSessionFactory.getEditSession(
            BukkitWorld(Bukkit.getWorld("FightWorld")), -1).use { editSession ->
            val operation = ClipboardHolder(Arenas.allArenas[arenaName]?.second)
                .createPaste(editSession)
                .to(BlockVector3.at(loc.first * Data.locationMultiplier, 100.0, loc.second * Data.locationMultiplier))
                .ignoreAirBlocks(true)
                .copyEntities(true)
                .build()
            Operations.complete(operation)
        }
    }


    fun removeSchematic() {
        val v1: BlockVector3 =
            Vector3.at(loc.first * Data.locationMultiplier, 95.0, loc.second * Data.locationMultiplier).toBlockPoint()
        val v2: BlockVector3 =
            Vector3.at(loc.first * Data.locationMultiplier + 150, 95.0 + 75, loc.second * Data.locationMultiplier + 150)
                .toBlockPoint()
        val region = CuboidRegion(BukkitWorld(Bukkit.getWorld("FightWorld")), v1, v2)
        for (bv: BlockVector3 in region) {
            val x = bv.blockX
            val y = bv.blockY
            val z = bv.blockZ
            val block = Location(Bukkit.getWorld("FightWorld"), x.toDouble(), y.toDouble(), z.toDouble()).block
            if (block.type != Material.AIR) {
                block.type = Material.AIR
            }
        }
        //Data.usedLocationMultipliersXZ.remove(loc)
    }
}

object Arenas {
    val allArenas = hashMapOf<String, Pair<ArenaTags, Clipboard>>()

    fun enable() {
        val file = File("arenas//arenas.yml")
        val yamlConfiguration = YamlConfiguration.loadConfiguration(file)
        for (arena in yamlConfiguration.getKeys(false)) {
            allArenas[arena] = getArenaInfos(arena)
        }
    }

    fun getArenaInfos(arenaName: String): Pair<ArenaTags, Clipboard> {
        val file = File("arenas//arenas.yml")
        val yamlConfiguration = YamlConfiguration.loadConfiguration(file)
        return Pair(enumValueOf(yamlConfiguration["$arenaName.tag"] as String), getClipboard(arenaName))
    }

    fun getClipboard(arenaName: String): Clipboard {
        val schemfile = File("arenas//$arenaName.schematic")
        val clipboard: Clipboard
        BuiltInClipboardFormat.SPONGE_SCHEMATIC.getReader(FileInputStream(schemfile)).use { reader ->
            clipboard = reader.read()
        }
        return clipboard
    }

    fun getRandomArena(arenaTag: ArenaTags): String {
        val arenas = arrayListOf<String>()
        allArenas.filter { it.value.first == arenaTag }.forEach { arenas.add(it.key) }
        if (arenas.isEmpty())
            return " "
        return arenas.random()
    }

    fun arenaWithTagExists(arenaTag: ArenaTags): Boolean {
        if (getRandomArena(arenaTag) != " ")
            return true
        return false
    }
}