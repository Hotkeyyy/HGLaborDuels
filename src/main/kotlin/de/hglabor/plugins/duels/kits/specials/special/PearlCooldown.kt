package de.hglabor.plugins.duels.kits.specials.special

import de.hglabor.plugins.duels.duel.GameState
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.specials.Specials
import de.hglabor.plugins.duels.player.DuelsPlayer
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.events.isRightClick
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent

object PearlCooldown {
    init {
        listen<PlayerInteractEvent> {
            val player = it.player
            val duelsPlayer = DuelsPlayer.get(player)
            if (it.action.isRightClick) {
                if (duelsPlayer.isInFight()) {
                    val duel = duelsPlayer.currentDuel() ?: return@listen
                    if (it.item?.type == Material.ENDER_PEARL) {
                        if (duel.state == GameState.INGAME) {
                            if (duel.kit.specials.contains(Specials.PEARLCOOLDOWN)) {
                                if (!Kits.hasCooldown(player))
                                    Kits.setCooldown(player, 15)
                                else
                                    it.isCancelled = true
                            }
                        } else {
                            it.isCancelled = true
                        }
                    }
                }
            }
        }
    }
}