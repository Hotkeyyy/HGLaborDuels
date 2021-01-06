package de.hglabor.plugins.duels.party

import org.bukkit.entity.Player

class Party(val leader: Player) {
    var isPublic = false
    val players = arrayListOf(leader)
    val invitedPlayers = arrayListOf<Player>()

    init {
        Partys.partyOfLeader[leader] = this
        leader.sendMessage("party erstellt")
    }

    fun invitePlayer(invited: Player) {
        invitedPlayers.add(invited)
        leader.sendMessage("du hast ${invited.name} eingeladen")
        players.filter { it != leader }.forEach {
            it.sendMessage("${leader.name} hat ${invited.name} eingeladen")
        }
    }

    fun uninvitePlayer(invited: Player) {
        invitedPlayers.remove(invited)
        leader.sendMessage("du hast ${invited.name} ausgeladen")
        players.filter { it != leader }.forEach {
            it.sendMessage("${leader.name} hat ${invited.name} ausgeladen")
        }
    }

    fun addPlayer(player: Player) {
        players.forEach {
            it.sendMessage("${player.name} ist der party beigetreten")
        }
        players.add(player)
        player.sendMessage("du bist der party von ${leader.name} beigetreten")
    }

    fun removePlayer(player: Player) {
        players.remove(player)
        players.forEach {
            it.sendMessage("${player.name} hat die party verlassen")
        }
        player.sendMessage("du hast die party von ${leader.name} verlassen")
    }

    fun togglePrivacy(): Boolean {
        isPublic = !isPublic
        return isPublic
    }

    fun listPlayers(player: Player) {
        player.sendMessage("-------------------")
        player.sendMessage("Leader: ${leader.name}")
        var members = ""
        players.filter { it != leader }.forEach { members += "${it.name}, " }
        player.sendMessage("Members (${players.size-1}: $members")
    }

    fun delete() {
        isPublic = false
        leader.sendMessage("du hast die party gelöscht")
        players.filter { it != leader }.forEach {
            it.sendMessage("${leader.name} hat die party gelöscht")
        }
        invitedPlayers.clear()
        players.clear()
    }
}