package de.hglabor.plugins.duels.functionality

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.utils.Localization
import net.axay.kspigot.extensions.bukkit.appear
import net.axay.kspigot.extensions.bukkit.heal
import net.axay.kspigot.items.flag
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.utils.mark
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

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

        p.inventory.setItem(5, itemStack(Material.POPPY) {
            meta {
                name = Localization.getMessage("mainInventory.partyItem.name", p)
            }
            mark("createparty")
        })

        p.inventory.setItem(3, itemStack(Material.NETHERITE_SWORD) {
            addEnchantment(Enchantment.DURABILITY, 1)
            meta {
                name = Localization.getMessage("mainInventory.duelItem.name", p)
                isUnbreakable = true
                flag(ItemFlag.HIDE_UNBREAKABLE)
                flag(ItemFlag.HIDE_ENCHANTS)
            }
            mark("duelitem")
        })

        p.inventory.setItem(4, itemStack(Material.CLOCK) {
            meta {
                name = Localization.getMessage("mainInventory.unrankedQueueItem.name", p)
            }
            mark("queue")
        })

        p.inventory.setItem(1, itemStack(Material.SUSPICIOUS_STEW) {
            meta {
                name = Localization.getMessage("mainInventory.soupsimulatorItem.name", p)
            }
            mark("soupsim")
        })

        p.inventory.setItem(7, itemStack(Material.REPEATER) {
            meta {
                name = Localization.getMessage("mainInventory.settingsItem.name", p)
            }
            mark("settings")
        })
    }
}