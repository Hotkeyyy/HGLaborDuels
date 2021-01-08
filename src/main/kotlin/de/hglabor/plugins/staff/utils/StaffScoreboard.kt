package de.hglabor.plugins.staff.utils

import de.hglabor.plugins.duels.scoreboard.LobbyScoreboard
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.Ranks
import de.hglabor.plugins.staff.utils.StaffData.isStaff
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.onlinePlayers
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team

object StaffScoreboard {
    val hasScoreboard = arrayListOf<Player>()
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

        obj.getScore("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                  §a").score = 4
        obj.getScore(updateTeam(sb, "vanished", " ${KColors.CORNSILK}Vanished ${KColors.DARKGRAY}» ",
            vanished, ChatColor.AQUA)).score = 3

        obj.getScore(updateTeam(sb, "following", " ${KColors.CORNSILK}Following ${KColors.DARKGRAY}» ",
                following, ChatColor.RED)).score = 2

        obj.getScore("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                  §b").score = 1
        obj.getScore("${KColors.GRAY}${KColors.ITALIC}HGLabor.de").score = 0

        val owner: Team = LobbyScoreboard.getTeam(sb, "0001owner", Ranks.Rank.OWNER)
        val admin: Team = LobbyScoreboard.getTeam(sb, "0002admin", Ranks.Rank.ADMIN)
        val mod: Team = LobbyScoreboard.getTeam(sb, "0003mod", Ranks.Rank.MOD)
        val helper: Team = LobbyScoreboard.getTeam(sb, "0004helper", Ranks.Rank.HELPER)
        val normieplus: Team = LobbyScoreboard.getTeam(sb, "0005normieplus", Ranks.Rank.NORMIEPLUS)
        val normie: Team = LobbyScoreboard.getTeam(sb, "0006normie", Ranks.Rank.NORMIE)

        onlinePlayers.forEach { on ->
            when (Ranks.getRank(on)) {
                Ranks.Rank.OWNER -> owner.addEntry(on.name)
                Ranks.Rank.ADMIN -> admin.addEntry(on.name)
                Ranks.Rank.MOD -> mod.addEntry(on.name)
                Ranks.Rank.HELPER -> helper.addEntry(on.name)
                Ranks.Rank.NORMIEPLUS -> normieplus.addEntry(on.name)
                else -> normie.addEntry(on.name)
            }
        }

        player.scoreboard = sb
    }

    fun updateScoreboard(player: Player) {
        val sb = player.scoreboard
        var obj = sb.getObjective("aaa")

        if (obj == null) {
            obj = sb.registerNewObjective("aaa", "bbb", "${KColors.DEEPSKYBLUE}${KColors.BOLD}Duels")
        }

        val owner: Team = LobbyScoreboard.getTeam(sb, "0001owner", Ranks.Rank.OWNER)
        val admin: Team = LobbyScoreboard.getTeam(sb, "0002admin", Ranks.Rank.ADMIN)
        val mod: Team = LobbyScoreboard.getTeam(sb, "0003mod", Ranks.Rank.MOD)
        val helper: Team = LobbyScoreboard.getTeam(sb, "0004helper", Ranks.Rank.HELPER)
        val normieplus: Team = LobbyScoreboard.getTeam(sb, "0005normieplus", Ranks.Rank.NORMIEPLUS)
        val normie: Team = LobbyScoreboard.getTeam(sb, "0006normie", Ranks.Rank.NORMIE)

        onlinePlayers.forEach { on ->
            when (Ranks.getRank(on)) {
                Ranks.Rank.OWNER -> owner.addEntry(on.name)
                Ranks.Rank.ADMIN -> admin.addEntry(on.name)
                Ranks.Rank.MOD -> mod.addEntry(on.name)
                Ranks.Rank.HELPER -> helper.addEntry(on.name)
                Ranks.Rank.NORMIEPLUS -> normieplus.addEntry(on.name)
                else -> normie.addEntry(on.name)
            }
        }

        val inFight = Data.inFight.size
        val online: Int = if (!player.isStaff)
            Bukkit.getOnlinePlayers().size - StaffData.vanishedPlayers.size
        else
            Bukkit.getOnlinePlayers().size

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