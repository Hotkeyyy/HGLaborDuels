package de.hglabor.plugins.duels.kits.specials.special

import de.hglabor.plugins.duels.duel.GameState
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.specials.Specials
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.events.isRightClick
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent

object PearlCooldown {
    init {
        listen<PlayerInteractEvent> {
            val player = it.player
            if (it.action.isRightClick) {
                if (player.isInFight()) {
                    val duel = Data.duelFromPlayer(player)
                    if (player.inventory.itemInMainHand.type == Material.ENDER_PEARL) {
                        if (duel.state == GameState.RUNNING) {
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