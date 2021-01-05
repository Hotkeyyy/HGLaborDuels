package de.hglabor.plugins.duels.eventmanager.arena

import de.hglabor.plugins.duels.arenas.*
import de.hglabor.plugins.duels.guis.CreateArenaGUI
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.events.isLeftClick
import net.axay.kspigot.extensions.events.isRightClick
import net.axay.kspigot.utils.hasMark
import org.bukkit.Material
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChatEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

object CreateArenaListener : Listener {

    fun enable() {
        listen<PlayerChatEvent> {
            val player = it.player
            if (arenaFromPlayer.containsKey(player)) {
                if (setName.contains(player)) {
                    it.isCancelled = true
                    val arena = arenaFromPlayer[it.player]
                    val name = it.message.trim()
                    arena!!.name(name)
                    if (name.equals("abbrechen", true) || name.equals("abort", true)) {
                        if (player.localization("de"))
                            player.sendMessage(Localization.ARENA_CREATION_LISTENER_ABORTED_SET_NAME_DE)
                        else
                            player.sendMessage(Localization.ARENA_CREATION_LISTENER_ABORTED_SET_NAME_EN)
                    } else {
                        if (player.localization("de"))
                            player.sendMessage(
                                Localization.ARENA_CREATION_LISTENER_SET_NAME_DE.replace(
                                    "%arenaName%", name))
                        else
                            player.sendMessage(
                                Localization.ARENA_CREATION_LISTENER_SET_NAME_EN.replace("%arenaName%",name))
                    }
                    setName.remove(player)
                    CreateArenaGUI.openCreateArenaGUI(player)
                }
            }
        }

        listen<PlayerInteractEvent> {
            if (arenaFromPlayer.containsKey(it.player)) {
                val player = it.player
                val arena = arenaFromPlayer[player]
                if (it.clickedBlock?.type != Material.AIR && it.clickedBlock != null && player.inventory.itemInMainHand.type != Material.AIR) {
                    val loc = it.clickedBlock!!.location
                    if (it.action.isLeftClick) {
                        if (player.inventory.itemInMainHand.hasMark("corner")) {
                            arena!!.setCorner(Locations.LocationOne, loc)
                            if (player.localization("de"))
                                player.sendMessage(Localization.ARENA_CREATION_LISTENER_SET_CORNER1_DE.replace("%x%", "${loc.x}").replace("%y%", "${loc.y}").replace("%z%", "${loc.z}"))
                            else
                                player.sendMessage(Localization.ARENA_CREATION_LISTENER_SET_CORNER1_EN.replace("%x%", "${loc.x}").replace("%y%", "${loc.y}").replace("%z%", "${loc.z}"))
                            it.isCancelled = true
                        } else if (player.inventory.itemInMainHand.hasMark("spawn")) {
                            arena!!.setSpawn(Spawn.SpawnOne, loc)
                                if (player.localization("de"))
                                    player.sendMessage(Localization.ARENA_CREATION_LISTENER_SET_SPAWN1_DE.replace("%x%", "${loc.x}").replace("%y%", "${loc.y}").replace("%z%", "${loc.z}"))
                                else
                                    player.sendMessage(Localization.ARENA_CREATION_LISTENER_SET_SPAWN1_EN.replace("%x%", "${loc.x}").replace("%y%", "${loc.y}").replace("%z%", "${loc.z}"))
                            it.isCancelled = true
                        }
                    }


                    if (it.action.isRightClick) {
                        if (player.inventory.itemInMainHand.hasMark("corner")) {
                            if (it.hand!! == EquipmentSlot.OFF_HAND) return@listen
                            arena!!.setCorner(Locations.LocationTwo, loc)
                            if (player.localization("de"))
                                player.sendMessage(Localization.ARENA_CREATION_LISTENER_SET_CORNER2_DE.replace("%x%", "${loc.x}").replace("%y%", "${loc.y}").replace("%z%", "${loc.z}"))
                            else
                                player.sendMessage(Localization.ARENA_CREATION_LISTENER_SET_CORNER2_EN.replace("%x%", "${loc.x}").replace("%y%", "${loc.y}").replace("%z%", "${loc.z}"))
                            it.isCancelled = true
                        } else if (player.inventory.itemInMainHand.hasMark("spawn")) {
                            if (it.hand!! == EquipmentSlot.OFF_HAND) return@listen
                            arena!!.setSpawn(Spawn.SpawnTwo, loc)
                            arena.world = loc.world
                            if (player.localization("de"))
                                player.sendMessage(Localization.ARENA_CREATION_LISTENER_SET_SPAWN2_DE.replace("%x%", "${loc.x}").replace("%y%", "${loc.y}").replace("%z%", "${loc.z}"))
                            else
                                player.sendMessage(Localization.ARENA_CREATION_LISTENER_SET_SPAWN2_EN.replace("%x%", "${loc.x}").replace("%y%", "${loc.y}").replace("%z%", "${loc.z}"))
                            it.isCancelled = true
                        }
                    }
                }
            }
        }
    }
}