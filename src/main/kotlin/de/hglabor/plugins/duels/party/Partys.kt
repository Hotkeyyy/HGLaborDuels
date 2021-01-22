package de.hglabor.plugins.duels.party

import org.bukkit.entity.Player

object Partys {
    val playerParty = hashMapOf<Player, Party>()

    fun Player.hasParty(): Boolean {
        if (playerParty.containsKey(this))
            if (playerParty[this]?.leader == this)
                return true
        return false
    }

    fun Player.isInParty(): Boolean {
        return playerParty.containsKey(this)
    }

    fun Player.isPartyMember(): Boolean {
        if (playerParty.containsKey(this))
            return playerParty[this]?.leader != this
        return false
    }
}