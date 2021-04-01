package de.hglabor.plugins.duels.events.listeners.duel

import de.hglabor.plugins.duels.player.DuelsPlayer
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.staff.utils.StaffData.isInStaffMode
import net.axay.kspigot.event.listen
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

object OnBuild {
    init {
        listen<BlockBreakEvent> {
            val player = it.player
            val duelsPlayer = DuelsPlayer.get(player)

            if (it.player.isInStaffMode) {
                it.isCancelled = true
                return@listen
            }

            if (!duelsPlayer.isInFight()) return@listen
            val duel = duelsPlayer.currentDuel() ?: return@listen
            val isBreakable = duel.blocksPlacedDuringGame.contains(it.block) || Data.breakableBlocks.contains(it.block)
            if (isBreakable) {
                it.isCancelled = false
                duel.blocksPlacedDuringGame.remove(it.block)
                Data.breakableBlocks.remove(it.block)
            } else
                it.isCancelled = true
        }

        listen<BlockPlaceEvent> {
            val player = it.player
            val duelsPlayer = DuelsPlayer.get(player)

            if (it.player.isInStaffMode) {
                it.isCancelled = true
                return@listen
            }
            if (!duelsPlayer.isInFight()) return@listen
            val duel = duelsPlayer.currentDuel() ?: return@listen
            duel.blocksPlacedDuringGame.add(it.block)
        }
    }
}