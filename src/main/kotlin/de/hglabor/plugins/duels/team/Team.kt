package de.hglabor.plugins.duels.team

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class Team(val members: MutableList<LivingEntity>, val teamColor: TeamColor) {
    companion object {
        val teamOfPlayer = mutableMapOf<LivingEntity, Team>()

        fun get(player: Player) = teamOfPlayer[player]
    }
    val livingMembers: MutableList<LivingEntity> = ArrayList(members)
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