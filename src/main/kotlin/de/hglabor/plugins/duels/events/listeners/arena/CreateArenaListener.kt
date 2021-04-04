package de.hglabor.plugins.duels.events.listeners.arena

import de.hglabor.plugins.duels.arenas.*
import de.hglabor.plugins.duels.utils.sendMsg
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.events.isLeftClick
import net.axay.kspigot.extensions.events.isRightClick
import net.axay.kspigot.utils.hasMark
import org.bukkit.Material
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

object CreateArenaListener : Listener {

    init {
       listen<PlayerInteractEvent> {
            if (arenaFromPlayer.containsKey(it.player)) {
                val player = it.player
                val arena = arenaFromPlayer[player]
                if (it.clickedBlock?.type != Material.AIR && it.clickedBlock != null && player.inventory.itemInMainHand.type != Material.AIR) {
                    val loc = it.clickedBlock!!.location
                    if (it.action.isLeftClick) {
                        if (player.inventory.itemInMainHand.hasMark("corner")) {
                            arena!!.setCorner(Locations.LocationOne, loc)
                            player.sendMsg("arena.creation.setCorner.1", mutableMapOf("x" to loc.x.toString(), "y" to loc.y.toString(), "z" to loc.z.toString()))
                            it.isCancelled = true
                        } else if (player.inventory.itemInMainHand.hasMark("spawn")) {
                            arena!!.setSpawn(Spawn.SpawnOne, loc)
                            player.sendMsg("arena.creation.setSpawn.1", mutableMapOf("x" to loc.x.toString(), "y" to loc.y.toString(), "z" to loc.z.toString()))
                            it.isCancelled = true
                        }
                    }

                    if (it.action.isRightClick) {
                        if (player.inventory.itemInMainHand.hasMark("corner")) {
                            if (it.hand!! == EquipmentSlot.OFF_HAND) return@listen
                            arena!!.setCorner(Locations.LocationTwo, loc)
                            player.sendMsg("arena.creation.setCorner.2", mutableMapOf("x" to loc.x.toString(), "y" to loc.y.toString(), "z" to loc.z.toString()))
                            it.isCancelled = true
                        } else if (player.inventory.itemInMainHand.hasMark("spawn")) {
                            if (it.hand!! == EquipmentSlot.OFF_HAND) return@listen
                            arena!!.setSpawn(Spawn.SpawnTwo, loc)
                            arena.world = loc.world
                            player.sendMsg("arena.creation.setSpawn.2", mutableMapOf("x" to loc.x.toString(), "y" to loc.y.toString(), "z" to loc.z.toString()))
                            it.isCancelled = true
                        }
                    }
                }
            }
        }
    }
}