package de.hglabor.plugins.duels.scoreboard

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.duel.Duel
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team

object FightSB {
    fun setScoreboard(duel: Duel, player: Player) {
        val sb = Bukkit.getScoreboardManager()!!.newScoreboard

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

    fun getTeam(sb: Scoreboard, Team: String?, color: ChatColor): Team {
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