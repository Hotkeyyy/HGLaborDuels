package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.duel.overview.DuelOverviewGUI
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.soupsimulator.isInSoupsimulator
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object DuelOverviewCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val player = sender
            if (!player.isInFight() && !player.isInSoupsimulator()) {
                if (args.size == 2) {
                    if (Data.gameIDs.contains(args[0])) {
                        val duel = Data.duelFromID[args[0]]
                        val playerOneOrTwo = Bukkit.getPlayer(args[1])
                        if (duel!!.p1 == playerOneOrTwo || duel.p2 == playerOneOrTwo) {
                            DuelOverviewGUI.openPlayerOverview(player, args[0], playerOneOrTwo)
                        } else {
                            player.sendLocalizedMessage(
                                Localization.DUELOVERVIEW_COMMAND_PLAYER_NOT_FOUND_DE.replace("%gameID%", args[0])
                                    .replace("%player%", args[1]),
                                Localization.DUELOVERVIEW_COMMAND_PLAYER_NOT_FOUND_EN.replace("%gameID%", args[0])
                                    .replace("player", args[1])
                            )
                        }
                    } else {
                        player.sendLocalizedMessage(
                            Localization.DUELOVERVIEW_COMMAND_GAME_NOT_FOUND_DE.replace("%gameID%", args[0]),
                            Localization.DUELOVERVIEW_COMMAND_GAME_NOT_FOUND_EN.replace("%gameID%", args[0])
                        )
                    }
                } else if (args.size == 1) {
                    player.sendMessage(Localization.DUELOVERVIEW_HELP)
                }

            } else {
                player.sendLocalizedMessage(
                    Localization.CANT_DO_THAT_RIGHT_NOW_DE,
                    Localization.CANT_DO_THAT_RIGHT_NOW_EN
                )
            }
        }
        return false
    }
}


