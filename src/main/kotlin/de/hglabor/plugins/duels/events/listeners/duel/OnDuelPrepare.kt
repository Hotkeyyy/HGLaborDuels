package de.hglabor.plugins.duels.events.listeners.duel

import de.hglabor.plugins.duels.events.events.duel.DuelPrepareEvent
import de.hglabor.plugins.duels.player.DuelsPlayer
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.staff.utils.StaffData
import net.axay.kspigot.event.listen

object OnDuelPrepare {

    init {
        listen<DuelPrepareEvent> {
            val duel = it.duel
            duel.allPlayers().forEach { player ->
                val duelsPlayer = DuelsPlayer.get(player)
                Data.challengeKit.remove(player)
                Data.challenged.remove(player)
                Data.duelOfPlayer[player] = duel
                Data.inFight.add(player)

                StaffData.followingStaffFromPlayer[player]?.forEach { staff ->
                    duel.addSpectator(staff, false)
                }

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