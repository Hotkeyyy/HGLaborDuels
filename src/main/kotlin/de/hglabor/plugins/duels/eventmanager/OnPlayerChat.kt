package de.hglabor.plugins.duels.eventmanager

import de.hglabor.plugins.duels.arenas.setName
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.broadcast
import org.bukkit.event.player.AsyncPlayerChatEvent

object OnPlayerChat {
    fun enable() {
        listen<AsyncPlayerChatEvent> {

            if (!setName.contains(it.player)) {
                if (it.isCancelled) return@listen

                it.isCancelled = true

                val player = it.player
                var finalMessage = it.message

                if (player.hasPermission("colorchat"))
                    finalMessage = finalMessage.replace("&", "§")

                broadcast("${KColors.GRAY}─ ${player.displayName} ${KColors.DARKGRAY}» ${KColors.WHITE}$finalMessage")
            }
        }
    }
}