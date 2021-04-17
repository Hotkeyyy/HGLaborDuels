package de.hglabor.plugins.duels.team

import com.google.gson.Gson
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class Team(val members: MutableList<Player>, var teamColor: TeamColor) {

    constructor(member: Player, teamColor: TeamColor): this(mutableListOf(member), teamColor)

    companion object {
        val teamOfPlayer = mutableMapOf<Player, Team>()

        fun get(livingEntity: Player) = teamOfPlayer[livingEntity]
    }
    val livingMembers: MutableList<Player> = ArrayList(members)
    var winner = false
    var loser = false

    fun isDead() = livingMembers.isEmpty()

    fun playerListString(): String {
        var string = ""
        for (member in members) {
            string += "${teamColor.secondaryColor}${member.name}"
            if (members.last() != member)
                string += "§8, "
        }
        return string
    }

    init {
        members.forEach { teamOfPlayer[it] = this }
    }
}