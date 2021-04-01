package de.hglabor.plugins.duels.events.listeners

import de.hglabor.plugins.duels.kits.specials.Specials
import de.hglabor.plugins.duels.player.DuelsPlayer
import net.axay.kspigot.event.listen
import org.bukkit.entity.Player
import org.bukkit.event.entity.FoodLevelChangeEvent

object OnFoodLevelChange {
    init {
        listen<FoodLevelChangeEvent> {
            if (it.entity !is Player) return@listen
            val player = it.entity as Player
            val duelsPlayer = DuelsPlayer.get(player)
            if (duelsPlayer.isInFight()) {
                val duel = duelsPlayer.currentDuel() ?: return@listen
                if (duel.kit.hasSpecial(Specials.HUNGER)) {
                    it.isCancelled = false
                    return@listen
                }
            }
            it.isCancelled = true
        }
    }
}