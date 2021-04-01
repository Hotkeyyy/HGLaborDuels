package de.hglabor.plugins.duels.kits.specials

import de.hglabor.plugins.duels.kits.specials.special.DeadInWater
import de.hglabor.plugins.duels.kits.specials.special.NinjaSpecial
import de.hglabor.plugins.duels.kits.specials.special.PearlCooldown
import de.hglabor.plugins.duels.kits.specials.special.ProjectileKnockback
import net.axay.kspigot.event.listen
import net.axay.kspigot.utils.hasMark
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType


enum class Specials {
    NINJA, NODAMAGE, DEADINWATER, PEARLCOOLDOWN, HITCOOLDOWN, JUMPANDRUN, UHC, INVINICIBLE, ROD_KNOCKBACK, HUNGER, SPLEEF;

    companion object {
        fun enable() {
            NinjaSpecial
            ProjectileKnockback
            PearlCooldown
            DeadInWater

            listen<PlayerItemConsumeEvent> {
                if (it.item.itemMeta?.hasMark("goldenHead") == true) {
                    it.player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 20 * 9, 1))
                }
            }
        }
    }
}