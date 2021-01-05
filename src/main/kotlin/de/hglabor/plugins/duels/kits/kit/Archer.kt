package de.hglabor.plugins.duels.kits.kit

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.guis.ChooseKitGUI
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.duel
import net.axay.kspigot.items.itemStack
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Archer : Kit(Kits.ARCHER) {
    override val name = "Archer"
    override val itemInGUIs = Kits.guiItem(Material.BOW, name, null)
    override val arenaTag = ArenaTags.NONE
    override val type = KitType.NONE
    override val specials: Nothing? = null

    override fun giveKit(player: Player) {
        player.inventory.clear()
        KitUtils.armor(player, Material.LEATHER_HELMET, Material.CHAINMAIL_CHESTPLATE,
            Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS)

        player.inventory.setItem(0, itemStack(Material.BOW) {
            addEnchantment(Enchantment.ARROW_INFINITE, 1) })

        player.inventory.setItem(1, ItemStack(Material.GOLDEN_APPLE, 8))
        player.inventory.setItem(9, ItemStack(Material.ARROW))

        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    override fun enable() {
        ChooseKitGUI.addContent(
            ChooseKitGUI.KitsGUICompoundElement(
                itemInGUIs,
                onClick = {
                    it.player.closeInventory()
                    Data.openedDuelGUI[it.player]?.let { it1 -> it.player.duel(it1, kits) }
                }
            ))
        kitMap[kits] = this
    }

}