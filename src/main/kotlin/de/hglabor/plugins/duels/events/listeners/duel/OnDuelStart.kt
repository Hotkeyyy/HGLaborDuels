package de.hglabor.plugins.duels.events.listeners.duel

import de.hglabor.plugins.duels.events.events.duel.DuelStartEvent
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.kit.Random
import de.hglabor.plugins.duels.utils.Data
import net.axay.kspigot.event.listen

object OnDuelStart {

    init {
        listen<DuelStartEvent> {
            val duel = it.duel
            val kit = duel.kit
            Data.duelFromID[duel.ID] = duel
            duel.alivePlayers.forEach { player ->
                Data.challengeKit.remove(player)
                Data.challenged.remove(player)
                Data.duelIDFromPlayer[player] = duel.ID
                Data.inFight.add(player)
                Kits.inGame[kit]?.addAll(duel.alivePlayers)

                Kits.playerQueue[player]?.forEach { kit ->
                    Kits.queue[kit]?.minusAssign(player)
                    Kits.queue[Random.INSTANCE]?.minusAssign(player)
                }
                Kits.playerQueue[player]?.clear()
            }
        }
    }
}