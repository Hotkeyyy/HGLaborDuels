package de.hglabor.plugins.staff.eventmanager

import de.hglabor.plugins.staff.Staffmode
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.getHandItem
import net.axay.kspigot.utils.hasMark
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

object StaffOnInteract {
    fun enable() {
        listen<PlayerInteractAtEntityEvent> {
            val player = it.player
            if (it.rightClicked is Player) {
                if (player.getHandItem(it.hand)?.hasMark("invseeitem") == true) {
                    val t = it.rightClicked as Player
                    player.openInventory(t.inventory)
                }
            }
        }

        listen<PlayerInteractEvent> {
            val player = it.player
            if (it.action == Action.RIGHT_CLICK_AIR) {
                if (it.hand == EquipmentSlot.HAND) {
                    if (player.getHandItem(it.hand)?.hasMark("unvanishitem") == true) {
                        Staffmode.toggleVanish(player)

                    } else
                    if (player.getHandItem(it.hand)?.hasMark("vanishitem") == true) {
                        Staffmode.toggleVanish(player)
                    }
                }
            }
        }
    }
}
