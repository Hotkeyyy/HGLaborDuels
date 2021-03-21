package de.hglabor.plugins.duels.kits

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.guis.KitsGUI
import de.hglabor.plugins.duels.kits.specials.Specials
import net.axay.kspigot.extensions.broadcast
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack

val kits = mutableListOf<AbstractKit>()

abstract class AbstractKit {

    init {
        broadcast("init kit")
    }

    abstract val name: String
    abstract val itemInGUI: ItemStack
    abstract val arenaTag: ArenaTags
    abstract val type: KitType?
    abstract val category: KitCategory?
    abstract val specials: Set<Specials?>
    abstract val allowsRespawn: Boolean

    abstract fun giveKit(player: Player)

    fun hasSpecial(special: Specials) = specials.contains(special)
}
