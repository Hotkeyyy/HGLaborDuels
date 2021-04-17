package de.hglabor.plugins.duels.duel

import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.team.Team
import de.hglabor.plugins.duels.team.TeamColor
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.chat.sendMessage
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player

class RankedDuel(teamOneLeader: Player, teamTwoLeader: Player, kit: AbstractKit) : AbstractDuel(Team(teamOneLeader, TeamColor.PINK), Team(teamTwoLeader, TeamColor.BLUE), kit) {

    override fun results() {
        sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                        ")
        if (teamOne.winner) {
            sendMsg("duel.result.winner.teamOne",
                mutableMapOf("teamColor" to "${teamOne.teamColor.mainColor}", "teamPlayers" to teamOne.playerListString()))
            sendMsg("duel.result.loser.teamTwo",
                mutableMapOf("teamColor" to "${teamTwo.teamColor.mainColor}", "teamPlayers" to teamTwo.playerListString()))
        } else {
            sendMsg("duel.result.winner.teamTwo",
                mutableMapOf("teamColor" to "${teamTwo.teamColor.mainColor}", "teamPlayers" to teamTwo.playerListString()))
            sendMsg("duel.result.loser.teamOne",
                mutableMapOf("teamColor" to "${teamOne.teamColor.mainColor}", "teamPlayers" to teamOne.playerListString()))
        }

        val message = TextComponent("Click to open the duel overview")
        message.color = KColors.GRAY
        message.isItalic = true
        message.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dueloverview $gameID")

        allPlayers().forEach { it.sendMessage(message) }
        sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                        ")
    }

    fun calculateELo() {

    }
}
