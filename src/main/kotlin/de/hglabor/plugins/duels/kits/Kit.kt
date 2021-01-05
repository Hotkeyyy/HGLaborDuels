package de.hglabor.plugins.duels.kits

import de.hglabor.plugins.duels.arenas.ArenaTags
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

val kitMap = hashMapOf<Kits, Kit>()

abstract class Kit(val kits: Kits) {

    abstract val name: String
    abstract val itemInGUIs: ItemStack
    abstract val arenaTag: ArenaTags
    abstract val type: KitType
    abstract val specials: List<String>?

    abstract fun giveKit(player: Player)
    abstract fun enable()
}
