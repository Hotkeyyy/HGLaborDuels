package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.guis.ChooseKitGUI
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.party.Party
import de.hglabor.plugins.duels.party.Partys.isInParty
import de.hglabor.plugins.duels.soupsimulator.Soupsim.isInSoupsimulator
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
import net.axay.kspigot.extensions.bukkit.info
import net.axay.kspigot.gui.openGUI
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


object ChallengeCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val player = sender
            if (player.isInParty()) {
                if (Party.get(player)?.leader != player) {
                    player.sendLocalizedMessage(
                        Localization.CANT_DO_THAT_RIGHT_NOW_DE,
                        Localization.CANT_DO_THAT_RIGHT_NOW_EN
                    )
                    return false
                }
            }

            if (!player.isInFight() && !player.isInSoupsimulator()) {
                // DUEL
                if (args.size == 1) {
                    val target = Bukkit.getPlayer(args[0])
                    if (target != null) {
                        /*if (player == target) {
                            player.sendLocalizedMessage(Localization.CHALLENGE_COMMAND_CANT_DUEL_SELF_DE, Localization.CHALLENGE_COMMAND_CANT_DUEL_SELF_EN)
                            return false
                        }*/

                        Data.openedDuelGUI[player] = target
                        Data.openedKitInventory[player] = Data.KitInventories.DUEL
                        player.openGUI(ChooseKitGUI.gui)
                    } else {
                        player.sendLocalizedMessage(
                            Localization.PLAYER_NOT_ONLINE_DE.replace("%playerName%", args[0]),
                            Localization.PLAYER_NOT_ONLINE_EN.replace("%playerName%", args[0])
                        )
                    }

                    // ACCEPT
                } else if (args.size == 2 && args[0].equals("accept", true)) {
                    val target = Bukkit.getPlayer(args[1])
                    if (target != null) {
                        if (target.isInFight()) {
                            player.sendLocalizedMessage(
                                Localization.CHALLENGE_COMMAND_ACCEPT_PLAYER_IN_FIGHT_DE.replace("%playerName%", target.name),
                                Localization.CHALLENGE_COMMAND_ACCEPT_PLAYER_IN_FIGHT_EN.replace("%playerName%", target.name)
                            )
                            return false
                        }
                        if (Data.challenged[target] == sender) {
                            Duel.create(sender, target, Data.challengeKit[target]!!)
                        }
                    } else {
                        player.sendLocalizedMessage(
                            Localization.PLAYER_NOT_ONLINE_DE.replace("%playerName%", args[1]),
                            Localization.PLAYER_NOT_ONLINE_EN.replace("%playerName%", args[1])
                        )
                    }
                } else {
                    player.sendLocalizedMessage(
                        Localization.COMMAND_WRONG_ARGUMENTS_DE,
                        Localization.COMMAND_WRONG_ARGUMENTS_EN
                    )
                    player.sendMessage(Localization.CHALLENGE_COMMAND_HELP)
                }
            } else {
                player.sendLocalizedMessage(
                    Localization.CANT_DO_THAT_RIGHT_NOW_DE,
                    Localization.CANT_DO_THAT_RIGHT_NOW_EN
                )
            }
        } else {
            sender.info("Du musst ein Spieler sein.")
        }
        return false
    }
}