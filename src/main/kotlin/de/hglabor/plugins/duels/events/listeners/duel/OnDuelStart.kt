package de.hglabor.plugins.duels.events.listeners.duel

import de.hglabor.plugins.duels.events.events.duel.DuelStartEvent
import de.hglabor.plugins.duels.player.DuelsPlayer
import de.hglabor.plugins.duels.utils.Data
import net.axay.kspigot.event.listen

object OnDuelStart {

    init {
        listen<DuelStartEvent> {
            val duel = it.duel
            Data.duelFromID[duel.ID] = duel
            duel.alivePlayers.forEach { player ->
                val duelsPlayer = DuelsPlayer.get(player)
                Data.challengeKit.remove(player)
                Data.challenged.remove(player)
                Data.duelOfPlayer[player] = duel
                Data.inFight.add(player)

                duelsPlayer.unrankedQueues.forEach { kit ->
                    kit.unrankedQueue -= player
                    duelsPlayer.unrankedQueues -= kit
                }

                duelsPlayer.rankedQueues.forEach { kit ->
                    kit.rankedQueue -= player
                    duelsPlayer.rankedQueues -= kit
                }
            }
        }
    }
}