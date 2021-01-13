package de.hglabor.plugins.duels.kits.kit

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.guis.ChooseKitGUI
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.duel
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Classic : Kit(Kits.CLASSIC) {
    override val name = "Classic"
    override fun itemInGUIs() = Kits.guiItem(Material.DIAMOND_CHESTPLATE, name, "1.16 Cooldown")
    override val arenaTag = ArenaTags.NONE
    override val type = KitType.NONE
    override val specials = listOf(Specials.HITCOOLDOWN)

    override fun giveKit(player: Player) {
        player.inventory.clear()
        KitUtils.armor(player, Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS)

        player.inventory.setItemInOffHand(ItemStack(Material.SHIELD))

        player.inventory.setItem(0, KitUtils.sword(Material.DIAMOND_SWORD, false))
        player.inventory.setItem(1, ItemStack(Material.IRON_AXE))
        player.inventory.setItem(2, ItemStack(Material.BOW))
        player.inventory.setItem(3, ItemStack(Material.CROSSBOW))
        player.inventory.setItem(7, ItemStack(Material.GOLDEN_APPLE, 12))
        player.inventory.setItem(8, ItemStack(Material.ARROW, 32))

        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 4.0
    }

    override fun enable() {
        ChooseKitGUI.addContent(
            ChooseKitGUI.KitsGUICompoundElement(
                itemInGUIs(),
                onClick = {
                    it.player.closeInventory()
                    Data.openedDuelGUI[it.player]?.let { it1 -> it.player.duel(it1, kits) }
                }
            ))
        kitMap[kits] = this
    }

}