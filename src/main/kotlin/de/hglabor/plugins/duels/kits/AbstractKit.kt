package de.hglabor.plugins.duels.kits

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.kits.specials.Specials
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

val kits = mutableListOf<AbstractKit>()

abstract class AbstractKit {

    abstract val name: String
    abstract val itemInGUI: ItemStack
    abstract val category: KitCategory?
    open val arenaTag: ArenaTags = ArenaTags.NONE
    open val type: KitType? = null
    open val specials: Set<Specials?> = setOf(null)
    open val allowsRespawn = false

    abstract fun giveKit(player: Player)

    fun hasSpecial(special: Specials) = specials.contains(special)
}
