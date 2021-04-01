package de.hglabor.plugins.duels.kits.kit.trash

import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.utils.KitUtils
import net.axay.kspigot.items.itemStack
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object Archer : AbstractKit() {

    override val name = "Archer"
    override val itemInGUI = Kits.guiItem(Material.BOW, name)
    override val type = KitType.BOW
    override val category = KitCategory.TRASH
    override val isMainKit = true

    override val armor = KitUtils.armor(
        Material.LEATHER_HELMET,
        Material.CHAINMAIL_CHESTPLATE,
        Material.LEATHER_LEGGINGS,
        Material.LEATHER_BOOTS
    )

    override val defaultInventory = mutableMapOf(
        0 to itemStack(Material.BOW) { addEnchantment(Enchantment.ARROW_INFINITE, 1) },
        1 to ItemStack(Material.GOLDEN_APPLE, 8),
        7 to ItemStack(Material.IRON_AXE, 1),
        8 to ItemStack(Material.OAK_PLANKS, 24),
        9 to ItemStack(Material.ARROW)
    )
}