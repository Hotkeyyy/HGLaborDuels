package de.hglabor.plugins.duels.arenas

import com.boydti.fawe.FaweAPI
import com.boydti.fawe.`object`.RelightMode
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitWorld
import com.sk89q.worldedit.extent.clipboard.Clipboard
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat
import com.sk89q.worldedit.function.operation.Operations
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.math.Vector3
import com.sk89q.worldedit.regions.CuboidRegion
import com.sk89q.worldedit.session.ClipboardHolder
import de.hglabor.plugins.duels.utils.Data
import net.axay.kspigot.runnables.taskRunLater
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.FileInputStream


class Arena(var loc: Pair<Int, Int>, val arenaName: String) {

    companion object {
        fun getInfo(arenaName: String): Pair<ArenaTags, Clipboard> {
            return Arenas.allArenas[arenaName]!!
        }
    }
    val file = File("arenas//arenas.yml")
    val yamlConfiguration = YamlConfiguration.loadConfiguration(file)

    var bukkitWorld: BukkitWorld
    val world: World

    var spawn1Loc: Location
    var spawn2Loc: Location

    init {
        /*if (Arenas.getArenaInfos(arenaName).first == ArenaTags.NETHER) {
            world = Bukkit.getWorld("NetherFightWorld")!!
            bukkitWorld = BukkitWorld(Bukkit.getWorld("NetherFightWorld"))
        } else {*/
        world = Bukkit.getWorld("FightWorld")!!
        bukkitWorld = BukkitWorld(world)

        spawn1Loc = Location(world, yamlConfiguration["${arenaName}.spawns.spawnOne.x"] as Double,
            yamlConfiguration["${arenaName}.spawns.spawnOne.y"] as Double + 2,
            yamlConfiguration["${arenaName}.spawns.spawnOne.z"] as Double)
        spawn2Loc = Location(world,
            yamlConfiguration["${arenaName}.spawns.spawnTwo.x"] as Double,
            yamlConfiguration["${arenaName}.spawns.spawnTwo.y"] as Double + 2,
            yamlConfiguration["${arenaName}.spawns.spawnTwo.z"] as Double)
        spawn1Loc = Location(world, loc.first * Data.locationMultiplier, 100.0, loc.second * Data.locationMultiplier).subtract(spawn1Loc).add(0.0, 2.0, 0.0)
        spawn2Loc = Location(world, loc.first * Data.locationMultiplier, 100.0, loc.second * Data.locationMultiplier).subtract(spawn2Loc).add(0.0, 2.0, 0.0)
        Data.usedLocationMultipliersXZ.add(loc)
    }

    fun pasteSchematic() {
        WorldEdit.getInstance().editSessionFactory.getEditSession(bukkitWorld, -1).use { editSession ->
            val operation = ClipboardHolder(Arenas.allArenas[arenaName]?.second)
                .createPaste(editSession)
                .to(BlockVector3.at(loc.first * Data.locationMultiplier, 100.0, loc.second * Data.locationMultiplier))
                .ignoreAirBlocks(true)
                .copyEntities(true)
                .build()
            Operations.complete(operation)
        }

        val v1: BlockVector3 =
            Vector3.at(loc.first * Data.locationMultiplier, 100.0, loc.second * Data.locationMultiplier).toBlockPoint()
        val v2: BlockVector3 =
            Vector3.at(loc.first * Data.locationMultiplier + getInfo(arenaName).second.dimensions?.x!!,
                100.0 + getInfo(arenaName).second.dimensions?.y!!, loc.second * Data.locationMultiplier + getInfo(arenaName).second.dimensions?.z!!).toBlockPoint()
        val region = CuboidRegion(v1, v2)
        FaweAPI.fixLighting(bukkitWorld, region, null, RelightMode.OPTIMAL)
        FaweAPI.fixLighting(bukkitWorld, region, null, RelightMode.ALL)
    }


    fun removeSchematic() {
        val v1: BlockVector3 =
            Vector3.at(loc.first * Data.locationMultiplier, 100.0, loc.second * Data.locationMultiplier).toBlockPoint()
        val v2: BlockVector3 =
            Vector3.at(loc.first * Data.locationMultiplier + getInfo(arenaName).second.dimensions?.x!!,
                100.0 + getInfo(arenaName).second.dimensions?.y!!, loc.second * Data.locationMultiplier + getInfo(arenaName).second.dimensions?.z!!).toBlockPoint()
        val region = CuboidRegion(bukkitWorld, v1, v2)

        for (bv: BlockVector3 in region) {
            val x = bv.blockX
            val y = bv.blockY
            val z = bv.blockZ
            val block = Location(world, x.toDouble(), y.toDouble(), z.toDouble()).block
            if (block.type != Material.AIR) {
                block.type = Material.AIR
            }
        }

        taskRunLater(400L, false) { Data.usedLocationMultipliersXZ.remove(loc) }
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