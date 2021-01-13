package de.hglabor.plugins.duels.eventmanager

import de.hglabor.plugins.duels.protection.Protection.isRestricted
import de.hglabor.plugins.duels.utils.PlayerFunctions.stopSpectating
import de.hglabor.plugins.staff.utils.StaffData.isInStaffMode
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.getHandItem
import net.axay.kspigot.extensions.events.isRightClick
import net.axay.kspigot.utils.hasMark
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

object OnInteractWithItem {
    fun enable() {
        listen<PlayerInteractEvent> {
            if (it.player.getHandItem(EquipmentSlot.HAND)?.hasMark("stopspec")!!)
                it.player.stopSpectating(false)


            if (it.player.getHandItem(EquipmentSlot.HAND)?.hasMark("createarenaitem")!!) {
                it.isCancelled = true
                if (it.action.isRightClick)
                    it.player.performCommand("arena create")
            }

            if (it.player.getHandItem(EquipmentSlot.HAND)?.hasMark("settings")!!) {
                if (it.player.isOp)

                it.player.sendMessage("${KColors.TOMATO}Not working yet")
            }


                if (it.player.isInStaffMode)
                    it.isCancelled = true

            if (it.player.world.name == "world")
                if (it.player.gameMode.isRestricted)
                    it.isCancelled = true
        }
    }
}