package de.hglabor.plugins.duels.kits.kit

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.guis.ChooseKitGUI
import de.hglabor.plugins.duels.guis.QueueGUI
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
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

    override val name = "NoDebuff"
    override val itemInGUIs = Kits.guiItem(Material.SPLASH_POTION, name, "PotPvP")
    override val arenaTag = ArenaTags.NONE
    override val type = KitType.NONE
    override val specials = listOf(Specials.PEARLCOOLDOWN)

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
        kitMap[kits] = this
        ChooseKitGUI.addContent(ChooseKitGUI.KitsGUICompoundElement(itemInGUIs))

        listen<PlayerInteractEvent> {
            val player = it.player
            if (it.action.isRightClick) {
                if (player.isInFight()) {
                    val duel = Data.duelFromPlayer(player)
                    if (kitMap[duel.kit]!!.specials.contains(Specials.PEARLCOOLDOWN)) {
                        if (player.inventory.itemInMainHand.type == Material.ENDER_PEARL) {
                            if (!Kits.hasCooldown(player))
                                Kits.setCooldown(player, 15)
                            else
                                it.isCancelled = true
                        }
                    }
                }
            }
        }
    }
}