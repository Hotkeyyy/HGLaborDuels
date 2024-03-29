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
import net.axay.kspigot.extensions.events.isRightClick
import net.axay.kspigot.gui.openGUI
import net.axay.kspigot.runnables.async
import net.axay.kspigot.utils.hasMark
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

object OnInteractWithItem {
    fun enable() {
        listen<PlayerInteractEvent> {
            if(it.hand == EquipmentSlot.OFF_HAND) return@listen

            if (it.action.isRightClick) {
                val item = it.player.inventory.itemInMainHand
                // Staff things
                if (item.hasMark("stopspec")) {
                    it.isCancelled = true
                    Data.duelFromSpec[it.player]?.removeSpectator(it.player, true)
                }
                if (item.hasMark("createarenaitem")) {
                    it.isCancelled = true
                    it.player.performCommand("arena create")
                }

                // Main Inventory things
                if (item.hasMark("settings")) {
                    it.isCancelled = true
                    PlayerSettingsGUI.open(it.player)
                }

                if (item.hasMark("queue")) {
                    it.isCancelled = true
                    it.player.openGUI(QueueGUI.gui)
                }

                if (item.hasMark("createparty")) {
                    it.isCancelled = true
                    Party(it.player).create(true)
                }

                // Party Inventory things

                if (item.hasMark("partygame")) {
                    it.isCancelled = true
                    if (Party.get(it.player)?.players?.size!! > 1)
                        PartyGameGUI.open(it.player)
                    else
                        it.player.sendLocalizedMessage("${Localization.PARTY_PREFIX}${KColors.TOMATO}Dafür sind nicht genügend Spieler in der Party.",
                            "${Localization.PARTY_PREFIX}${KColors.TOMATO}There aren't enough players in your party.")
                }

                if (item.hasMark("partyinfo")) {
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