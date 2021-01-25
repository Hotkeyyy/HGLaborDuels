package de.hglabor.plugins.duels.eventmanager

import de.hglabor.plugins.duels.arenas.setName
import de.hglabor.plugins.duels.data.PlayerSettings
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.staff.utils.StaffData.isStaff
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.async
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.player.AsyncPlayerChatEvent

object OnPlayerChat {
    fun enable() {
        listen<AsyncPlayerChatEvent> {
            if (!setName.contains(it.player)) {
                if (it.isCancelled) return@listen
                it.isCancelled = true

                val player = it.player
                var finalMessage = it.message

                if (player.isStaff)
                    finalMessage = finalMessage.replace("&", "§")

                val recievers = arrayListOf<CommandSender>(Bukkit.getConsoleSender())

                async {
                    onlinePlayers.forEach { players ->
                        if (recievesMessage(player, players)) {
                            recievers.add(players)
                        }
                    }

                    recievers.forEach { reciever ->
                        reciever.sendMessage("${KColors.GRAY}${player.displayName} ${KColors.DARKGRAY}» ${KColors.WHITE}$finalMessage")
                    }
                }
            }
        }
    }

    fun recievesMessage(sender: Player, reciever: Player): Boolean {
        val settings = PlayerSettings.get(reciever)

        if (sender == reciever)
            return true

        if (reciever.isInFight()) {
            if (settings.chatInFight() == PlayerSettings.Companion.Chat.NONE)
                return false

            else if (settings.chatInFight() == PlayerSettings.Companion.Chat.FIGHT)
                if (sender.isInFight()) {
                    if (Data.duelFromPlayer(sender) != Data.duelFromPlayer(reciever))
                        return false
                }  else
                    return false
        }
        return true
    }
}