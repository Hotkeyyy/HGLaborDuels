package de.hglabor.plugins.duels.eventmanager

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.settings.Settings
import de.hglabor.plugins.duels.utils.PlayerFunctions.getStats
import de.hglabor.plugins.duels.utils.PlayerFunctions.hasRank
import de.hglabor.plugins.duels.utils.PlayerFunctions.reset
import de.hglabor.plugins.duels.utils.Ranks
import de.hglabor.plugins.staff.utils.StaffData
import de.hglabor.plugins.staff.utils.StaffData.isStaff
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.onlineSenders
import net.axay.kspigot.runnables.async
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent

object OnPlayerJoin {

    fun enable() {
        listen<PlayerJoinEvent>(EventPriority.HIGHEST) {
            it.joinMessage = null
            val player = it.player
            if (!player.getStats().exist())
                player.getStats().create()
            if (!player.hasRank())
                Ranks.setRank(player, Ranks.Rank.NORMIE)

            if (!player.isStaff)
                StaffData.vanishedPlayers.forEach { vanished -> player.hidePlayer(Manager.INSTANCE, vanished) }

            player.reset()
            player.setPlayerListHeaderFooter(header, footer)

            Settings.setPlayerSettings(player)

            async {
                val joinMessage = "${KColors.CHARTREUSE}→ ${KColors.POWDERBLUE}${player.name}"
                onlineSenders.forEach { all -> all.sendMessage(joinMessage) }
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