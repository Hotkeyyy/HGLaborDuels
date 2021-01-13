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

class Gladiator : Kit(Kits.GLADIATOR) {
    override val name = "Gladiator"
    override fun itemInGUIs() = Kits.guiItem(Material.IRON_BARS, name, "Soup")
    override val arenaTag = ArenaTags.GLADIATOR
    override val type = KitType.SOUP
    override val specials = listOf(null)

    override fun giveKit(player: Player) {
        player.inventory.clear()
        KitUtils.armor(
            player, Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS
        )
        player.inventory.setItem(0, KitUtils.sword(Material.DIAMOND_SWORD, true))

        player.inventory.setItem(1, ItemStack(Material.LAVA_BUCKET))
        player.inventory.setItem(16, ItemStack(Material.LAVA_BUCKET))
        player.inventory.setItem(2, ItemStack(Material.WATER_BUCKET))
        player.inventory.setItem(17, ItemStack(Material.WATER_BUCKET))
        player.inventory.setItem(7, ItemStack(Material.OAK_PLANKS, 64))
        player.inventory.setItem(8, ItemStack(Material.COBBLESTONE_WALL, 64))
        player.inventory.setItem(17, ItemStack(Material.IRON_PICKAXE))
        player.inventory.setItem(26, ItemStack(Material.IRON_AXE))
        KitUtils.giveRecraft(player, 64)
        KitUtils.giveSoups(player)

        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
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