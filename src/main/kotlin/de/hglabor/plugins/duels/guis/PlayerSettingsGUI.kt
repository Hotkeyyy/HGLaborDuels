package de.hglabor.plugins.duels.guis

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.data.PlayerSettings
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.items.addLore
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue

object PlayerSettingsGUI {

    fun open(player: Player) {
        val inventory = Bukkit.createInventory(null, 27, "${KColors.DODGERBLUE}Settings")

        for (i in 0..26)
            inventory.setItem(i, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })
        for (i in 10..16)
            inventory.setItem(i, itemStack(Material.BLACK_STAINED_GLASS_PANE) { meta { name = null } })

        inventory.setItem(10, knockbackItem(player))
        inventory.setItem(12, attackSoundItem(player))
        inventory.setItem(14, allowSpectatorsItem(player))
        inventory.setItem(16, chatInFightItem(player))

        player.openInventory(inventory)
    }

    fun knockbackItem(player: Player): ItemStack {
        val item = itemStack(Material.PISTON) {
            meta {
                name = if (player.localization("de")) Localization.SETTINGSGUI_KNOCKBACK_NAME_DE
                else Localization.SETTINGSGUI_KNOCKBACK_NAME_EN

                val settings = PlayerSettings.get(player)
                addLore {
                    if (settings.knockback() == PlayerSettings.Companion.Knockback.NEW) {
                        +"${KColors.MEDIUMPURPLE}1.16"
                        +"${KColors.DIMGRAY}1.8"
                    } else {
                        +"${KColors.MEDIUMPURPLE}1.8"
                        +"${KColors.DIMGRAY}1.16"
                    }
                }
            }
        }
        return item
    }

    fun attackSoundItem(player: Player): ItemStack {
        val item = itemStack(Material.NOTE_BLOCK) {
            meta {
                name = if (player.localization("de")) Localization.SETTINGSGUI_DAMAGESOUND_NAME_DE
                else Localization.SETTINGSGUI_DAMAGESOUND_NAME_EN

                val settings = PlayerSettings.get(player)
                addLore {
                    if (player.localization("de")) {
                        if (settings.ifAttackSound()) {
                            +"${KColors.MEDIUMPURPLE}An"
                            +"${KColors.DIMGRAY}Aus"
                        } else {
                            +"${KColors.MEDIUMPURPLE}Aus"
                            +"${KColors.DIMGRAY}An"
                        }
                    } else {
                        if (settings.ifAttackSound()) {
                            +"${KColors.MEDIUMPURPLE}Enabled"
                            +"${KColors.DIMGRAY}Disabled"
                        } else {
                            +"${KColors.MEDIUMPURPLE}Disabled"
                            +"${KColors.DIMGRAY}Enabled"
                        }
                    }
                }
            }
        }
        return item
    }

    fun allowSpectatorsItem(player: Player): ItemStack {
        val item = itemStack(Material.ENDER_EYE) {
            meta {
                name = Localization.SETTINGSGUI_ALLOWSPECTATORS_NAME

                val settings = PlayerSettings.get(player)
                addLore {
                    if (player.localization("de")) {
                        if (settings.ifAllowSpectators()) {
                            +"${KColors.MEDIUMPURPLE}Erlauben"
                            +"${KColors.DIMGRAY}Verweigern"
                        } else {
                            +"${KColors.MEDIUMPURPLE}Verweigern"
                            +"${KColors.DIMGRAY}Erlauben"
                        }
                    } else {
                        if (settings.ifAllowSpectators()) {
                            +"${KColors.MEDIUMPURPLE}Allowing"
                            +"${KColors.DIMGRAY}Denying"
                        } else {
                            +"${KColors.MEDIUMPURPLE}Denying"
                            +"${KColors.DIMGRAY}Allowing"
                        }
                    }
                }
            }
        }
        return item
    }

    fun chatInFightItem(player: Player): ItemStack {
        val item = itemStack(Material.WRITABLE_BOOK) {
            meta {
                name = if (player.localization("de")) Localization.SETTINGSGUI_CHATINFIGHT_NAME_DE
                else Localization.SETTINGSGUI_CHATINFIGHT_NAME_EN

                val settings = PlayerSettings.get(player)
                addLore {
                    if (player.localization("de")) {
                        if (settings.chatInFight() == PlayerSettings.Companion.Chat.NONE) {
                            +"${KColors.MEDIUMPURPLE}Keine"
                            +"${KColors.DIMGRAY}Alle"
                            +"${KColors.DIMGRAY}Kampf"
                        } else if (settings.chatInFight() == PlayerSettings.Companion.Chat.FIGHT) {
                            +"${KColors.MEDIUMPURPLE}Kampf"
                            +"${KColors.DIMGRAY}Keine"
                            +"${KColors.DIMGRAY}Alle"
                        } else {
                            +"${KColors.MEDIUMPURPLE}Alle"
                            +"${KColors.DIMGRAY}Kampf"
                            +"${KColors.DIMGRAY}Keine"
                        }
                    } else {
                        if (settings.chatInFight() == PlayerSettings.Companion.Chat.NONE) {
                            +"${KColors.MEDIUMPURPLE}None"
                            +"${KColors.DIMGRAY}All"
                            +"${KColors.DIMGRAY}Fight"
                        } else if (settings.chatInFight() == PlayerSettings.Companion.Chat.FIGHT) {
                            +"${KColors.MEDIUMPURPLE}Fight"
                            +"${KColors.DIMGRAY}None"
                            +"${KColors.DIMGRAY}All"
                        } else {
                            +"${KColors.MEDIUMPURPLE}All"
                            +"${KColors.DIMGRAY}Fight"
                            +"${KColors.DIMGRAY}None"
                        }
                    }
                }
            }
        }
        return item
    }

    fun enable() {
        listen<InventoryClickEvent> {
            if (it.view.title == "${KColors.DODGERBLUE}Settings") {
                val player = it.whoClicked as Player
                var clickedSetting = false
                it.isCancelled = true
                if (it.currentItem != null) {
                    val settings = PlayerSettings.get(player)

                    if (it.currentItem!!.type == Material.PISTON) {
                        if (settings.knockback() == PlayerSettings.Companion.Knockback.NEW) {
                            settings.setKnockback(PlayerSettings.Companion.Knockback.OLD)
                            //player.setMetadata("oldKnockback", FixedMetadataValue(Manager.INSTANCE, ""))
                        } else if (settings.knockback() == PlayerSettings.Companion.Knockback.OLD) {
                            settings.setKnockback(PlayerSettings.Companion.Knockback.NEW)
                            //player.removeMetadata("oldKnockback", Manager.INSTANCE)
                        }
                        it.inventory.setItem(it.rawSlot, knockbackItem(player))
                        clickedSetting = true
                    }

                    if (it.currentItem!!.type == Material.NOTE_BLOCK) {
                            settings.setAttackSound(!settings.ifAttackSound())
                        it.inventory.setItem(it.rawSlot, attackSoundItem(player))
                        clickedSetting = true
                    }

                    if (it.currentItem!!.type == Material.ENDER_EYE) {
                        settings.setAllowSpectators(!settings.ifAllowSpectators())
                        it.inventory.setItem(it.rawSlot, allowSpectatorsItem(player))
                        clickedSetting = true
                    }

                    if (it.currentItem!!.type == Material.WRITABLE_BOOK) {
                        if (settings.chatInFight() == PlayerSettings.Companion.Chat.ALL) 
                            settings.setChatInFight(PlayerSettings.Companion.Chat.FIGHT)
                        else if (settings.chatInFight() == PlayerSettings.Companion.Chat.FIGHT)
                            settings.setChatInFight(PlayerSettings.Companion.Chat.NONE)
                        else if (settings.chatInFight() == PlayerSettings.Companion.Chat.NONE)
                        settings.setChatInFight(PlayerSettings.Companion.Chat.ALL)
                        it.inventory.setItem(it.rawSlot, chatInFightItem(player))
                        clickedSetting = true
                    }
                
                    if (clickedSetting)
                        player.playSound(player.location, Sound.UI_BUTTON_CLICK, 5f, 1f)
                }   
            }
        }
    }
}


