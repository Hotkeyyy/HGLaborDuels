package de.hglabor.plugins.duels.kits.specials.special

import de.hglabor.plugins.duels.kits.specials.Specials
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import net.axay.kspigot.event.listen
import org.bukkit.entity.Entity
import org.bukkit.entity.FishHook
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.util.Vector

object RodKnockback {
    init {
        listen<ProjectileHitEvent> {
            val hook = it.entity
            val hookShooter = hook.shooter
            if (it.entity is FishHook) {
                if (it.hitEntity != null) {
                    if (it.hitEntity is LivingEntity) {
                        val hitEntity = it.hitEntity as LivingEntity
                        if (hookShooter is Player && hitEntity is Player) {
                            if (hookShooter.isInFight() && hitEntity.isInFight()) {
                                if (Data.duelFromPlayer(hookShooter).kit.specials.contains(Specials.ROD_KNOCKBACK)) {
                                    var kx = hook.location.direction.x / 1.8
                                    val kz = hook.location.direction.z / 1.8
                                    kx -= kx * 2.0
                                    hitEntity.damage(0.000001, hookShooter as Entity)
                                    var upVel = 0.372
                                    //var upVel = 0.452
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
                    if (Data.duelFromPlayer(hookShooter).kit.specials.contains(Specials.ROD_KNOCKBACK)) {
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

    }
}