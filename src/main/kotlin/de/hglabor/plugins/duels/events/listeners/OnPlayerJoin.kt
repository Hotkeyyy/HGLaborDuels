package de.hglabor.plugins.duels.events.listeners

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.data.PlayerSettings
import de.hglabor.plugins.duels.data.PlayerStats
import de.hglabor.plugins.duels.utils.PlayerFunctions.reset
import de.hglabor.plugins.staff.utils.StaffData
import de.hglabor.plugins.staff.utils.StaffData.isStaff
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.runnables.async
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent

object OnPlayerJoin {

    fun enable() {
        listen<PlayerJoinEvent>(EventPriority.HIGHEST) {
            it.joinMessage = null
            val player = it.player
            if (!player.isStaff)
                StaffData.vanishedPlayers.forEach { vanished -> player.hidePlayer(Manager.INSTANCE, vanished) }

            player.reset()
            player.setPlayerListHeaderFooter(header, footer)

            async {
                PlayerStats.get(player)
                PlayerSettings.get(player)
            }
        }
    }

    private val header = StringBuilder().apply {
        appendLine(" ")
        appendLine("   ${KColors.BOLD}${KColors.DODGERBLUE}HG${KColors.DEEPSKYBLUE}Labor${KColors.WHITE}.${KColors.LIGHTGRAY}de   ")
    }.toString()

    private val footer = StringBuilder().apply {
        appendLine()
    }.toString()

}