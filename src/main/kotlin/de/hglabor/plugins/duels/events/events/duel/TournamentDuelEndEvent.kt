package de.hglabor.plugins.duels.events.events.duel

import de.hglabor.plugins.duels.duel.TournamentDuel
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class TournamentDuelEndEvent(val duel: TournamentDuel) : Event(false) {
    companion object {
        @JvmStatic
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLERS
    }

    override fun getHandlers(): HandlerList {
        return HANDLERS
    }
}