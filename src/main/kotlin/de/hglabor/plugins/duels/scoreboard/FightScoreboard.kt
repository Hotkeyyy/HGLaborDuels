package de.hglabor.plugins.duels.scoreboard

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.duels.utils.Ranks
import de.hglabor.plugins.staff.utils.StaffData
import de.hglabor.plugins.staff.utils.StaffData.isInStaffMode
import de.hglabor.plugins.staff.utils.StaffData.isStaff
import de.hglabor.plugins.staff.utils.StaffScoreboard
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.onlinePlayers
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team

object FightScoreboard {
    val hasScoreboard = arrayListOf<Player>()

    fun setScoreboard(player: Player) {
        val sb = Bukkit.getScoreboardManager().newScoreboard

        val owner: Team = getTeam(sb, "0001owner", Ranks.Rank.OWNER)
        val admin: Team = getTeam(sb, "0002admin", Ranks.Rank.ADMIN)
        val mod: Team = getTeam(sb, "0003mod", Ranks.Rank.MOD)
        val helper: Team = getTeam(sb, "0004helper", Ranks.Rank.HELPER)
        val normieplus: Team = getTeam(sb, "0005normieplus", Ranks.Rank.NORMIEPLUS)
        val normie: Team = getTeam(sb, "0006normie", Ranks.Rank.NORMIE)

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

        val owner: Team = getTeam(sb, "0001owner", Ranks.Rank.OWNER)
        val admin: Team = getTeam(sb, "0002admin", Ranks.Rank.ADMIN)
        val mod: Team = getTeam(sb, "0003mod", Ranks.Rank.MOD)
        val helper: Team = getTeam(sb, "0004helper", Ranks.Rank.HELPER)
        val normieplus: Team = getTeam(sb, "0005normieplus", Ranks.Rank.NORMIEPLUS)
        val normie: Team = getTeam(sb, "0006normie", Ranks.Rank.NORMIE)

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
    }


    fun getTeam(sb: Scoreboard, teamName: String, rank: Ranks.Rank): Team {
        var team = sb.getTeam(teamName)
        if (team == null) {
            team = sb.registerNewTeam(teamName)
        }
        if (rank.prefix != null)
            team.prefix = rank.prefix
        team.suffix = ""
        team.color = rank.color
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
}