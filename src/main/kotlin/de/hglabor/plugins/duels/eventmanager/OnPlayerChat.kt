package de.hglabor.plugins.duels.eventmanager

import de.hglabor.plugins.duels.arenas.setName
import de.hglabor.plugins.duels.settings.Settings
import de.hglabor.plugins.duels.utils.PlayerFunctions.getEnemy
import de.hglabor.plugins.duels.utils.Ranks
import de.hglabor.plugins.staff.utils.StaffData.isStaff
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.extensions.onlinePlayers
import org.bukkit.Bukkit
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

                broadcast("${KColors.GRAY}─ ${Ranks.getRank(player).prefix}${Ranks.getRank(player).color}${player.displayName} ${KColors.DARKGRAY}» ${KColors.WHITE}$finalMessage")
            }
        }
    }
}