package de.hglabor.plugins.duels.settings

import org.bukkit.entity.Player
import java.util.*

object Settings {
    val knockback = hashMapOf<UUID, Knockback>()
    val hitSound = hashMapOf<UUID, Boolean>()
    val chatSetting = hashMapOf<UUID, Chat>()

    enum class Knockback { OLD, NEW }
    enum class Chat { ALL, FIGHT, NEVER }

    fun setPlayerSettings(player: Player) {
        if (!hitSound.containsKey(player.uniqueId))
            hitSound[player.uniqueId] = true
        if (!knockback.containsKey(player.uniqueId))
            knockback[player.uniqueId] = Knockback.NEW
        if (!chatSetting.containsKey(player.uniqueId))
            chatSetting[player.uniqueId] = Chat.ALL
    }
}