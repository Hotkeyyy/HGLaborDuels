package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.guis.KitsGUI
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.localization.sendMsg
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
                    player.sendMsg("command.cantExecuteNow")
                    return false
                }
            }

            if (!player.isInFight() && !player.isInSoupsimulator()) {
                // DUEL
                if (args.size == 1) {
                    val target = Bukkit.getPlayer(args[0])
                    if (target != null) {
                        if (player == target) {
                            player.sendMsg("challenge.deny.cantDuelSelf")
                            return false
                        }

                        Data.openedDuelGUI[player] = target
                        Data.openedKitInventory[player] = Data.KitInventories.DUEL
                        player.openGUI(KitsGUI.guiBuilder(player))
                    } else {
                        player.sendMsg("playerNotOnline", mutableMapOf("%playerName%" to args[0]))
                    }

                    // ACCEPT
                } else if (args.size == 2 && args[0].equals("accept", true)) {
                    val target = Bukkit.getPlayer(args[1])
                    if (target != null) {
                        if (target.isInFight()) {
                            player.sendMsg("challenge.playerAlreadyInFight", mutableMapOf("%playerName%" to target.name))
                            return false
                        }
                        if (Data.challenged[target] == sender) {
                            Duel.create(sender, target, Data.challengeKit[target]!!)
                        }
                    } else {
                        player.sendMsg("playerNotOnline", mutableMapOf("playerName" to args[0]))
                    }
                } else {
                    player.sendMsg("command.wrongArguments")
                    player.sendMsg("challenge.help")
                }
            } else {
                player.sendMsg("command.cantExecuteNow")
            }
        } else {
            sender.info("Du musst ein Spieler sein.")
        }
        return false
    }
}