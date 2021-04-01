package de.hglabor.plugins.duels.utils

import net.axay.kspigot.runnables.task
import org.bukkit.Location
import org.bukkit.entity.LivingEntity

object LocationUtils {
    fun setDirection(livingEntity: LivingEntity, loc: Location) {
        task(true, 5) {
            val dir = loc.clone().subtract(livingEntity.eyeLocation).toVector()
            val finalLoc = livingEntity.location.setDirection(dir)
            finalLoc.pitch = 0f
            livingEntity.teleport(finalLoc)
        }
    }
}