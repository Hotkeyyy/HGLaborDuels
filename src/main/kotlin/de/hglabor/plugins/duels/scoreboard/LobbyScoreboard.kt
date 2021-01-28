package de.hglabor.plugins.duels.scoreboard

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.duel.GameState
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.staff.utils.StaffData
import de.hglabor.plugins.staff.utils.StaffData.isInStaffMode
import de.hglabor.plugins.staff.utils.StaffData.isStaff
import de.hglabor.plugins.staff.utils.StaffScoreboard
import net.axay.kspigot.chat.KColors
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team

object LobbyScoreboard {

    fun setScoreboard(player: Player) {
        val sb = Bukkit.getScoreboardManager()!!.newScoreboard
        var obj = sb.getObjective("aaa")

        if (obj == null) {
            obj = sb.registerNewObjective("aaa", "bbb", "${KColors.DEEPSKYBLUE}${KColors.BOLD}Duels")
        }

        obj.displayName = "${KColors.DEEPSKYBLUE}${KColors.BOLD}Duels"
        obj.displaySlot = DisplaySlot.SIDEBAR

        val inFight = Data.inFight.size
        val online = Bukkit.getOnlinePlayers().size

        obj.getScore(updateTeam(sb, "lineone", "${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                  §a",
                "", ChatColor.DARK_GRAY)).score = 4

        obj.getScore(updateTeam(sb, "online", " ${KColors.CORNSILK}Online ${KColors.DARKGRAY}» ",
            "${KColors.DODGERBLUE}$online", ChatColor.AQUA)).score = 3

        obj.getScore(updateTeam(sb, "infight", " ${KColors.CORNSILK}In Fight ${KColors.DARKGRAY}» ",
            "${KColors.DODGERBLUE}$inFight", ChatColor.RED)).score = 2

        obj.getScore(updateTeam(sb, "linetwo", "${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                  §a",
            "", ChatColor.GRAY)).score = 1

        obj.getScore(updateTeam(sb, "hglabor", "${KColors.GRAY}${KColors.ITALIC}HGLabor.de",
            "", ChatColor.WHITE)).score = 0

        player.scoreboard = sb
    }

    private fun updateScoreboard(player: Player) {
        val sb = player.scoreboard
        var obj = sb.getObjective("aaa")

        if (obj == null) {
            obj = sb.registerNewObjective("aaa", "bbb", "${KColors.DEEPSKYBLUE}${KColors.BOLD}Duels")
        }

        val inFight = Data.inFight.size
        val online: Int = if (!player.isStaff)
            Bukkit.getOnlinePlayers().size - StaffData.vanishedPlayers.size
        else
            Bukkit.getOnlinePlayers().size

        obj.getScore(updateTeam(sb, "lineone", "${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                  §a",
                "", ChatColor.DARK_GRAY)).score = 4

        obj.getScore(updateTeam(sb, "online", " ${KColors.CORNSILK}Online ${KColors.DARKGRAY}» ",
            "${KColors.DODGERBLUE}$online", ChatColor.AQUA)).score = 3

        obj.getScore(updateTeam(sb, "infight", " ${KColors.CORNSILK}In Fight ${KColors.DARKGRAY}» ",
            "${KColors.DODGERBLUE}$inFight", ChatColor.RED)).score = 2

        obj.getScore(updateTeam(sb, "linetwo", "${KColors.DARKGRAY}${KColors.STRIKETHROUGH} " +
                "                 §a", "", ChatColor.GRAY)).score = 1

        obj.getScore(updateTeam(sb, "hglabor", "${KColors.GRAY}${KColors.ITALIC}HGLabor.de",
            "", ChatColor.WHITE)).score = 0
    }


    fun getTeam(sb: Scoreboard, teamName: String): Team {
        var team = sb.getTeam(teamName)
        if (team == null) {
            team = sb.registerNewTeam(teamName)
        }
        team.setCanSeeFriendlyInvisibles(false)
        team.setAllowFriendlyFire(true)
        return team
    }

    private fun updateTeam(sb: Scoreboard, teamName: String, prefix: String, suffix: String, entry: ChatColor): String {
        var team = sb.getTeam(teamName)
        if (team == null) {
            team = sb.registerNewTeam(teamName)
        }
        team.prefix = prefix
        team.suffix = suffix
        team.addEntry(entry.toString())
        return entry.toString()
    }

    fun startRunnable() {
        Bukkit.getServer().scheduler.scheduleSyncRepeatingTask(Manager.INSTANCE, {
            for (all in Bukkit.getOnlinePlayers()) {
                if (all.isInStaffMode)
                    if (StaffScoreboard.hasScoreboard.contains(all))
                        StaffScoreboard.updateScoreboard(all)
                    else
                        StaffScoreboard.setScoreboard(all)
                else if (all.isInFight())
                    if (Data.duelFromPlayer(all).state == GameState.COUNTDOWN)
                        FightSB.setCountdownScoreboard(Data.duelFromPlayer(all), all)
                    else
                        FightSB.setGameScoreboard(Data.duelFromPlayer(all), all)
                else
                    updateScoreboard(all)
            }
        }, 10, 20)
    }
}