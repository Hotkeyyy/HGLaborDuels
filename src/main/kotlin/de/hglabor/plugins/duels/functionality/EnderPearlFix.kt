package de.hglabor.plugins.duels.functionality

import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.specials.Specials
import de.hglabor.plugins.duels.player.DuelsPlayer
import net.axay.kspigot.event.listen
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.inventory.ItemStack

object EnderPearlFix {
    init {
        val enderPearlsToFix = mutableSetOf<Player>()

        listen<ProjectileHitEvent> {
            if (it.entity.shooter is Player) {
                if (it.entityType == EntityType.ENDER_PEARL) {
                    if (it.hitEntity != null) return@listen
                    if (it.hitBlockFace != BlockFace.UP && it.hitBlockFace != BlockFace.DOWN) {
                        val blockName = it.hitBlock?.type?.name?.toLowerCase() ?: ""
                        if (!blockName.contains("pane") && !blockName.contains("fence")) {
                            enderPearlsToFix += it.entity.shooter as Player
                        }
                    }
                }
            }
        }

        listen<PlayerTeleportEvent> {
            val player = it.player
            val duelsPlayer = DuelsPlayer.get(player)
            val duel = duelsPlayer.currentDuel() ?: return@listen
            val to = it.to ?: return@listen

            if (it.cause === PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
                if (!to.block.type.isSolid) return@listen
                it.isCancelled = true
                player.sendMessage("detected pearlglitching - pearl has been refunded")
                player.inventory.addItem(ItemStack(Material.ENDER_PEARL))

                if (duel.kit.hasSpecial(Specials.PEARLCOOLDOWN)) {
                    Kits.removeCooldown(it.player)
                }
            }
            if (enderPearlsToFix.contains(player)) {
                val toLoc = Location(to.world, to.blockX + 0.5, to.y, to.blockZ + 0.5)
                it.isCancelled = true
                player.teleport(toLoc)
                enderPearlsToFix.remove(player)
            }
        }
    }
}
