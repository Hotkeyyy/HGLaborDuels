package de.hglabor.plugins.duels.arenas

import com.sk89q.worldedit.bukkit.BukkitWorld
import com.sk89q.worldedit.extent.clipboard.Clipboard
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.regions.CuboidRegion
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.SchematicUtils
import net.axay.kspigot.extensions.bukkit.info
import net.axay.kspigot.extensions.console
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
    private val file = File("arenas//arenas.yml")
    private val yamlConfiguration = YamlConfiguration.loadConfiguration(file)

    var bukkitWorld: BukkitWorld
    val world: World = Bukkit.getWorld("FightWorld")!!

    var spawn1Loc: Location
    var spawn2Loc: Location

    var location: Location
    init {
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
        location = Location(world, loc.first * Data.locationMultiplier, 100.0, loc.second * Data.locationMultiplier)
        Data.usedLocationMultipliersXZ.add(loc)
    }

    fun pasteSchematic() {
        /*WorldEdit.getInstance().editSessionFactory.getEditSession(bukkitWorld, -1).use { editSession ->
            val operation = ClipboardHolder(Arenas.allArenas[arenaName]?.second)
                .createPaste(editSession)
                .to(BlockVector3.at(loc.first * Data.locationMultiplier, 100.0, loc.second * Data.locationMultiplier))
                .ignoreAirBlocks(true)
                .copyEntities(true)
                .build()
            Operations.complete(operation)
        }*/

        SchematicUtils.pasteSchematic(this, location)
    }


    fun removeSchematic() {
        console.info(arenaName)
        val v1 = BlockVector3.at(loc.first * Data.locationMultiplier - 10, 100.0, loc.second * Data.locationMultiplier - 10)
        val v2 = BlockVector3.at(loc.first * Data.locationMultiplier + (getInfo(arenaName).second.dimensions?.x!!) + 10,
            100.0 + getInfo(arenaName).second.dimensions?.y!!,
            loc.second * Data.locationMultiplier + (getInfo(arenaName).second.dimensions?.z!!) + 10)
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

    private fun getArenaInfos(arenaName: String): Pair<ArenaTags, Clipboard> {
        val file = File("arenas//arenas.yml")
        val yamlConfiguration = YamlConfiguration.loadConfiguration(file)
        return Pair(enumValueOf(yamlConfiguration["$arenaName.tag"] as String), getClipboard(arenaName))
    }

    fun getClipboard(arenaName: String): Clipboard {
        val schematicFile = File("arenas//$arenaName.schem")
        var clipboard: Clipboard
        val format = ClipboardFormats.findByFile(schematicFile)
        format!!.getReader(FileInputStream(schematicFile)).use { reader -> clipboard = reader.read() }
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