package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.events.events.duel.DuelDeathReason
import de.hglabor.plugins.duels.events.events.duel.PlayerDeathInDuelEvent
import de.hglabor.plugins.duels.localization.sendMsg
import de.hglabor.plugins.duels.soupsimulator.Soupsim.isInSoupsimulator
import de.hglabor.plugins.duels.soupsimulator.Soupsimulator
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object LeaveCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if (sender.isInSoupsimulator()) {
                Soupsimulator.get(sender)?.end()
                return true
            }

            if (sender.isInFight()) {
                val duel = Data.duelFromPlayer(sender)
                Bukkit.getPluginManager().callEvent(PlayerDeathInDuelEvent(sender, duel, DuelDeathReason.QUIT))
                return true
            }
            sender.sendMsg("command.cantExecuteNow")
        }
        return false
    }
}