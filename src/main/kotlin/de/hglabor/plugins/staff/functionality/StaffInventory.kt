package de.hglabor.plugins.staff.functionality

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.bukkit.heal
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.utils.mark
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object StaffInventory {
    fun unvanishItem(player: Player): ItemStack {
        val item = itemStack(Material.RED_DYE) {
            amount = 1
            meta {
                name = if (player.localization("de"))
                    Localization.STAFFINVENTORY_TOGGLE_INVISIBILITY_DE
                else
                    Localization.STAFFINVENTORY_TOGGLE_INVISIBILITY_EN
            }
            mark("unvanishitem")
        }
        return item
    }

    fun vanishItem(player: Player): ItemStack {
       val item = itemStack(Material.SCUTE) {
            amount = 1
            meta {
                name = if (player.localization("de"))
                    Localization.STAFFINVENTORY_TOGGLE_VISIBILITY_DE
                else
                    Localization.STAFFINVENTORY_TOGGLE_VISIBILITY_EN
            }
            mark("vanishitem")
        }
        return item
    }

    fun giveItems(p: Player) {
        for (all in Bukkit.getOnlinePlayers()) {
            p.showPlayer(Manager.INSTANCE, all)
        }
        p.gameMode = GameMode.CREATIVE
        p.heal()
        p.closeInventory()
        p.inventory.clear()
        p.updateInventory()

        p.inventory.helmet = itemStack(Material.TURTLE_HELMET) {
            meta {
                name = "${KColors.DARKPURPLE}Staff Helmet"
            }
            mark("staffhelmet")
        }

        p.inventory.setItem(3, unvanishItem(p))

        p.inventory.setItem(5, itemStack(Material.BOOK) {
            amount = 1
            meta {
                name = if (p.localization("de"))
                    Localization.STAFFINVENTORY_INVSEE_DE
                else
                    Localization.STAFFINVENTORY_INVSEE_EN
            }
            mark("invseeitem")
        })
    }
}