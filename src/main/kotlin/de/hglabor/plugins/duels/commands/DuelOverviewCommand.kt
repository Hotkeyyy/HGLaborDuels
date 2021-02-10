package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.guis.overview.DuelOverviewGUI
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.soupsimulator.Soupsim.isInSoupsimulator
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
import net.axay.kspigot.chat.KColors
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object DuelOverviewCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val player = sender
            player.sendMessage("${KColors.TOMATO}This command is currently disabled.")
            return false
            if (!player.isInFight() && !player.isInSoupsimulator()) {
                if (args.size == 1) {
                    if (Data.gameIDs.contains(args[0])) {
                        DuelOverviewGUI.open(player, args[0])
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


