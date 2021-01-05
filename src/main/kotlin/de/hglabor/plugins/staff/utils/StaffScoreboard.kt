package de.hglabor.plugins.staff.utils

import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.staff.utils.StaffData.isStaff
import net.axay.kspigot.chat.KColors
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team

object StaffScoreboard {
    fun setScoreboard(player: Player) {
        val sb = Bukkit.getScoreboardManager().newScoreboard
        var obj = sb.getObjective("aaa")

        if (obj == null) {
            obj = sb.registerNewObjective("aaa", "bbb", "${KColors.DARKPURPLE}${KColors.BOLD}Staff")
        }

        obj.displayName = "${KColors.DEEPSKYBLUE}${KColors.BOLD}Duels"
        obj.displaySlot = DisplaySlot.SIDEBAR

        val vanished = if (StaffData.vanishedPlayers.contains(player)) "${KColors.GREEN}Yes"
                else "${KColors.RED}No"
        val following = if (StaffData.followedPlayerFromStaff.containsKey(player)) "${KColors.DODGERBLUE}${StaffData.followedPlayerFromStaff[player]!!.name}"
                else "${KColors.GRAY}None"

        obj.getScore("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}----------§a").score = 4
        obj.getScore(updateTeam(sb, "vanished", " ${KColors.CORNSILK}Vanished ${KColors.DARKGRAY}» ",
            vanished, ChatColor.AQUA)).score = 3

        obj.getScore(updateTeam(sb, "following", " ${KColors.CORNSILK}Following ${KColors.DARKGRAY}» ",
                following, ChatColor.RED)).score = 2

        obj.getScore("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}----------§b").score = 1
        obj.getScore("${KColors.GRAY}${KColors.ITALIC}HGLabor.de").score = 0

        player.scoreboard = sb
    }

    fun setEmptyScoreBoard(player: Player) {
        val sb = Bukkit.getScoreboardManager().newScoreboard

        player.scoreboard = sb
    }

    fun updateScoreboard(player: Player) {
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

        obj.getScore("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}----------§a").score = 4
        obj.getScore(
            updateTeam(
                sb, "online", " ${KColors.CORNSILK}Online ${KColors.DARKGRAY}» ",
                "${KColors.DODGERBLUE}$online",
                ChatColor.AQUA
            )
        ).score = 3

        obj.getScore(
            updateTeam(
                sb, "infight", " ${KColors.CORNSILK}In Fight ${KColors.DARKGRAY}» ",
                "${KColors.DODGERBLUE}$inFight",
                ChatColor.RED
            )
        ).score = 2

    }


    fun getTeam(sb: Scoreboard, teamName: String, prefix: String, suffix: String): Team {
        var team = sb.getTeam(teamName)
        if (team == null) {
            team = sb.registerNewTeam(teamName)
        }
        team.prefix = prefix
        team.suffix = suffix
        team.setCanSeeFriendlyInvisibles(false)
        team.setAllowFriendlyFire(true)
        return team
    }

    fun updateTeam(sb: Scoreboard, teamName: String, prefix: String, suffix: String, entry: ChatColor): String {
        var team = sb.getTeam(teamName)
        if (team == null) {
            team = sb.registerNewTeam(teamName)
        }
        team.prefix = prefix
        team.suffix = suffix
        team.addEntry(entry.toString())
        return entry.toString()
    }
}