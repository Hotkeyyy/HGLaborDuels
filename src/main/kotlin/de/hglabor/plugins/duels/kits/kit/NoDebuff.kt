package de.hglabor.plugins.duels.kits.kit

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.guis.ChooseKitGUI
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.duel
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.events.isRightClick
import net.axay.kspigot.items.itemStack
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType
import org.bukkit.scheduler.BukkitRunnable

class NoDebuff : Kit(Kits.NODEBUFF) {

    companion object {
        fun enchantArmor(material: Material): ItemStack {
            return itemStack(material) {
                addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                addEnchantment(Enchantment.DURABILITY, 3)
                if (material.toString().toLowerCase().contains("boots"))
                    addEnchantment(Enchantment.PROTECTION_FALL, 4)
            }

        }
    }

    override val name = "NoDamage"
    override val itemInGUIs = Kits.guiItem(Material.SPLASH_POTION, name, "PotPvP")
    override val arenaTag = ArenaTags.NONE
    override val type = KitType.NONE
    override val specials = listOf("pearlcd")

    override fun giveKit(player: Player) {
        player.inventory.clear()
        player.inventory.helmet = enchantArmor(Material.DIAMOND_HELMET)
        player.inventory.chestplate = enchantArmor(Material.DIAMOND_CHESTPLATE)
        player.inventory.leggings = enchantArmor(Material.DIAMOND_LEGGINGS)
        player.inventory.boots = enchantArmor(Material.DIAMOND_BOOTS)

        player.inventory.setItem(0, KitUtils.sword(Material.DIAMOND_SWORD, true))
        player.inventory.setItem(1, ItemStack(Material.ENDER_PEARL, 16))

        val pot = ItemStack(Material.SPLASH_POTION)
        val potMeta = pot.itemMeta as PotionMeta
        potMeta.basePotionData = PotionData(PotionType.INSTANT_HEAL, false, true)
        pot.itemMeta = potMeta

        for (i in 0..35) {
            if (player.inventory.getItem(i) == null) {
                player.inventory.setItem(i, pot)
            }
        }

        player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 1))
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    override fun enable() {
        ChooseKitGUI.addContent(
            ChooseKitGUI.KitsGUICompoundElement(
                itemInGUIs,
                onClick = {
                    it.player.closeInventory()
                    Data.openedDuelGUI[it.player]?.let { it1 -> it.player.duel(it1, kits) }
                }
            ))

        listen<PlayerInteractEvent> {
            val player = it.player
            if (it.action.isRightClick) {
                if (player.isInFight()) {
                    val duel = Data.duelFromPlayer(player)
                    if (kitMap[duel.kit]!!.specials!!.contains("pearlcd")) {
                        if (player.inventory.itemInMainHand.type == Material.ENDER_PEARL) {
                            player.sendMessage("pearlcd")
                            val jetzt: Long = System.currentTimeMillis()
                            if (Kits.cooldown.containsKey(player)) {
                                val be = Kits.cooldown[player]
                                val rest: Long = (be!! + (1000 * 5)) - jetzt

                                if (rest > 0) {
                                    val second: Int = (rest / 1000).toInt()
                                    val ms = rest % 1000
                                    if (player.localization("de"))
                                        player.sendMessage("${Localization.PREFIX}Du musst noch ${KColors.DODGERBLUE}$second${KColors.DARKGRAY}:${KColors.DODGERBLUE}$ms ${(if (second == 1) " Sekunde" else " Sekunden")} ${KColors.GRAY}warten.")
                                    else
                                        player.sendMessage("${Localization.PREFIX}You still have to wait ${KColors.DODGERBLUE}$second${KColors.DARKGRAY}:${KColors.DODGERBLUE}$ms ${(if (second == 1) " second" else " seconds")}${KColors.GRAY}.")
                                    it.isCancelled = true
                                }
                            } else {
                                Kits.cooldown[player] = jetzt

                                object : BukkitRunnable() {
                                    override fun run() {
                                        if (player.localization("de"))
                                            player.sendMessage(Localization.CAN_USE_KIT_AGAIN_DE)
                                        else
                                            player.sendMessage(Localization.CAN_USE_KIT_AGAIN_EN)
                                    }
                                }.runTaskLater(Manager.INSTANCE, 20 * 5)
                            }
                        }
                    }
                }
            }
        }

        kitMap[kits] = this
    }
}