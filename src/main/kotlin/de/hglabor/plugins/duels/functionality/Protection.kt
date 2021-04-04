package de.hglabor.plugins.duels.functionality

import de.hglabor.plugins.duels.guis.KitsGUI
import de.hglabor.plugins.duels.kits.KitType
import de.hglabor.plugins.duels.kits.specials.Specials
import de.hglabor.plugins.duels.utils.sendMsg
import de.hglabor.plugins.duels.player.DuelsPlayer
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.staff.utils.StaffData.isInStaffMode
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.getHandItem
import net.axay.kspigot.utils.hasMark
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.block.BlockFadeEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.*
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.inventory.CraftItemEvent
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

    init {
        listen<BlockBreakEvent> {
            val player = it.player
            val duelsPlayer = DuelsPlayer.get(player)

            if (player.isInStaffMode)
                it.isCancelled = true

            if (!duelsPlayer.isInFight())
                it.isCancelled = it.player.gameMode.isRestricted
        }

        listen<BlockPlaceEvent> {
            val player = it.player
            val duelsPlayer = DuelsPlayer.get(player)

            if (player.isInStaffMode)
                it.isCancelled = true

            if (!duelsPlayer.isInFight())
                it.isCancelled = it.player.gameMode.isRestricted
        }

        listen<FoodLevelChangeEvent> {
            it.isCancelled = true
        }

        listen<EntityDamageEvent> {
            if (it.entity is Player) {
                val player = it.entity as Player
                val duelsPlayer = DuelsPlayer.get(player)

                if (player.isInStaffMode)
                    it.isCancelled = true

                if (duelsPlayer.isInFight()) return@listen
                if (duelsPlayer.isInSoupsimulator()) {
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
                val damager = it.damager as Player
                val entity = it.entity as Player
                val duelsDamager = DuelsPlayer.get(damager)
                val duelsEntity = DuelsPlayer.get(entity)

                if (damager.getHandItem(EquipmentSlot.HAND)!!.hasMark("duelitem"))
                    it.isCancelled = true
                if (damager.isInStaffMode)
                    it.isCancelled = true
                if (entity.isInStaffMode)
                    it.isCancelled = true
                if (duelsDamager.isInFight() && duelsEntity.isInFight()) {
                    it.isCancelled = false
                }
            } else {
                if (it.damager is Player && it.entity !is Player) {
                    it.isCancelled = (it.damager as Player).gameMode != GameMode.CREATIVE
                }
            }
        }

        listen<PlayerDropItemEvent> {
            val player = it.player
            val duelsPlayer = DuelsPlayer.get(player)

            if (player.isInStaffMode)
                it.isCancelled = true

            if (!duelsPlayer.isInFight() && player.gameMode.isRestricted) {
                it.isCancelled = true
            }
        }

        listen<InventoryClickEvent> {
            if (it.whoClicked is Player) {
                val player = it.whoClicked as Player
                val duelsPlayer = DuelsPlayer.get(player)

                if (Data.openedKitInventory[player] == KitsGUI.KitInventories.INVENTORYSORTING) {
                    it.isCancelled = false
                    return@listen
                }

                if (!duelsPlayer.isInFight() && player.gameMode.isRestricted && !duelsPlayer.isInSoupsimulator()) {
                    it.isCancelled = true
                }
            }
        }

        listen<EntityRegainHealthEvent> {
            if (it.entity is Player) {
                val player = it.entity as Player
                val duelsPlayer = DuelsPlayer.get(player)

                if (duelsPlayer.isInFight()) {
                    val duel = duelsPlayer.currentDuel() ?: return@listen
                    if (duel.kit.specials.contains(Specials.UHC)) {
                        if (!(it.regainReason == EntityRegainHealthEvent.RegainReason.MAGIC ||
                                    it.regainReason == EntityRegainHealthEvent.RegainReason.MAGIC_REGEN ||
                                    it.regainReason == EntityRegainHealthEvent.RegainReason.CUSTOM)
                        ) {
                            it.isCancelled = true
                        }
                    }
                }

                if (duelsPlayer.isInSoupsimulator()) {
                    it.isCancelled = true
                }
            }
        }

        listen<CreatureSpawnEvent> {
            if (it.spawnReason == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG ||
                it.spawnReason == CreatureSpawnEvent.SpawnReason.DEFAULT
            ) {
                it.isCancelled = false
            } else {
                if (it.entity.location.world != Bukkit.getWorld("FightWorld")) {
                    it.isCancelled = true
                }
            }
        }

        listen<PlayerArmorStandManipulateEvent> {
            if (it.player.gameMode.isRestricted)
                it.isCancelled = true
        }

        listen<EntityExplodeEvent> {
            if (it.entityType == EntityType.ENDER_CRYSTAL && it.entity.world == Bukkit.getWorld("FightWorld"))
                return@listen
            it.blockList().clear()
        }

        listen<BlockExplodeEvent> {
            it.blockList().clear()
        }

        listen<CraftItemEvent> {
            if (it.whoClicked is Player) {
                val player = it.whoClicked as Player
                val duelsPlayer = DuelsPlayer.get(player)

                if (it.currentItem?.type!!.name.contains("BOAT")) {
                    it.isCancelled = true
                    player.sendMsg("itemCantBeCrafted")
                }

                if (duelsPlayer.isInFight()) {
                    val duel = duelsPlayer.currentDuel() ?: return@listen
                    if (duel.kit.type != KitType.SOUP) {
                        if (it.currentItem?.type!! == Material.MUSHROOM_STEW) {
                            it.isCancelled = true
                            player.sendMsg("itemCantBeCrafted")
                        }
                    }
                }
            }
        }

        listen<BlockFadeEvent> {
            val blockName = it.block.type.toString().toLowerCase()
            if (blockName.contains("ice") || blockName.contains("snow")) {
                it.isCancelled = true
            }
        }
    }
}