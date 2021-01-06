package de.hglabor.plugins.duels.party

import org.bukkit.entity.Player

object Partys {
    val partyOfLeader = hashMapOf<Player, Party>()
    val partyOfMember = hashMapOf<Player, Party>()

    fun Player.hasParty(): Boolean {
        return partyOfLeader.containsKey(this)
    }

    fun Player.isInParty(): Boolean {
        return partyOfMember.containsKey(this)
    }

    fun Player.isInOrHasParty(): Boolean {
        return partyOfMember.containsKey(this) || partyOfLeader.containsKey(this)
    }

    fun getPartyFromPlayer(player: Player): Party {
        if (player.hasParty()) {
            partyOfLeader.remove(player)
            return partyOfLeader[player]!!
        } else {
            partyOfMember.remove(player)
            return partyOfMember[player]!!
        }
    }
}