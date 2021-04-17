package de.hglabor.plugins.soupsimulator

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.duel.AbstractDuel
import de.hglabor.plugins.duels.utils.KitUtils
import de.hglabor.plugins.duels.utils.Localization
import de.hglabor.plugins.duels.utils.sendMsg
import de.hglabor.plugins.duels.player.DuelsPlayer
import de.hglabor.plugins.soupsimulator.Soupsim.endsAfterTime
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.reset
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.bukkit.actionBar
import net.axay.kspigot.runnables.task
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import kotlin.random.Random

class Soupsimulator(val player: Player) {

    companion object {
        fun get(player: Player): Soupsimulator? {
            return Data.soupsimulator[player]
        }

        fun forceStop(player: Player) {
            get(player)?.stop()
        }
    }

    lateinit var level: SoupsimulatorLevel
    lateinit var Task: SoupsimulatorTasks
    var state = Data.GameState.STARTING

    var timer = 0
    var taskStart: Long? = null
    var score = 0
    var refills = 0
    var recrafts = 0
    var wrongHotkeys = 0

    fun start(level: SoupsimulatorLevel) {
        this.level = level
        Data.challengeKit.remove(player)
        Data.challenged.remove(player)
        player.inventory.clear()
        player.isGlowing = false

        sendCountdown()

        if (level == SoupsimulatorLevel.EASY)
            player.sendTitle("${KColors.CORNSILK}/Surrender", Localization.getMessage("soupsimulator.title.toLeave", player), 10, 30, 10)

        Data.soupsimulator[player] = this
        player.inventory.setItem(0, ItemStack(Material.STONE_SWORD))
    }

    private fun sendCountdown() {
        state = Data.GameState.COUNTDOWN
        var count = 3
        var colorcode = 'a'
        task(true, 20, 20, 4) {
            if (state != Data.GameState.COUNTDOWN) {
                it.cancel(); return@task
            }
            if (count == 2) colorcode = 'e'
            if (count == 1) colorcode = 'c'
            if (count != 0) {
                player.sendTitle("§$colorcode$count", "§$colorcode", 3, 13, 3)
                player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 3f, 2f)
            } else {
                player.sendTitle(Localization.getMessage("soupsimulator.title.go", player), "§b", 3, 13, 3)
                player.closeInventory()
                runTimer()
                nextTask()
                state = Data.GameState.INGAME
            }
            count--
        }
    }

    private fun runTimer() {
        object : BukkitRunnable() {
            override fun run() {
                if (state != Data.GameState.INGAME) {
                    cancel(); return
                }
                val s = timer / 10
                val ms = timer % 10

                player.actionBar("${KColors.DODGERBLUE}$s.$ms ${KColors.GRAY}Sec ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}${score}")

                if (level.endsAfterTime) {
                    if (s == 30) {
                        player.reset()
                        player.sendMsg("soupsimulator.finish.survived")
                        end()
                        cancel()
                        return
                    }
                }
                timer += 1
            }
        }.runTaskTimer(Manager.INSTANCE, 0, 2)
    }

    fun nextTask() {
        for (i in 0..player.inventory.size)
            if (player.inventory.getItem(i)?.type != Material.STONE_SWORD)
                player.inventory.setItem(i, ItemStack(Material.AIR))
        player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 3f, 1f)

        if (level == SoupsimulatorLevel.BONUS) {
            val nextTask = Random.nextInt(30)

            if (nextTask < 21)
                Task = SoupsimulatorTasks.SOUP
            else if (nextTask < 26) {
                player.sendTitle("${KColors.DODGERBLUE}Refill", "§a", 1, 15, 1)
                Task = SoupsimulatorTasks.REFILL
                for (i in 9..35)
                    player.inventory.setItem(i, ItemStack(Material.MUSHROOM_STEW))

            } else {
                player.sendTitle("${KColors.MEDIUMPURPLE}Recraft", "§a", 1, 15, 1)
                KitUtils.giveRecraft(player, 64)
                Task = SoupsimulatorTasks.RECRAFT
            }
        } else
            Task = SoupsimulatorTasks.SOUP

        if (Task == SoupsimulatorTasks.SOUP) {
            var again = true

            do {
                val i = Random.nextInt(9)
                if (player.inventory.getItem(i)?.type == Material.STONE_SWORD) {
                    continue
                } else {
                    Task = SoupsimulatorTasks.SOUP
                    player.inventory.setItem(i, ItemStack(Material.MUSHROOM_STEW))
                    player.level = i + 1
                    again = false
                }
            } while (again)

        } else {
            taskStart = System.currentTimeMillis()
            player.level = 10
        }
    }

    fun sendRecraftRefillTime() {
        val jetzt = System.currentTimeMillis()
        val time = (jetzt - taskStart!!).toInt()
        val seconds = time / 1000
        val millisec = time - seconds * 1000

        val refillPrefix = " ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}Refill ${KColors.DARKGRAY}» ${KColors.GRAY}"
        val recraftPrefix =
            " ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}Recraft ${KColors.DARKGRAY}» ${KColors.GRAY}"

        if (Task == SoupsimulatorTasks.REFILL)
            if (player.localization("de"))
                player.sendMessage("${refillPrefix}Du hast ${KColors.DODGERBLUE}$seconds${KColors.DARKGRAY}:${KColors.DODGERBLUE}$millisec${(if (seconds == 1) " Sekunde" else " Sekunden")} ${KColors.GRAY}gebraucht um zu Refillen.")
            else
                player.sendMessage("${refillPrefix}It took you ${KColors.DODGERBLUE}$seconds${KColors.DARKGRAY}:${KColors.DODGERBLUE}$millisec${(if (seconds == 1) " second" else " seconds")} ${KColors.GRAY}to refill.")

        if (Task == SoupsimulatorTasks.RECRAFT)
            if (player.localization("de"))
                player.sendMessage("${recraftPrefix}Du hast ${KColors.DODGERBLUE}$seconds${KColors.DARKGRAY}:${KColors.DODGERBLUE}$millisec${(if (seconds == 1) " Sekunde" else " Sekunden")} ${KColors.GRAY}gebraucht um zu Recraften.")
            else
                player.sendMessage("${recraftPrefix}It took you ${KColors.DODGERBLUE}$seconds${KColors.DARKGRAY}:${KColors.DODGERBLUE}$millisec${(if (seconds == 1) " second" else " seconds")} ${KColors.GRAY}to recraft.")
    }

    fun end() {
        stop()
        player.reset()
    }

    fun stop() {
        state = Data.GameState.ENDED
        player.sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                         ")
        player.sendMsg("soupsimulator.finish.score", mutableMapOf("score" to "$score"))
        if (level == SoupsimulatorLevel.BONUS) {
            player.sendMsg("soupsimulator.finish.refills", mutableMapOf("refills" to "$refills"))
            player.sendMsg("soupsimulator.finish.recrafts", mutableMapOf("recrafts" to "$recrafts"))
        }
        player.sendMsg("soupsimulator.finish.wrongHotkeys", mutableMapOf("hotkeys" to "$wrongHotkeys"))
        player.sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                         ")

        val duelsPlayer = DuelsPlayer.get(player)
        if (level == SoupsimulatorLevel.HARD) {
            val stats = duelsPlayer.stats
            if (stats.soupsimulatorHighscore() < score) {
                stats.setSoupsimulatorHighscore(score)
                player.sendMsg("soupsimulator.finish.newRecord")
            }
        }
        Data.soupsimulator.remove(player)
    }
}