package de.hglabor.plugins.duels.functionality

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.appear
import net.axay.kspigot.extensions.bukkit.heal
import net.axay.kspigot.items.*
import net.axay.kspigot.utils.mark
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

object CreateArenaInventory {

    fun giveItems(p: Player) {
        for (all in Bukkit.getOnlinePlayers()) {
            p.showPlayer(Manager.INSTANCE, all)
        }
        p.gameMode = GameMode.CREATIVE
        p.appear()
        p.heal()
        p.closeInventory()
        p.inventory.clear()
        p.updateInventory()
        p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)!!.baseValue = 0.0

        p.inventory.setItem(8, itemStack(Material.REPEATING_COMMAND_BLOCK) {
            amount = 1
            meta {
                name = if (p.localization("de"))
                    Localization.ARENA_INVENTORY_ITEM_NAME_DE
                else
                    Localization.ARENA_INVENTORY_ITEM_NAME_EN
            }
            mark("createarenaitem")
        })
    }

}