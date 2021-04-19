package de.hglabor.plugins.duels.utils

import com.sk89q.worldedit.MaxChangedBlocksException
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.WorldEditException
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.bukkit.BukkitWorld
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard
import com.sk89q.worldedit.extent.clipboard.Clipboard
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat
import com.sk89q.worldedit.extent.clipboard.io.SpongeSchematicReader
import com.sk89q.worldedit.extent.clipboard.io.SpongeSchematicWriter
import com.sk89q.worldedit.function.operation.ForwardExtentCopy
import com.sk89q.worldedit.function.operation.Operation
import com.sk89q.worldedit.function.operation.Operations
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.regions.CuboidRegion
import com.sk89q.worldedit.session.ClipboardHolder
import com.sk89q.worldedit.world.World
import de.hglabor.plugins.duels.arenas.Arena
import de.hglabor.plugins.duels.arenas.Arenas
import org.bukkit.Bukkit
import org.bukkit.Location
import org.primesoft.asyncworldedit.api.IAsyncWorldEdit
import org.primesoft.asyncworldedit.api.utils.IFuncParamEx
import org.primesoft.asyncworldedit.api.worldedit.IAsyncEditSessionFactory
import org.primesoft.asyncworldedit.api.worldedit.ICancelabeEditSession
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

private val weWorld = BukkitWorld(Bukkit.getWorld("FightWorld"))

object SchematicUtils {
    fun saveSchematic(region: CuboidRegion, schematicName: String) {
        val clipboard = BlockArrayClipboard(region)
        val schematicFile = File("arenas//$schematicName.schem")
        BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(FileOutputStream(schematicFile)).use { writer ->
            writer.write(clipboard)
        }

        val threadSafeEditSession = (WorldEdit.getInstance().editSessionFactory as IAsyncEditSessionFactory).getThreadSafeEditSession(weWorld, -1)
        val forwardExtentCopy = ForwardExtentCopy(threadSafeEditSession, region, clipboard, region.minimumPoint)
        forwardExtentCopy.isCopyingEntities = false
        forwardExtentCopy.isRemovingEntities = false
        Operations.complete(forwardExtentCopy)
    }

    fun pasteSchematic(arena: Arena, location: Location) {
        val awe = Bukkit.getPluginManager().getPlugin("AsyncWorldEdit") as IAsyncWorldEdit
        val player = awe.playerManager.consolePlayer
        val tsSession = (WorldEdit.getInstance().editSessionFactory as IAsyncEditSessionFactory).getThreadSafeEditSession(BukkitWorld(location.world), -1, null, player)

        val action = PasteAction(arena.arenaName, location)
        awe.blockPlacer.performAsAsyncJob(tsSession, player, "loadWarGear:$arena.arenaName", action)
    }
}

class PasteAction(private val arenaName: String, to: Location?) :
    IFuncParamEx<Int, ICancelabeEditSession?, MaxChangedBlocksException?> {
    private val to: BlockVector3 = BukkitAdapter.asBlockVector(to)

    override fun execute(editSession: ICancelabeEditSession?): Int {
        (WorldEdit.getInstance().editSessionFactory as IAsyncEditSessionFactory).getEditSession(weWorld, -1).use { editSession ->
            val operation = ClipboardHolder(Arenas.allArenas[arenaName]?.second)
                .createPaste(editSession)
                .to(to)
                .ignoreAirBlocks(true)
                .copyEntities(false)
                .build()
            Operations.complete(operation)
        }

        /*try {
            val schematicFile = File("arenas//$arenaName.schem")
            val stream = FileInputStream(schematicFile)
            val reader = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getReader(stream)
            val clipboard: Clipboard = reader.read()
            val holder = ClipboardHolder(clipboard)
            val operation: Operation = holder
                .createPaste(editSession)
                .to(to)
                .ignoreAirBlocks(true)
                .copyEntities(false)
                .build()
            Operations.complete(operation)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: WorldEditException) {
            e.printStackTrace()
        }*/
        return 0
    }
}

