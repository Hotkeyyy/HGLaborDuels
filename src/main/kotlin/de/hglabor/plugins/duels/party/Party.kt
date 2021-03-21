package de.hglabor.plugins.duels.party

import de.hglabor.plugins.duels.events.events.duel.DuelDeathReason
import de.hglabor.plugins.duels.events.events.duel.PlayerDeathInDuelEvent
import de.hglabor.plugins.duels.functionality.MainInventory
import de.hglabor.plugins.duels.functionality.PartyInventory
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.localization.sendMsg
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.duels.utils.PlayerFunctions.reset
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.chat.sendMessage
import net.axay.kspigot.runnables.async
import net.axay.kspigot.runnables.taskRunLater
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class Party(val leader: Player) {
    companion object {
        fun get(player: Player): Party? {
            return if (Partys.playerParty[player] == null)
                null
            else
                Partys.playerParty[player]
        }

        fun getOrCreate(player: Player, notifyOnCreation: Boolean): Party {
            return if (get(player) == null)
                Party(player).create(notifyOnCreation)
            else
                Partys.playerParty[player]!!
        }

        fun help(player: Player) {
            player.sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                         ")
            player.sendMsg("party.help")
            player.sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                         ")
        }
    }

    var isPublic = false
    val players = arrayListOf<Player>()
    val invitedPlayers = arrayListOf<Player>()

    init { players.add(leader) }

    fun create(notify: Boolean): Party {
        if (notify)
            leader.sendMsg("party.created")

        Partys.playerParty[leader] = this
        PartyInventory.giveItems(leader)
        return this
    }

    fun invitePlayer(invited: Player) {
        invitedPlayers.add(invited)
        leader.sendMsg("party.invited", mutableMapOf("%playerName%" to invited.name))
        async {
            players.filter { it != leader }.forEach {
                it.sendMsg("party.leaderInvited", mutableMapOf("%playerName%" to invited.name))
            }
        }
        invited.sendMsg("party.youWereInvited", mutableMapOf("%leaderName%" to leader.name))

        val message = TextComponent("")
        val one = TextComponent("  [")
        one.color = KColors.DARKGRAY
        val text = TextComponent(Localization.INSTANCE.getMessage("party.clickToJoin.message", invited))
        message.color = KColors.MEDIUMPURPLE
        message.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND,
            Localization.INSTANCE.getMessage("party.clickToJoin.hover", mutableMapOf("leaderName" to leader.name), invited))
        val two = TextComponent("]")
        two.color = KColors.DARKGRAY
        message.addExtra(one)
        message.addExtra(text)
        message.addExtra(two)
        invited.sendMessage(message)

        taskRunLater(20*60, false) { invitedPlayers.remove(invited) }
    }

    fun addPlayer(player: Player) {
        sendMsg("party.playerJoined", mutableMapOf("playerName" to player.name))
        player.sendMsg("party.joined", mutableMapOf("leaderName" to leader.name))
        players.add(player)
        PartyInventory.giveItems(player)
        Partys.playerParty[player] = this
    }

    fun leave(player: Player) {
        players.remove(player)
        Partys.playerParty.remove(player)
        sendMsg("party.playerLeft", mutableMapOf("playerName" to player.name))
        player.sendMsg("party.left")
        if (!player.isInFight())
            player.reset()
    }

    fun kick(player: Player) {
        players.remove(player)
        Partys.playerParty.remove(player)
        sendMsg("party.playerWasKicked", mutableMapOf("playerName" to player.name))
        player.sendMsg("party.wereKicked")

        if (player.isInFight())
            Bukkit.getPluginManager().callEvent(PlayerDeathInDuelEvent(player, Data.duelFromPlayer(player), DuelDeathReason.QUIT))

        if (player.world.name != "world")
            player.reset()
        else
            MainInventory.giveItems(player)
    }

    fun togglePrivacy(): Boolean {
        isPublic = !isPublic
        val privacy = if (isPublic) "public" else "private"
        sendMsg("party.privacy.toggled", mutableMapOf("privacy" to privacy))
        return isPublic
    }

    fun sendInfo(player: Player) {
        player.sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                         ")
        player.sendMessage(" ${KColors.DARKGRAY}| ${KColors.GRAY}Leader: ${KColors.MAGENTA}${leader.name}")
        player.sendMessage(" ${KColors.DARKGRAY}| ${KColors.GRAY}Public: $isPublic")
        var members = ""
        players.filter { it != leader }.forEach { members += "${it.name}, " }
        player.sendMessage(" ${KColors.DARKGRAY}| ${KColors.GRAY}Members (${players.size - 1}): ${KColors.MEDIUMPURPLE}$members")
        player.sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                         ")
    }

    fun delete() {
        isPublic = false
        sendMsg("party.delted")
        players.forEach {
            if (!it.isInFight())
                MainInventory.giveItems(it)
            Partys.playerParty.remove(it)
        }
        players.clear()
    }

    fun getSplitTeams(): Pair<ArrayList<Player>, ArrayList<Player>> {
        val allPlayers: ArrayList<Player> = players
        allPlayers.shuffle()

        val teamOne = ArrayList<Player>()
        val teamTwo = ArrayList<Player>()

        teamOne.addAll(players.subList(0, players.size / 2 + players.size % 2))
        teamTwo.addAll(players.subList(players.size / 2 + players.size % 2, players.size))
        return teamOne to teamTwo
    }

    fun sendMessage(string: String) {
        players.forEach {
            it.sendMessage(string)
        }
    }

    fun sendMsg(key: String, values: MutableMap<String, String>? = null) {
        players.forEach {
            it.sendMsg(key, values)
        }
    }
}