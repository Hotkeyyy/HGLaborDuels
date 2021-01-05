package de.hglabor.plugins.staff.eventmanager

import de.hglabor.plugins.staff.utils.StaffData.isInStaffMode
import net.axay.kspigot.event.listen
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerDropItemEvent

object StaffOnItemDrop {
    fun enable() {
        listen<PlayerDropItemEvent>(EventPriority.HIGHEST) {
            if (it.player.isInStaffMode)
                it.isCancelled = true
        }
    }
}