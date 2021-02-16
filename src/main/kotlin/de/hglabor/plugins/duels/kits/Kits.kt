package de.hglabor.plugins.duels.kits

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.guis.ChooseKitGUI
import de.hglabor.plugins.duels.guis.QueueGUI
import de.hglabor.plugins.duels.kits.kit.*
import de.hglabor.plugins.duels.kits.specials.Specials
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.extensions.bukkit.feedSaturate
import net.axay.kspigot.extensions.bukkit.heal
import net.axay.kspigot.gui.ForInventoryFiveByNine
import net.axay.kspigot.gui.elements.GUICompoundElement
import net.axay.kspigot.items.*
import net.axay.kspigot.utils.hasMark
import net.axay.kspigot.utils.mark
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

enum class KitType { SOUP, POT, NONE }

enum class Kits {
    ANCHOR, ARCHER, CLASSIC, EHG, FEAST, GLADIATOR, ICEFISHING,
    JUMPANDRUN, NINJA, NODEBUFF, ONEBAR, ONLYSWORD, RANDOM, SPEED,
    SUMO, UHC, UNDERWATER;

    companion object {
        val queueItems = arrayListOf<GUICompoundElement<ForInventoryFiveByNine>>()
        val cooldownStart = hashMapOf<Player, Long>()
        val cooldownTime = hashMapOf<Player, Long>()
        val cooldownTask = hashMapOf<Player, BukkitTask>()
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

        fun ItemStack.getKit(): Kits? {
            val itemMeta = this.itemMeta
            for (kit in values()) {
                if (itemMeta.name!!.contains(kit.info.name)) {
                    return kit
                }
            }
            return null
        }

        fun random(): Kits {
            val kits = values().toMutableList()
            kits.remove(RANDOM)
            return kits.random()
        }

        fun enable() {
            values().forEach { inGame[it] = arrayListOf(); queue[it] = arrayListOf() }

            Anchor().enable()
            Archer().enable()
            Classic().enable()
            EHG().enable()
            Feast().enable()
            Gladiator().enable()
            IceFishing().enable()
            JumpAndRun().enable()
            Ninja().enable()
            NoDebuff().enable()
            Onebar().enable()
            OnlySword().enable()
            Random().enable()
            Speed().enable()
            Sumo().enable()
            UHC().enable()
            Underwater().enable()
            Specials.enable()

            QueueGUI.updateContents()
        }

        fun setCooldown(player: Player, seconds: Long) {
            val jetzt: Long = System.currentTimeMillis()
            cooldownStart[player] = jetzt
            cooldownTime[player] = seconds

            cooldownTask[player] = object : BukkitRunnable() {
                override fun run() {
                    if (player.localization("de"))
                        player.sendMessage(Localization.CAN_USE_KIT_AGAIN_DE)
                    else
                        player.sendMessage(Localization.CAN_USE_KIT_AGAIN_EN)
                }
            }.runTaskLater(Manager.INSTANCE, 20 * seconds)
        }

        fun hasCooldown(player: Player): Boolean {
            val jetzt: Long = System.currentTimeMillis()
            if (cooldownStart.containsKey(player) && cooldownTime.containsKey(player)) {
                val be = cooldownStart[player]
                val rest: Long = (be!! + (1000 * cooldownTime[player]!!)) - jetzt

                if (rest > 0) {
                    val second: Int = (rest / 1000).toInt()
                    val ms = rest % 1000
                    if (player.localization("de"))
                        player.sendMessage("${Localization.PREFIX}Du musst noch ${KColors.DODGERBLUE}$second${KColors.DARKGRAY}:${KColors.DODGERBLUE}$ms ${(if (second == 1) "Sekunde" else "Sekunden")} ${KColors.GRAY}warten.")
                    else
                        player.sendMessage("${Localization.PREFIX}You still have to wait ${KColors.DODGERBLUE}$second${KColors.DARKGRAY}:${KColors.DODGERBLUE}$ms ${(if (second == 1) "second" else "seconds")}${KColors.GRAY}.")
                    return true
                }
            }
            return false
        }

        fun removeCooldown(player: Player) {
            cooldownTask[player]?.cancel()
            cooldownTask.remove(player)
            cooldownTime.remove(player)
            cooldownStart.remove(player)
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
