package de.hglabor.plugins.duels.arenas

import com.sk89q.worldedit.bukkit.BukkitWorld
import com.sk89q.worldedit.bukkit.WorldEditPlugin
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat
import com.sk89q.worldedit.function.operation.ForwardExtentCopy
import com.sk89q.worldedit.function.operation.Operations
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.math.Vector3
import com.sk89q.worldedit.regions.CuboidRegion
import com.sk89q.worldedit.util.io.Closer
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter
import com.sk89q.worldedit.WorldEdit

import com.sk89q.worldedit.EditSession
import de.hglabor.plugins.duels.localization.sendMsg


val arenaFromPlayer = HashMap<Player, CreateArena>()

class CreateArena(val creator: Player) {
    var name = ""
    var tag = ArenaTags.NONE
    var world: World? = null
    var corner1: Location? = null
    var corner2: Location? = null
    var spawn1: Location? = null
    var spawn2: Location? = null

    fun setCorner(loc: Locations, location: Location) {
        if (loc == Locations.LocationOne)
            corner1 = location
        if (loc == Locations.LocationTwo)
            corner2 = location
    }

    fun setSpawn(spawn: Spawn, location: Location) {
        if (spawn == Spawn.SpawnOne)
            spawn1 = location.add(0.5, 1.0, 0.5)
        if (spawn == Spawn.SpawnTwo)
            spawn2 = location.add(0.5, 1.0, 0.5)
    }

    fun name(string: String) {
        name = string.toLowerCase().capitalize().trim()
    }


    fun save() {
        if (name == "" || world == null || corner1 == null || corner2 == null || spawn1 == null || spawn2 == null) {
            creator.sendMsg("arena.creation.failed.valueMissing")
            return
        }

        val schemfile = File("arenas//$name.schematic")
        creator.sendMsg("arena.creation.saving")

        if (schemfile.exists()) {
            creator.sendMsg("arena.creation.failed.alreadyExisting")
        }

        // Position1 as NorthWest
        val newPosLocs = Locations.relocate(corner1!!, corner2!!)
        corner1 = newPosLocs.first
        corner2 = newPosLocs.second

        // SET IN arenas.yml
        val file = File("arenas//arenas.yml")
        val yamlConfiguration = YamlConfiguration.loadConfiguration(file)

        yamlConfiguration[name]
        yamlConfiguration["$name.creator"] = creator.name
        yamlConfiguration["$name.tag"] = tag.toString()
        yamlConfiguration["$name.spawns"]
        yamlConfiguration["$name.spawns.spawnOne"]
        yamlConfiguration["$name.spawns.spawnOne.x"] = spawn1?.x?.let { corner1?.x?.minus(it) }
        yamlConfiguration["$name.spawns.spawnOne.y"] = spawn1?.y?.let { corner1?.y?.minus(it) }
        yamlConfiguration["$name.spawns.spawnOne.z"] = spawn1?.z?.let { corner1?.z?.minus(it) }
        yamlConfiguration["$name.spawns.spawnTwo"]
        yamlConfiguration["$name.spawns.spawnTwo.x"] = spawn2?.x?.let { corner1?.x?.minus(it) }
        yamlConfiguration["$name.spawns.spawnTwo.y"] = spawn2?.y?.let { corner1?.y?.minus(it) }
        yamlConfiguration["$name.spawns.spawnTwo.z"] = spawn2?.z?.let { corner1?.z?.minus(it) }

        try {
            yamlConfiguration.save(file)
        } catch (e: IOException) {
            e.printStackTrace()
            creator.sendMsg("arena.creation.failed.file")
            return
        }

        // SAVE SCHEMATIC
        val weWorld = BukkitWorld(world)
        val pos1: BlockVector3 = Vector3.at(corner2!!.x, corner2!!.y, corner2!!.z).toBlockPoint()
        val pos2: BlockVector3 = Vector3.at(corner1!!.x, corner1!!.y, corner1!!.z).toBlockPoint()
        val region = CuboidRegion(weWorld, pos1, pos2)
        val clipboard = BlockArrayClipboard(region)

        try {
            WorldEdit.getInstance().editSessionFactory.getEditSession(weWorld, -1).use { editSession ->
                val forwardExtentCopy = ForwardExtentCopy(editSession, region, clipboard, region.minimumPoint)
                forwardExtentCopy.isCopyingBiomes = true
                forwardExtentCopy.isRemovingEntities = true
                Operations.complete(forwardExtentCopy)
            }

            val schematicFile = File("arenas//$name.schematic")
            BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(FileOutputStream(schematicFile)).use { writer ->
                writer.write(clipboard)
            }

        } catch (e: IOException) {
            e.printStackTrace()
            creator.sendMsg("arena.creation.failed.schematic")
            return
        } finally {
            try {
                creator.sendMsg("arena.creation.success")
                arenaFromPlayer.remove(creator)
                Arenas.allArenas[name] = Pair(tag, Arenas.getClipboard(name))
            } catch (ignore: IOException) {
            }
        }
    }
}

enum class Spawn { SpawnOne, SpawnTwo }
enum class Locations {
    LocationOne, LocationTwo;

    companion object {
        fun relocate(loc1: Location, loc2: Location): Pair<Location, Location> {
            val world = loc1.world
            val locOne: Location
            val locTwo: Location

            val x1: Double
            val x2: Double
            val y1: Double
            val y2: Double
            val z1: Double
            val z2: Double

            if (loc1.x < loc2.x) {
                x1 = loc1.x
                x2 = loc2.x
            } else {
                x1 = loc2.x
                x2 = loc1.x
            }

            if (loc1.y < loc2.y) {
                y1 = loc1.y
                y2 = loc2.y
            } else {
                y1 = loc2.y
                y2 = loc1.y
            }

            if (loc1.z < loc2.z) {
                z1 = loc1.z
                z2 = loc2.z
            } else {
                z1 = loc2.z
                z2 = loc1.z
            }

            locOne = Location(world, x1, y1, z1)
            locTwo = Location(world, x2, y2, z2)

            return locOne to locTwo
        }
    }
}