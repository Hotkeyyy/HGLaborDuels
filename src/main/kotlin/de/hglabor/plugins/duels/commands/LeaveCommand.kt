package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.events.events.duel.DuelDeathReason
import de.hglabor.plugins.duels.events.events.duel.PlayerDeathInDuelEvent
import de.hglabor.plugins.duels.localization.sendMsg
import de.hglabor.plugins.duels.player.DuelsPlayer
import de.hglabor.plugins.duels.soupsimulator.Soupsimulator
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object LeaveCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val duelsPlayer = DuelsPlayer.get(sender)

            if (!duelsPlayer.isBusy()) {
                sender.sendMsg("command.cantExecuteNow")
                return false
            }

            if (duelsPlayer.isInSoupsimulator()) {
                Soupsimulator.get(sender)?.end()

            } else if (duelsPlayer.isInFight()) {
                val duel = duelsPlayer.currentDuel() ?: return false
                Bukkit.getPluginManager().callEvent(PlayerDeathInDuelEvent(sender, duel, DuelDeathReason.QUIT))

            } else if (duelsPlayer.isSpectator()) {
                val duel = duelsPlayer.spectatingDuel()?: return false
                duel.removeSpectator(sender, true)
            }
        }
        return false
    }
}