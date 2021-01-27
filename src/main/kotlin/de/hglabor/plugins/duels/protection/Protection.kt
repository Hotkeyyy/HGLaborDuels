package de.hglabor.plugins.duels.protection

import de.hglabor.plugins.duels.soupsimulator.Soupsim.isInSoupsimulator
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.staff.utils.StaffData.isInStaffMode
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.getHandItem
import net.axay.kspigot.utils.hasMark
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.*
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerArmorStandManipulateEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.inventory.EquipmentSlot

object Protection {

    val GameMode.isRestricted
        get() = when (this) {
            GameMode.ADVENTURE, GameMode.SURVIVAL -> true
            else -> false
        }

    fun enable() {
        listen<BlockBreakEvent> {
            if (it.player.isInStaffMode)
                it.isCancelled = true

            if (!it.player.isInFight())
                it.isCancelled = it.player.gameMode.isRestricted
        }

        listen<BlockPlaceEvent> {
            if (it.player.isInStaffMode)
                it.isCancelled = true

            if (!it.player.isInFight())
                it.isCancelled = it.player.gameMode.isRestricted
        }

        listen<FoodLevelChangeEvent> {
            it.isCancelled = true
        }

        listen<EntityDamageEvent> {
            if (it.entity is Player) {
                if ((it.entity as Player).isInStaffMode)
                    it.isCancelled = true

                if ((it.entity as Player).isInFight()) return@listen
                if ((it.entity as Player).isInSoupsimulator()) {
                    if (it.cause != DamageCause.CUSTOM) {
                        it.isCancelled = true
                    }
                    return@listen
                }
                it.isCancelled = true
                return@listen
            }
        }

        listen<EntityDamageByEntityEvent> {
            if (it.entity is Player && it.damager is Player) {

                if ((it.damager as Player).getHandItem(EquipmentSlot.HAND)!!.hasMark("duelitem"))
                    it.isCancelled = true
                if ((it.damager as Player).isInStaffMode)
                    it.isCancelled = true
                if ((it.entity as Player).isInStaffMode)
                    it.isCancelled = true
                if ((it.damager as Player).isInFight() && (it.entity as Player).isInFight()) {
                    it.isCancelled = false
                }
            }
        }

        listen<PlayerDropItemEvent> {
            val p = it.player
            if (p.isInStaffMode)
                it.isCancelled = true

            if (!p.isInFight() && p.gameMode.isRestricted) {
                it.isCancelled = true
            }
        }

        listen<InventoryClickEvent> {
            if (it.whoClicked is Player) {
                val p = it.whoClicked as Player
                if (!p.isInFight() && p.gameMode.isRestricted && !p.isInSoupsimulator()) {
                    it.isCancelled = true
                }
            }
        }

        listen<EntityRegainHealthEvent> {
            if (it.entity is Player) {
                if ((it.entity as Player).isInSoupsimulator()) {
                    it.isCancelled = true
                } else {
                    if (it.regainReason == EntityRegainHealthEvent.RegainReason.EATING || it.regainReason == EntityRegainHealthEvent.RegainReason.SATIATED) {
                        it.isCancelled = true
                    }
                }
            }
        }

        listen<CreatureSpawnEvent> {
            if (it.entity.location.world != Bukkit.getWorld("FightWorld")) {
                it.isCancelled = true
            }
        }

        listen<EntitySpawnEvent> {
            if (it.entity.location.world != Bukkit.getWorld("FightWorld")) {
                it.isCancelled = true
            }
        }

        listen<PlayerArmorStandManipulateEvent> {
            if(it.player.gameMode.isRestricted)
                it.isCancelled = true
        }

        listen<EntityExplodeEvent> {
            it.blockList().clear()
        }

        listen<BlockExplodeEvent> {
            it.blockList().clear()
        }
    }
}