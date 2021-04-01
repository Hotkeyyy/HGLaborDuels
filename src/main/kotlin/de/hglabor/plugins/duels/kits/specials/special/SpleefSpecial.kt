package de.hglabor.plugins.duels.kits.specials.special

import de.hglabor.plugins.duels.kits.specials.Specials
import de.hglabor.plugins.duels.player.DuelsPlayer
import net.axay.kspigot.event.listen
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

object SpleefSpecial {
    init {
        listen<PlayerInteractEvent> {
            if (it.action != Action.LEFT_CLICK_BLOCK) return@listen
            val player: Player = it.player
            val duelsPlayer = DuelsPlayer.get(player)
            val duel = duelsPlayer.currentDuel() ?: return@listen

            if (duel.kit.specials.contains(Specials.SPLEEF)) {
                if (duel.alivePlayers.contains(player)) {
                    val clickedBlock = it.clickedBlock ?: return@listen
                    if (clickedBlock.type == Material.SNOW_BLOCK) {
                        clickedBlock.type = Material.AIR
                        player.inventory.addItem(ItemStack(Material.SNOWBALL))
                    }
                }
            }
        }

        listen<ProjectileHitEvent> {
            val shooter = it.entity.shooter
            if (shooter is Player) {
                val duelsPlayer = DuelsPlayer.get(shooter)
                val duel = duelsPlayer.currentDuel() ?: return@listen
                if (duel.kit.specials.contains(Specials.SPLEEF)) {
                    if (duel.alivePlayers.contains(shooter)) {
                        val hitBlock = it.hitBlock ?: return@listen
                        if (hitBlock.type == Material.SNOW_BLOCK) {
                            hitBlock.type = Material.AIR
                        }
                    }
                }
            }
        }
    }
}