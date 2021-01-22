package de.hglabor.plugins.duels.functionality

import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.party.Partys.isInParty
import de.hglabor.plugins.duels.party.Partys.playerParty
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import net.axay.kspigot.extensions.bukkit.appear
import net.axay.kspigot.extensions.bukkit.heal
import net.axay.kspigot.items.flag
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.utils.mark
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

object PartyInventory {
    fun giveItems(p: Player) {
        p.appear()
        p.heal()
        p.inventory.clear()
        p.updateInventory()

        p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)!!.baseValue = 0.0

        if (p.isInParty() && playerParty[p]?.leader == p) {
            p.inventory.setItem(3, itemStack(Material.NETHERITE_SWORD) {
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

            p.inventory.setItem(5, itemStack(Material.WITHER_ROSE) {
                amount = 1
                meta {
                    name = if (p.localization("de"))
                        Localization.PARTY_INVENTORY_PARTY_GAME_ITEM_NAME_DE
                    else
                        Localization.PARTY_INVENTORY_PARTY_GAME_ITEM_NAME_EN
                }
                mark("partygame")
            })
        }

        p.inventory.setItem(4, itemStack(Material.PAPER) {
            amount = 1
            meta {
                name = if (p.localization("de"))
                    Localization.PARTY_INVENTORY_INFO_ITEM_NAME_DE
                else
                    Localization.PARTY_INVENTORY_INFO_ITEM_NAME_EN
            }
            mark("partyinfo")
        })

        p.inventory.setItem(7, itemStack(Material.REPEATER) {
            amount = 1
            meta {
                name = if (p.localization("de"))
                    Localization.MAIN_INVENTORY_SETTINGS_ITEM_NAME_DE
                else
                    Localization.MAIN_INVENTORY_SETTINGS_ITEM_NAME_EN
            }
            mark("settings")
        })

        p.inventory.setItem(1, itemStack(Material.SUSPICIOUS_STEW) {
            amount = 1
            meta {
                name = if (p.localization("de"))
                    Localization.MAIN_INVENTORY_SOUPSIMULATOR_ITEM_NAME_DE
                else
                    Localization.MAIN_INVENTORY_SOUPSIMULATOR_ITEM_NAME_EN
            }
            mark("soupsim")
        })
    }
}