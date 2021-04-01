package de.hglabor.plugins.duels.events.listeners.duel

import de.hglabor.plugins.duels.kits.KitType
import de.hglabor.plugins.duels.player.DuelsPlayer
import net.axay.kspigot.event.listen
import org.bukkit.entity.Player
import org.bukkit.event.entity.PotionSplashEvent

object OnPotionSplash {
    init {
        listen<PotionSplashEvent> {
            if (it.potion.shooter is Player) {
                val potion = it.potion
                val player = potion.shooter as Player
                val duelsPlayer = DuelsPlayer.get(player)
                if (duelsPlayer.isInFight()) {
                    val duel = duelsPlayer.currentDuel() ?: return@listen
                    if (duel.kit.type == KitType.POT) {
                        val healed = 8 * it.getIntensity(player).toInt()
                        val missed = 8 - healed
                        if (missed > 2)
                            duel.missedPots[player] = (duel.missedPots[player]?: 0) + 1

                        duel.wastedHealth[player] = (duel.wastedHealth[player]?: 0) + missed
                    }
                }
            }
        }
    }
}
