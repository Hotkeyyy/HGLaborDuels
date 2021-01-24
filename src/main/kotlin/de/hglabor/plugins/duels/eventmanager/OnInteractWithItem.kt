package de.hglabor.plugins.duels.eventmanager

import de.hglabor.plugins.duels.functionality.PartyInventory
import de.hglabor.plugins.duels.guis.PartyGameGUI
import de.hglabor.plugins.duels.guis.PlayerSettingsGUI
import de.hglabor.plugins.duels.guis.QueueGUI
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.party.Party
import de.hglabor.plugins.duels.protection.Protection.isRestricted
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
import de.hglabor.plugins.staff.utils.StaffData.isInStaffMode
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.getHandItem
import net.axay.kspigot.extensions.events.isRightClick
import net.axay.kspigot.runnables.async
import net.axay.kspigot.utils.hasMark
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

object OnInteractWithItem {
    fun enable() {
        listen<PlayerInteractEvent> {
            if (it.action.isRightClick) {

                // Staff things
                if (it.player.getHandItem(EquipmentSlot.HAND)?.hasMark("stopspec")!!) {
                    it.isCancelled = true
                    Data.duelFromSpec[it.player]?.removeSpectator(it.player, true, true)
                }
                if (it.player.getHandItem(EquipmentSlot.HAND)?.hasMark("createarenaitem")!!) {
                    it.isCancelled = true
                    it.player.performCommand("arena create")
                }

                // Main Inventory things
                if (it.player.getHandItem(EquipmentSlot.HAND)?.hasMark("settings")!!) {
                    it.isCancelled = true
                    PlayerSettingsGUI.open(it.player)
                }

                if (it.player.getHandItem(EquipmentSlot.HAND)?.hasMark("queue")!!) {
                    it.isCancelled = true
                    QueueGUI.open(it.player)
                }

                if (it.player.getHandItem(EquipmentSlot.HAND)?.hasMark("createparty")!!) {
                    it.isCancelled = true
                    Party(it.player).create(true)
                    async { PartyInventory.giveItems(it.player) }
                }

                // Party Inventory things

                if (it.player.getHandItem(EquipmentSlot.HAND)?.hasMark("partygame")!!) {
                    it.isCancelled = true
                    if (Party.get(it.player)?.players?.size!! > 1)
                        PartyGameGUI.open(it.player)
                    else
                        it.player.sendLocalizedMessage("${Localization.PARTY_PREFIX}${KColors.TOMATO}Dafür sind nicht genügend Spieler in der Party.",
                            "${Localization.PARTY_PREFIX}${KColors.TOMATO}There aren't enough players in your party.")
                }

                if (it.player.getHandItem(EquipmentSlot.HAND)?.hasMark("partyinfo")!!) {
                    it.isCancelled = true
                    Party.get(it.player)?.sendInfo(it.player)
                }
            }

            if (it.player.isInStaffMode)
                it.isCancelled = true

            if (it.player.world.name == "world")
                if (it.player.gameMode.isRestricted)
                    it.isCancelled = true
        }
    }
}