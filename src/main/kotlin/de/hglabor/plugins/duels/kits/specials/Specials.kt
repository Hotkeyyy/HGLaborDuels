package de.hglabor.plugins.duels.kits.specials

import de.hglabor.plugins.duels.duel.GameState
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.Kits.Companion.info
import de.hglabor.plugins.duels.kits.kitMap
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.isFeetInWater
import net.axay.kspigot.extensions.events.isRightClick
import net.axay.kspigot.utils.hasMark
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.FishHook
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.*
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin


enum class Specials {
    NINJA, NODAMAGE, DEADINWATER, PEARLCOOLDOWN, HITCOOLDOWN, JUMPANDRUN, UHC, INVINICIBLE, ROD_KNOCKBACK;

    companion object {
        fun enable() {

            // Ninja
            listen<PlayerToggleSneakEvent> {
                val player: Player = it.player
                if (player.isInFight()) {
                    val duel = Data.duelFromPlayer(player)
                    if (duel.kit.info.specials.contains(NINJA)) {
                        if (duel.lastHitOfPlayer.containsKey(player)) {
                            val target = duel.lastHitOfPlayer[player]
                            if (duel.alivePlayers.contains(target)) {
                                if (!Kits.hasCooldown(player)) {
                                    Kits.setCooldown(player, 13)
                                    var nang: Float = target!!.location.yaw + 90
                                    if (nang < 0) nang += 360f
                                    val nX = cos(Math.toRadians(nang.toDouble()))
                                    val nZ = sin(Math.toRadians(nang.toDouble()))
                                    val loc = target.location.clone().subtract(nX, 0.0, nZ)
                                    player.teleport(loc)
                                }
                            }
                        }
                    }
                }
            }

            // Golden Head
            listen<PlayerItemConsumeEvent> {
                if (it.item.itemMeta.hasMark("goldenHead")) {
                    it.player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 20 * 9, 1))
                }
            }

            // ROD_KNOCKBACK
            listen<ProjectileHitEvent> {
                val hook = it.entity
                val hookShooter = hook.shooter
                if (it.entity is FishHook) {
                    if (it.hitEntity != null) {
                        if (it.hitEntity is LivingEntity) {
                            val hitEntity = it.hitEntity as LivingEntity
                            if (hookShooter is Player && hitEntity is Player) {
                                if (hookShooter.isInFight() && hitEntity.isInFight()) {
                                    if (Data.duelFromPlayer(hookShooter).kit.info.specials.contains(ROD_KNOCKBACK)) {
                                        var kx = hook.location.direction.x / 1.8
                                        val kz = hook.location.direction.z / 1.8
                                        kx -= kx * 2.0
                                        hitEntity.damage(0.000001, hookShooter as Entity)
                                        //var upVel = 0.372
                                        var upVel = 0.452
                                        if (!hitEntity.isOnGround) {
                                            upVel = 0.0
                                        }
                                        hitEntity.velocity = Vector(kx, upVel, kz)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            listen<PlayerFishEvent> {
                val hook = it.hook
                val hookShooter = hook.shooter
                if (it.caught != null) {
                    val hitEntity = it.caught
                    if (hookShooter is Player && hitEntity is Player) {
                        if (Data.duelFromPlayer(hookShooter).kit.info.specials.contains(ROD_KNOCKBACK)) {
                            if (it.state == PlayerFishEvent.State.CAUGHT_ENTITY) {
                                if (it.caught is Player) {
                                    it.isCancelled = true
                                    it.hook.remove()
                                }
                            }
                        }
                    }
                }
            }

            // PEARL COOLDOWN
            listen<PlayerInteractEvent> {
                val player = it.player
                if (it.action.isRightClick) {
                    if (player.isInFight()) {
                        val duel = Data.duelFromPlayer(player)
                        if (player.inventory.itemInMainHand.type == Material.ENDER_PEARL) {
                            if (duel.state == GameState.RUNNING) {
                                if (kitMap[duel.kit]!!.specials.contains(PEARLCOOLDOWN)) {
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

            // DEAD IN WATER / JUMP AND RUN
            listen<PlayerMoveEvent> {
                val player = it.player
                if (player.isInFight()) {
                    val duel = Data.duelFromID[Data.duelIDFromPlayer[player]]
                    if (duel?.state == GameState.COUNTDOWN)
                        it.isCancelled = true
                    if (duel?.kit?.info?.specials?.contains(DEADINWATER) == true) {
                        if (duel.state == GameState.RUNNING) {
                            if (player.isFeetInWater) {
                                duel.playerDied(
                                    player,
                                    "${duel.teamColor(player)}${player.name} ${KColors.GRAY}ist gestorben.",
                                    "${duel.teamColor(player)}${player.name} ${KColors.GRAY}died.")

                                if (Data.duelFromSpec.containsKey(player)) {
                                    val duel = Data.duelFromSpec[player]!!
                                    val arena = duel.arena
                                    duel.addSpectator(player, false)
                                    val spawnOne = arena.spawn1Loc
                                    val spawnTwo = arena.spawn2Loc
                                    val x = (spawnOne.x + spawnTwo.x) / 2
                                    val y = spawnOne.y + 3
                                    val z = (spawnOne.z + spawnTwo.z) / 2
                                    val centerLoc = Location(Bukkit.getWorld("FightWorld"), x, y, z)
                                    player.teleport(centerLoc)
                                }
                            }
                        }
                    } else if (duel?.kit?.info?.specials?.contains(JUMPANDRUN) == true) {
                        if (duel.state == GameState.RUNNING) {
                            if (player.isFeetInWater) {
                                if (duel.teamOne.contains(player)) {
                                    player.teleport(duel.arena.spawn1Loc)
                                    duel.direction(player, duel.arena.spawn2Loc)
                                } else {
                                    player.teleport(duel.arena.spawn2Loc)
                                    duel.direction(player, duel.arena.spawn1Loc)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}