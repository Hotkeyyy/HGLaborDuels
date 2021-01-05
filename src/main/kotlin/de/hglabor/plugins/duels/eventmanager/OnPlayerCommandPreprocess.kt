package de.hglabor.plugins.duels.eventmanager

import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
import net.axay.kspigot.event.listen
import org.bukkit.Bukkit
import org.bukkit.event.player.PlayerCommandPreprocessEvent

object OnPlayerCommandPreprocess {
    fun enable() {
        listen<PlayerCommandPreprocessEvent> {
            val player = it.player
            if (!it.isCancelled) {
                val command = it.message.split(" ")[0];
                val htopic = Bukkit.getServer().helpMap.getHelpTopic(command);
                if (htopic == null) {
                    player.sendLocalizedMessage(Localization.UNKNOWN_COMMAND_DE.replace("%command%", command),
                        Localization.UNKNOWN_COMMAND_EN.replace("%command%", command))
                    it.isCancelled = true
                }
            }
        }
    }
}