package de.hglabor.plugins.duels.scoreboard

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.duel.Duel
import net.axay.kspigot.chat.KColors
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team

object FightSB {
    fun setCountdownScoreboard(duel: Duel, player: Player) {
        val sb = Bukkit.getScoreboardManager().newScoreboard

        if (duel.alivePlayers.contains(player)) {
            var obj = sb.getObjective("aaa")
            if (obj == null) {
                obj = sb.registerNewObjective("aaa", "bbb", "${KColors.DEEPSKYBLUE}${KColors.BOLD}Duel")
            }

            obj.displayName = "${KColors.DEEPSKYBLUE}${KColors.BOLD}Duel"
            obj.displaySlot = DisplaySlot.SIDEBAR

            val ownTeamSize = duel.getTeam(player).size
            val enemyTeamSize = duel.getOtherTeam(player).size
            val kit = duel.kit.name
            val knockback = duel.knockbackType?.version

            obj.getScore(updateTeam(sb, "lineone", "${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                  §a",
                "", ChatColor.GREEN)).score = 7

            obj.getScore(updateTeam(sb, "ownTeam", " ${KColors.CORNSILK}Your Team ${KColors.DARKGRAY}» ",
                "${KColors.DODGERBLUE}$ownTeamSize", ChatColor.AQUA)).score = 6

            obj.getScore(updateTeam(sb, "enemyTeam", " ${KColors.CORNSILK}Enemy Team ${KColors.DARKGRAY}» ",
                "${KColors.MEDIUMPURPLE}$enemyTeamSize", ChatColor.RED)).score = 5

            obj.getScore(updateTeam(sb, "placeholder", " ",
                "", ChatColor.LIGHT_PURPLE)).score = 4

            obj.getScore(updateTeam(sb, "kit", " ${KColors.CORNSILK}Kit ${KColors.DARKGRAY}» ",
                "${KColors.DODGERBLUE}$kit", ChatColor.YELLOW)).score = 3

            obj.getScore(updateTeam(sb, "knockback", " ${KColors.CORNSILK}Knockback ${KColors.DARKGRAY}» ",
                "${KColors.DODGERBLUE}$knockback", ChatColor.WHITE)).score = 2

            obj.getScore(updateTeam(sb, "linetwo", "${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                  §a",
                "", ChatColor.GRAY)).score = 1

            obj.getScore(updateTeam(sb, "hglabor", "${KColors.GRAY}${KColors.ITALIC}HGLabor.de",
                "", ChatColor.DARK_RED)).score = 0

        }

        val teamOne = getTeam(sb, "0001teamOne", ChatColor.AQUA)
        val teamTwo = getTeam(sb, "0002teamTwo", ChatColor.LIGHT_PURPLE)
        for (on in Bukkit.getOnlinePlayers()) {
            if (duel.teamOne.contains(on))
                teamOne.addEntry(on.name)

            else if (duel.teamTwo.contains(on))
                teamTwo.addEntry(on.name)

            else
                player.hidePlayer(Manager.INSTANCE, on)
        }
        player.scoreboard = sb
    }

    fun setGameScoreboard(duel: Duel, player: Player) {
        val sb = Bukkit.getScoreboardManager().newScoreboard
        val teamOne = getTeam(sb, "0001teamOne", ChatColor.AQUA)
        val teamTwo = getTeam(sb, "0002teamTwo", ChatColor.LIGHT_PURPLE)
        for (on in Bukkit.getOnlinePlayers()) {
        if (duel.teamOne.contains(on))
            teamOne.addEntry(on.name)
        else if (duel.teamTwo.contains(on))
            teamTwo.addEntry(on.name)
        else
            player.hidePlayer(Manager.INSTANCE, on)
        }
        player.scoreboard = sb
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

    private fun getTeam(sb: Scoreboard, Team: String?, color: ChatColor): Team {
        var team = sb.getTeam(Team!!)
        if (team == null) {
            team = sb.registerNewTeam(Team)
        }
        team.color = color
        team.setCanSeeFriendlyInvisibles(false)
        team.setAllowFriendlyFire(false)
        team.setOption(org.bukkit.scoreboard.Team.Option.COLLISION_RULE, org.bukkit.scoreboard.Team.OptionStatus.FOR_OWN_TEAM)
        return team
    }
}