package de.hglabor.plugins.duels.kits

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.kits.specials.Specials
import de.hglabor.plugins.duels.player.DuelsPlayer
import de.hglabor.plugins.duels.utils.KitUtils
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

val kits = mutableListOf<AbstractKit>()
val nameToKit = mutableMapOf<String, AbstractKit>()

abstract class AbstractKit {

    abstract val name: String
    abstract val itemInGUI: ItemStack
    abstract val category: KitCategory?
    open val arenaTag: ArenaTags = ArenaTags.NONE
    open val type: KitType? = null
    open val specials: Set<Specials?> = setOf(null)
    open val allowsRespawn = false
    open val allowsRanked = false
    open val isMainKit = false
    open var unrankedQueue = setOf<Player>()
    open var rankedQueue = setOf<Player>()
    open val defaultInventory = mutableMapOf<Int, ItemStack>()
    open val offHand: ItemStack? = null
    open val armor = mutableMapOf<KitUtils.ArmorSlots, ItemStack?>()

    fun attributes(player: Player) {
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    open fun setAttributes(player: Player) {}

    fun enable() {
        kits += this
        if (isMainKit) Kits.mainKits += this
    }

    fun giveKit(player: Player) {
        player.inventory.clear()
        val duelsPlayer = DuelsPlayer.get(player)
        val inventory = duelsPlayer.inventorySorting.inventories[this] ?: defaultInventory

        armor.forEach { (slot, item) ->
            player.inventory.setItem(slot.number, item)
        }
        inventory.forEach { (slot, item) ->
            player.inventory.setItem(slot, item)
        }
        player.inventory.setItemInOffHand(offHand)
        attributes(player)
        setAttributes(player)
        giveRest(player)
    }

    open fun giveRest(player: Player) { }

    fun hasSpecial(special: Specials) = specials.contains(special)
}
