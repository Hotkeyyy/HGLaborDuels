package de.hglabor.plugins.staff.eventmanager

import de.hglabor.plugins.staff.utils.StaffData.isInStaffMode
import net.axay.kspigot.event.listen
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

object StaffOnInventoryClick {
    fun enable() {
        listen<InventoryClickEvent> {
            if((it.whoClicked as Player).isInStaffMode) {
                it.isCancelled = true
            }
        }
    }
}