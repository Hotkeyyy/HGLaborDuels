package de.hglabor.plugins.duels.events.listeners

import de.hglabor.plugins.duels.localization.sendMsg
import net.axay.kspigot.event.listen
import org.bukkit.Bukkit
import org.bukkit.event.player.PlayerCommandPreprocessEvent

object OnPlayerCommandPreprocess {
    init {
        listen<PlayerCommandPreprocessEvent> {
            val player = it.player
            if (!it.isCancelled) {
                val command = it.message.split(" ")[0];
                val htopic = Bukkit.getServer().helpMap.getHelpTopic(command);
                if (htopic == null) {
                    player.sendMsg("command.unknownCommand", mutableMapOf("command" to command))
                    it.isCancelled = true
                }
            }
        }
    }
}