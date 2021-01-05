package de.hglabor.plugins.duels.functionality

import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.getStats
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.getHandItem
import net.axay.kspigot.extensions.events.isRightClick
import net.axay.kspigot.extensions.pluginKey
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent

object SoupHealing {

    fun enable() {
        listen<PlayerInteractEvent> {
            if (it.action.isRightClick) {
                val p = it.player
                var consumed = false
                var presouped = false
                var wastedHealth = 0
                if (p.isInFight()) {
                    val duelID = Data.duelIDFromPlayer[p]
                    val duel = Data.duelFromID[duelID]
                    if (p.getHandItem(it.hand)?.type == Material.MUSHROOM_STEW) {
                        if (p.health != 20.0) {
                            if (p.health > 13) {
                                p.health = 20.0
                                presouped = true
                                wastedHealth = (p.health.toInt() + 7 - 20)
                            } else if (p.health <= 13)
                                p.health = p.health + 7
                            consumed = true

                        } else {
                            if (p.foodLevel != 20) {
                                if (p.foodLevel >= 12)
                                    p.health = 20.0
                                else if (p.health < 12)
                                    p.health = p.health + 8
                                consumed = true
                                p.inventory.itemInMainHand.type = Material.BOWL
                            }
                        }

                        if (consumed) {
                            p.inventory.itemInMainHand.type = Material.BOWL
                            p.getStats().addSoupEaten()
                            if(presouped) {
                                duel!!.presoups[p] = duel.presoups[p]!! + 1
                                duel.wastedHealth[p] = duel.wastedHealth[p]!! + wastedHealth
                            }
                        }
                    }
                }
            }
        }
    }
}