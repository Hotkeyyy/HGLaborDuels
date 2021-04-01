package de.hglabor.plugins.duels.kits.specials.special

import de.hglabor.plugins.duels.kits.specials.Specials
import de.hglabor.plugins.duels.player.DuelsPlayer
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.broadcast
import org.bukkit.entity.*
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.util.Vector

object ProjectileKnockback {
    init {
        listen<ProjectileHitEvent> {
            val hook = it.entity
            val hookShooter = hook.shooter
            if (!(it.entity is FishHook || it.entity is Snowball)) return@listen
            if (it.hitEntity ?: return@listen is LivingEntity) {
                val hitEntity = it.hitEntity as LivingEntity

                if (hookShooter is Player && hitEntity is Player) {
                    val duelsShooter = DuelsPlayer.get(hookShooter)
                    val duelsEntity = DuelsPlayer.get(hitEntity)

                    if (duelsEntity.isInFight() && duelsShooter.isInFight()) {
                        val duel = duelsEntity.currentDuel() ?: return@listen
                        if (duel.kit.specials.contains(Specials.ROD_KNOCKBACK)) {
                            if (hitEntity.noDamageTicks > 2) return@listen
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

        listen<PlayerFishEvent> {
            val hook = it.hook
            val hookShooter = hook.shooter
            val hitEntity = it.caught ?: return@listen

            if (hookShooter is Player && hitEntity is Player) {
                val duelsEntity = DuelsPlayer.get(hitEntity)
                val duel = duelsEntity.currentDuel() ?: return@listen
                if (duel.kit.specials.contains(Specials.ROD_KNOCKBACK)) {
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