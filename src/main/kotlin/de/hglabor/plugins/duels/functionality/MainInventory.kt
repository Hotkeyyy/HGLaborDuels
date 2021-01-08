package de.hglabor.plugins.duels.functionality

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import de.hglabor.plugins.staff.utils.StaffData.isStaff
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.appear
import net.axay.kspigot.extensions.bukkit.heal
import net.axay.kspigot.items.*
import net.axay.kspigot.utils.mark
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import java.util.*

object MainInventory {

    fun giveItems(p: Player) {
        for (all in Bukkit.getOnlinePlayers()) {
            p.showPlayer(Manager.INSTANCE, all)
        }
        p.appear()
        p.heal()
        p.closeInventory()
        p.inventory.clear()
        p.updateInventory()

        p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)!!.baseValue = 0.0

        p.inventory.setItem(2, itemStack(Material.NETHERITE_SWORD) {
            amount = 1
            addEnchantment(Enchantment.DURABILITY, 1)
            meta {
                name = if (p.localization("de"))
                    Localization.MAIN_INVENTORY_DUEL_ITEM_NAME_DE
                else
                    Localization.MAIN_INVENTORY_DUEL_ITEM_NAME_EN
                isUnbreakable = true
                flag(ItemFlag.HIDE_UNBREAKABLE)
                flag(ItemFlag.HIDE_ENCHANTS)
            }
            mark("duelitem")
        })

        p.inventory.setItem(6, itemStack(Material.SUSPICIOUS_STEW) {
            amount = 1
            meta {
                name = if (p.localization("de"))
                    Localization.MAIN_INVENTORY_SOUPSIMULATOR_ITEM_NAME_DE
                else
                    Localization.MAIN_INVENTORY_SOUPSIMULATOR_ITEM_NAME_EN
            }
            mark("soupsim")
        })

        p.inventory.setItem(8, itemStack(Material.REPEATER) {
            amount = 1
            meta {
                name = if (p.localization("de"))
                    Localization.MAIN_INVENTORY_SETTINGS_ITEM_NAME_DE
                else
                    Localization.MAIN_INVENTORY_SETTINGS_ITEM_NAME_EN
            }
            mark("settings")
        })


    }

}