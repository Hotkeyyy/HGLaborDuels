package de.hglabor.plugins.staff.functionality

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.localization.Localization
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

object StaffInventory {
    fun unvanishItem(player: Player) = itemStack(Material.RED_DYE) {
        amount = 1
        meta {
            name = Localization.getMessage("staff.inventory.item.unvanish.name", player)
        }
        mark("unvanishitem")
    }

    fun vanishItem(player: Player) = itemStack(Material.SCUTE) {
        amount = 1
        meta {
            name = Localization.getMessage("staff.inventory.item.vanish.name", player)
        }
        mark("vanishitem")
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
                name = Localization.getMessage("staff.inventory.item.invsee.name", p)
            }
            mark("invseeitem")
        })
    }
}