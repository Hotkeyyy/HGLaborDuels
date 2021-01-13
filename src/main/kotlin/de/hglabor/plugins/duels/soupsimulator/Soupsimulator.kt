package de.hglabor.plugins.duels.soupsimulator

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.kits.KitUtils
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.reset
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import kotlin.random.Random

enum class SoupsimulatorLevel { EASY, MEDIUM, HARD, BONUS }
enum class SoupsimulatorTasks { SOUP, REFILL, RECRAFT }

val SoupsimulatorLevel.endsAfterTime
    get() = when (this) {
        SoupsimulatorLevel.HARD, SoupsimulatorLevel.BONUS -> true; else -> false
    }

fun Player.isInSoupsimulator(): Boolean {
    return Soupsimulator.inSoupsimulator.contains(player)
}

object Soupsimulator {

    val inSoupsimulator = arrayListOf<Player>()
    val level = hashMapOf<Player, SoupsimulatorLevel>()
    val task = hashMapOf<Player, SoupsimulatorTasks>()

    val taskStart = hashMapOf<Player, Long>()
    val timer = hashMapOf<Player, Int>()
    val score = hashMapOf<Player, Int>()
    val refills = hashMapOf<Player, Int>()
    val recrafts = hashMapOf<Player, Int>()
    val wrongHotkeys = hashMapOf<Player, Int>()

    fun countdown(player: Player) {
        Data.challengeKit.remove(player)
        Data.challenged.remove(player)
        player.inventory.clear()
        player.isGlowing = false
        player.sendTitle("${KColors.LIMEGREEN}3", "§a", 3, 13, 3)
        player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 3f, 5f)

        object : BukkitRunnable() {
            override fun run() {
                player.sendTitle("${KColors.YELLOW}2", "§e", 3, 13, 3)
                player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 3f, 3f)
            }
        }.runTaskLater(Manager.INSTANCE, 20)

        object : BukkitRunnable() {
            override fun run() {
                player.sendTitle("${KColors.RED}1", "§c", 3, 13, 3)
                player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 3f, 2f)
            }
        }.runTaskLater(Manager.INSTANCE, 40)

        object : BukkitRunnable() {
            override fun run() {
                player.sendTitle(" ", " ", 1, 1, 1)
                player.closeInventory()
                start(player)
            }
        }.runTaskLater(Manager.INSTANCE, 60)
    }

    fun start(player: Player) {
        inSoupsimulator.add(player)
        timer[player] = 0
        score[player] = 0
        refills[player] = 0
        recrafts[player] = 0
        wrongHotkeys[player] = 0
        player.inventory.setItem(
            0,
            itemStack(Material.STONE_SWORD) { meta { name = "${KColors.DEEPSKYBLUE}Soupsimulator" } })
        giveNextTask(player)
        if (level[player] == SoupsimulatorLevel.EASY)
            player.sendTitle("${KColors.CORNSILK}/Leave", "${KColors.GRAY}To leave", 5, 25, 5)

        runTimer(player)
    }

    fun giveNextTask(player: Player) {
        player.closeInventory()
        player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 3f, 1f)
        if (level[player] == SoupsimulatorLevel.BONUS) {
            for (i in 1..player.inventory.size) {
                player.inventory.setItem(i, ItemStack(Material.AIR))
            }
            val nextTask = Random.nextInt(30)
            if (nextTask < 21) {
                task[player] = SoupsimulatorTasks.SOUP
            } else if (nextTask < 26) {
                player.sendTitle("${KColors.DODGERBLUE}Refill", "§a", 1, 15, 1)
                task[player] = SoupsimulatorTasks.REFILL
                taskStart[player] = System.currentTimeMillis()
                for (i in 9..35) {
                    player.inventory.setItem(i, ItemStack(Material.MUSHROOM_STEW))
                }
                player.level = 10
            } else {
                player.sendTitle("${KColors.MEDIUMPURPLE}Recraft", "§a", 1, 15, 1)
                KitUtils.giveRecraft(player, 64)
                task[player] = SoupsimulatorTasks.RECRAFT
                taskStart[player] = System.currentTimeMillis()
                player.level = 10
            }
        }

        if (task[player] == SoupsimulatorTasks.SOUP || level[player] != SoupsimulatorLevel.BONUS) {
            val i = Random.nextInt(8) + 1
            task[player] = SoupsimulatorTasks.SOUP
            player.inventory.setItem(i, ItemStack(Material.MUSHROOM_STEW))
            player.level = i + 1
        }
    }

    fun sendRecraftRefillTime(player: Player) {
        val jetzt = System.currentTimeMillis()
        val anfang: Long = taskStart[player]!!
        val time = (jetzt - anfang).toInt()
        val seconds = time / 1000
        val millisec = time - seconds * 1000

        val refillPrefix = " ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}Refill ${KColors.DARKGRAY}» ${KColors.GRAY}"
        val recraftPrefix = " ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}Recraft ${KColors.DARKGRAY}» ${KColors.GRAY}"

        if (task[player] == SoupsimulatorTasks.REFILL)
            if (player.localization("de"))
                player.sendMessage("${refillPrefix}Du hast ${KColors.DODGERBLUE}$seconds${KColors.DARKGRAY}:${KColors.DODGERBLUE}$millisec${(if (seconds == 1) " Sekunde" else " Sekunden")} ${KColors.GRAY}gebraucht um zu Refillen.")
            else
                player.sendMessage("${refillPrefix}It took you ${KColors.DODGERBLUE}$seconds${KColors.DARKGRAY}:${KColors.DODGERBLUE}$millisec${(if (seconds == 1) " second" else " seconds")} ${KColors.GRAY}to refill.")

        if (task[player] == SoupsimulatorTasks.RECRAFT)
            if (player.localization("de"))
                player.sendMessage("${recraftPrefix}Du hast ${KColors.DODGERBLUE}$seconds${KColors.DARKGRAY}:${KColors.DODGERBLUE}$millisec${(if (seconds == 1) " Sekunde" else " Sekunden")} ${KColors.GRAY}gebraucht um zu Recraften.")
            else
                player.sendMessage("${recraftPrefix}It took you ${KColors.DODGERBLUE}$seconds${KColors.DARKGRAY}:${KColors.DODGERBLUE}$millisec${(if (seconds == 1) " second" else " seconds")} ${KColors.GRAY}to recraft.")
    }

    fun runTimer(player: Player) {
        object : BukkitRunnable() {
            override fun run() {
                if (!inSoupsimulator.contains(player)) {
                    player.reset(); cancel(); return
                }
                val s = timer[player]!! / 10
                val ms = timer[player]!! % 10

                player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    *TextComponent.fromLegacyText("${KColors.DODGERBLUE}$s.$ms ${KColors.GRAY}Sec ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}${score[player]}")
                )

                if (level[player]!!.endsAfterTime) {
                    if (s == 30) {
                        player.reset()
                        if (player.localization("de"))
                            player.sendMessage(Localization.SOUPSIMULATOR_SURVIVED_DE)
                        else
                            player.sendMessage(Localization.SOUPSIMULATOR_SURVIVED_EN)
                        end(player)
                        cancel()
                        return
                    }
                }
                timer[player] = timer[player]!! + 1
            }
        }.runTaskTimer(Manager.INSTANCE, 0, 2)
    }

    fun end(player: Player) {
        player.sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                         ")
        if (player.localization("de"))
            player.sendMessage(Localization.SOUPSIMULATOR_END_SCORE_DE.replace("%score%", score[player].toString()))
        else
            player.sendMessage(Localization.SOUPSIMULATOR_END_SCORE_EN.replace("%score%", score[player].toString()))

        if (level[player] == SoupsimulatorLevel.BONUS) {
            player.sendMessage(Localization.SOUPSIMULATOR_END_REFILLS.replace("%refills%", refills[player].toString()))
            player.sendMessage(
                Localization.SOUPSIMULATOR_END_RECRAFTS.replace(
                    "%recrafts%",
                    refills[player].toString()
                )
            )
        }

        if (player.localization("de"))
            player.sendMessage(
                Localization.SOUPSIMULATOR_END_WRONGHOTKEYS_DE.replace(
                    "%wrongHotkeys%",
                    wrongHotkeys[player].toString()
                )
            )
        else
            player.sendMessage(
                Localization.SOUPSIMULATOR_END_WRONGHOTKEYS_EN.replace(
                    "%wrongHotkeys%",
                    wrongHotkeys[player].toString()
                )
            )

        player.sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                         ")
        inSoupsimulator.remove(player)
        timer.remove(player)
        score.remove(player)
        wrongHotkeys.remove(player)
        level.remove(player)
        task.remove(player)
        player.reset()
    }
}

