package de.hglabor.plugins.duels.functionality

import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.specials.Specials
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.broadcast
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
            if (it.cause === PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
                if (it.to.block.type != Material.AIR) {
                    it.isCancelled = true
                    broadcast("detected pearlglitching - pearl has been refunded")
                    player.inventory.addItem(ItemStack(Material.ENDER_PEARL))
                    if (player.isInFight()) {
                        val duel = Data.duelIDFromPlayer[player]?.let { id -> Duel.get(id) }
                        if (duel?.kit?.hasSpecial(Specials.PEARLCOOLDOWN) == true) {
                            Kits.removeCooldown(it.player)
                        }
                    }
                }
                if (enderPearlsToFix.contains(player)) {
                    val to: Location = it.to
                    to.x = to.blockX + 0.5
                    to.z = to.blockZ + 0.5
                    it.to = to
                    enderPearlsToFix.remove(player)
                }
            }
        }
    }
}