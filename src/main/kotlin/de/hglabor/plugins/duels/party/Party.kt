package de.hglabor.plugins.duels.party

import de.hglabor.plugins.duels.functionality.MainInventory
import de.hglabor.plugins.duels.functionality.PartyInventory
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.reset
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.runnables.async
import net.axay.kspigot.runnables.taskRunLater
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
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
            if (player.localization("de")) {
                player.sendMessage(" §8| ${KColors.MEDIUMPURPLE}/party create §8» §7Erstelle eine Party")
                player.sendMessage(" §8| ${KColors.MEDIUMPURPLE}/party info §8<${KColors.MEDIUMPURPLE}Spieler§8> §8» §7Zeige Infos über eine Party")
                player.sendMessage(" §8| ${KColors.MEDIUMPURPLE}/party invite §8<${KColors.MEDIUMPURPLE}Spieler§8> §8» §7Lade einen Spieler ein")
                player.sendMessage(" §8| ${KColors.MEDIUMPURPLE}/party join §8<${KColors.MEDIUMPURPLE}Spieler§8> §8» §7Trete einer Party bei")
                player.sendMessage(" §8| ${KColors.MEDIUMPURPLE}/party leave §8» §7Verlasse/lösche deine Party")
                player.sendMessage(" §8| ${KColors.MEDIUMPURPLE}/party public §8» §7Party auf öffentlich/privat stellen")
            } else {
                player.sendMessage(" §8| ${KColors.MEDIUMPURPLE}/party create §8» §7Create a party")
                player.sendMessage(" §8| ${KColors.MEDIUMPURPLE}/party info §8<${KColors.MEDIUMPURPLE}Player§8> §8» §7Show infos about a party")
                player.sendMessage(" §8| ${KColors.MEDIUMPURPLE}/party invite §8<${KColors.MEDIUMPURPLE}Player§8> §8» §7Invite a player")
                player.sendMessage(" §8| ${KColors.MEDIUMPURPLE}/party join §8<${KColors.MEDIUMPURPLE}Player§8> §8» §7Join a party")
                player.sendMessage(" §8| ${KColors.MEDIUMPURPLE}/party leave §8» §7Leave/delete a party")
                player.sendMessage(" §8| ${KColors.MEDIUMPURPLE}/party public §8» §7Set party to public/privat")
            }
            player.sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                         ")
        }
    }

    var isPublic = false
    val players = arrayListOf<Player>()
    val invitedPlayers = arrayListOf<Player>()

    init { players.add(leader) }

    fun create(notify: Boolean): Party {
        if (notify)
            leader.sendLocalizedMessage(Localization.PARTY_CREATED_DE, Localization.PARTY_CREATED_EN)

        Partys.playerParty[leader] = this
        PartyInventory.giveItems(leader)
        return this
    }

    fun invitePlayer(invited: Player) {
        invitedPlayers.add(invited)
        leader.sendLocalizedMessage(
            Localization.PARTY_YOU_INVITED_DE,
            Localization.PARTY_YOU_INVITED_EN,
            "%playerName%", invited.name)
        async {
            players.filter { it != leader }.forEach {
                if (it.localization("de"))
                    it.sendMessage("${Localization.PARTY_PREFIX}${KColors.MAGENTA}${leader.name} §7hat ${KColors.MEDIUMPURPLE}${invited.name} §7eingeladen.")
                else
                    it.sendMessage("${Localization.PARTY_PREFIX}${KColors.MAGENTA}${leader.name} §7invited ${KColors.MEDIUMPURPLE}${invited.name}§7.")
            }
        }
        invited.sendLocalizedMessage(
            Localization.PARTY_YOU_WERE_INVITED_DE,
            Localization.PARTY_YOU_WERE_INVITED_EN,
            "%playerName%",
            leader.name
        )

        val message = TextComponent("")
        val one = TextComponent("  [")
        one.color = KColors.DARKGRAY
        val text = TextComponent("Click to join")
        message.color = KColors.MEDIUMPURPLE
        message.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party join ${leader.name}")
        val two = TextComponent("]")
        two.color = KColors.DARKGRAY
        message.addExtra(one)
        message.addExtra(text)
        message.addExtra(two)
        invited.sendMessage(message)

        taskRunLater(20*60, false) { invitedPlayers.remove(invited) }
    }

    fun addPlayer(player: Player) {
        sendLocalizedMessage(
            Localization.PARTY_PLAYER_JOINED_DE,
            Localization.PARTY_PLAYER_JOINED_EN,
            "%playerName%",
            player.name
        )
        player.sendLocalizedMessage(
            Localization.PARTY_YOU_JOINED_DE,
            Localization.PARTY_YOU_JOINED_EN,
            "%playerName%",
            leader.name
        )
        players.add(player)
        PartyInventory.giveItems(player)
        Partys.playerParty[player] = this
    }

    fun leave(player: Player) {
        players.remove(player)
        Partys.playerParty.remove(player)
        sendLocalizedMessage(
            Localization.PARTY_PLAYER_LEFT_DE,
            Localization.PARTY_PLAYER_LEFT_EN,
            "%playerName%", player.name)
        player.sendLocalizedMessage(Localization.PARTY_YOU_LEFT_DE, Localization.PARTY_YOU_LEFT_EN)
        if (!player.isInFight())
            player.reset()
    }

    fun kick(player: Player) {
        players.remove(player)
        Partys.playerParty.remove(player)
        sendLocalizedMessage(
            Localization.PARTY_PLAYER_WAS_KICKED_DE,
            Localization.PARTY_PLAYER_WAS_KICKED_EN,
            "%playerName",
            player.name)
        player.sendLocalizedMessage(Localization.PARTY_YOU_WERE_KICKED_DE, Localization.PARTY_YOU_WERE_KICKED_EN)

        if (player.isInFight())
            Data.duelFromPlayer(player).playerLeft(player)

        if (player.world.name != "world")
            player.reset()
        else
            MainInventory.giveItems(player)
    }

    fun togglePrivacy(): Boolean {
        if (isPublic)
            sendLocalizedMessage(Localization.PARTY_NOW_PRIVAT_DE, Localization.PARTY_NOW_PRIVAT_EN)
        else
            sendLocalizedMessage(Localization.PARTY_NOW_PUBLIC_DE, Localization.PARTY_NOW_PUBLIC_EN)
        isPublic = !isPublic
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
        sendLocalizedMessage(Localization.PARTY_DELETED_DE, Localization.PARTY_DELETED_EN)
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

    fun sendLocalizedMessage(germanMessage: String, englishMessage: String) {
        async { players.forEach { it.sendLocalizedMessage(germanMessage, englishMessage) } }
    }

    fun sendLocalizedMessage(germanMessage: String, englishMessage: String, toReplace: String, replacement: String) {
        async { players.forEach { it.sendLocalizedMessage(germanMessage, englishMessage, toReplace, replacement) } }
    }
}