package de.hglabor.plugins.duels.events.listeners.duel

import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import net.axay.kspigot.event.listen
import org.bukkit.entity.Player
import org.bukkit.event.entity.PotionSplashEvent

object OnPotionSplash {
    fun enable() {
        listen<PotionSplashEvent> {
            if (it.potion.shooter is Player) {
                val potion = it.potion
                val player = potion.shooter as Player
                if (player.isInFight()) {
                    val duel = Data.duelFromPlayer(player)
                    val healed = 8 * it.getIntensity(player).toInt()
                    val missed = 8 - healed
                    if (missed > 2)
                        duel.missedPots[player] = duel.missedPots[player]!! + 1

                    duel.wastedHealth[player] = duel.wastedHealth[player]!! + missed
                }
            }
        }
    }
}
