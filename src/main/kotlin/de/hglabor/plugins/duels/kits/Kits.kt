package de.hglabor.plugins.duels.kits

import de.hglabor.plugins.duels.kits.kit.*
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.bukkit.feedSaturate
import net.axay.kspigot.extensions.bukkit.heal
import net.axay.kspigot.items.*
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

enum class KitType { SOUP, POT, NONE }
enum class Specials { NINJA, NODAMAGE, DEADINWATER, PEARLCOOLDOWN, HITCOOLDOWN }

enum class Kits {
    ANCHOR,
    ARCHER,
    CLASSIC,
    EHG,
    FEAST,
    GLADIATOR,
    ICEFISHING,
    NINJA,
    NODEBUFF,
    ONEBAR,
    ONLYSWORD,
    SPEED,
    SUMO,
    UNDERWATER;

    companion object {
        val cooldown = hashMapOf<Player, Long>()
        val queue = hashMapOf<Kits, ArrayList<Player>>()
        val playerQueue = hashMapOf<Player, Kits>()
        val inGame = hashMapOf<Kits, ArrayList<Player>>()

        fun guiItem(material: Material, name: String, description: String?): ItemStack {
            return itemStack(material) {
                meta {
                    this.name = "${KColors.DEEPSKYBLUE}$name"
                    if (description != null)
                        lore = description.toLoreList(KColors.MEDIUMPURPLE)
                    flag(ItemFlag.HIDE_ATTRIBUTES)
                    flag(ItemFlag.HIDE_POTION_EFFECTS)
                }
            }
        }

        fun enable() {
            Anchor().enable()
            Archer().enable()
            Classic().enable()
            EHG().enable()
            Feast().enable()
            Gladiator().enable()
            IceFishing().enable()
            Ninja().enable()
            OnlySword().enable()
            NoDebuff().enable()
            Onebar().enable()
            Speed().enable()
            Sumo().enable()
            Underwater().enable()

            values().forEach { inGame[it] = arrayListOf(); queue[it] = arrayListOf() }
        }

        fun Player.giveKit(kit: Kits) {
            val player: Player = player!!
            player.heal()
            player.feedSaturate()
            player.exp = 0f
            player.level = 0
            for (effect in player.activePotionEffects) {
                player.removePotionEffect(effect.type)
            }

            kitMap[kit]!!.giveKit(player)
        }

        val Kits.info get() = kitMap[this]!!
    }
}
