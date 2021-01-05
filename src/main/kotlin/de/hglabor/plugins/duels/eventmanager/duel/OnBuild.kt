package de.hglabor.plugins.duels.eventmanager.duel

import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.staff.utils.StaffData.isInStaffMode
import net.axay.kspigot.event.listen
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

object OnBuild {
    fun enable() {
        listen<BlockBreakEvent> {
            val player = it.player
            if (it.player.isInStaffMode)
                it.isCancelled = true
            if (!player.isInFight()) return@listen
            val duel = Data.duelFromPlayer(player)
            it.isCancelled = !duel.blocksPlacedDuringGame.contains(it.block)
        }

        listen<BlockPlaceEvent> {
            val player = it.player
            if (it.player.isInStaffMode)
                it.isCancelled = true
            if (!player.isInFight()) return@listen
            val duel = Data.duelFromPlayer(player)
            duel.blocksPlacedDuringGame.add(it.block)
        }
    }
}