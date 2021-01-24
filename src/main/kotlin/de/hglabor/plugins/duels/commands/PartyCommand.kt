package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.functionality.PartyInventory
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.party.Party
import de.hglabor.plugins.duels.party.Partys.hasParty
import de.hglabor.plugins.duels.party.Partys.isInParty
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object PartyCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val player = sender

            if (player.isInFight()) {
                var cancel = true
                if (args.size > 1)
                    if (args[0].equals("info", true))
                        cancel = false
                if (cancel)
                    player.sendLocalizedMessage(Localization.CANT_DO_THAT_RIGHT_NOW_DE, Localization.CANT_DO_THAT_RIGHT_NOW_EN)
            }

            if (args.size == 1) {
                if (args[0].equals("create", true)) {
                    if (!player.isInParty()) {
                        Party(player).create(true)
                        PartyInventory.giveItems(player)
                    } else
                        player.sendLocalizedMessage(Localization.PARTY_COMMAND_ALREADY_IN_PARTY_DE, Localization.PARTY_COMMAND_ALREADY_IN_PARTY_EN)

                } else if (args[0].equals("leave", true)) {
                    if (player.isInParty()) {
                        if (player.hasParty())
                            Party.get(player)?.delete()
                        else
                            Party.get(player)?.leave(player)
                    } else
                        player.sendLocalizedMessage(Localization.PARTY_COMMAND_NOT_IN_PARTY_DE, Localization.PARTY_COMMAND_NOT_IN_PARTY_EN)

                } else if (args[0].equals("info", true)) {
                    if (player.isInParty()) {
                        Party.get(player)?.sendInfo(player)
                    } else
                        player.sendLocalizedMessage(Localization.PARTY_COMMAND_NOT_IN_PARTY_DE, Localization.PARTY_COMMAND_NOT_IN_PARTY_EN)

                } else if (args[0].equals("public", true)) {
                    if (player.hasParty())
                        Party.get(player)?.togglePrivacy()
                    else
                        player.sendLocalizedMessage(Localization.PARTY_COMMAND_NOT_IN_PARTY_DE, Localization.PARTY_COMMAND_NOT_IN_PARTY_EN)
                }
            } else if (args.size == 2) {
                if (args[0].equals("invite", true)) {
                    val target = Bukkit.getPlayer(args[1])
                    if (target != null)
                        if (!target.isInParty()) {
                            val party = Party.getOrCreate(player, true)
                            if (party.leader == player)
                                if (!party.invitedPlayers.contains(target))
                                    party.invitePlayer(target)
                                else
                                    player.sendLocalizedMessage(Localization.PARTY_COMMAND_PLAYER_ALREADY_INVITED_DE, Localization.PARTY_COMMAND_PLAYER_ALREADY_INVITED_EN, "%playerName%", target.name)
                        } else
                            player.sendLocalizedMessage(Localization.PARTY_COMMAND_PLAYER_ALREADY_IN_PARTY_DE, Localization.PARTY_COMMAND_PLAYER_ALREADY_IN_PARTY_EN, "%playerName%", target.name)
                    else
                        player.sendLocalizedMessage(Localization.PLAYER_NOT_ONLINE_DE, Localization.PLAYER_NOT_ONLINE_EN, "%playerName%", args[1])


                } else if (args[0].equals("join", true)) {
                    if (!player.isInParty()) {
                        val target = Bukkit.getPlayer(args[1])
                        if (target != null)
                            if (target.hasParty()) {
                                val party = Party.get(target)
                                if (party!!.invitedPlayers.contains(player) || party.isPublic)
                                    party.addPlayer(player)
                                else
                                    player.sendLocalizedMessage(Localization.PARTY_COMMAND_CANT_JOIN_DE, Localization.PARTY_COMMAND_CANT_JOIN_EN, "%playerName%", target.name)
                            } else
                                player.sendLocalizedMessage(Localization.PARTY_COMMAND_PLAYER_HAS_NO_PARTY_DE, Localization.PARTY_COMMAND_PLAYER_HAS_NO_PARTY_EN, "%playerName%", target.name)
                        else
                            player.sendLocalizedMessage(Localization.PLAYER_NOT_ONLINE_DE, Localization.PLAYER_NOT_ONLINE_EN, "%playerName%", args[1])
                    } else
                        player.sendLocalizedMessage(Localization.PARTY_COMMAND_ALREADY_IN_PARTY_DE, Localization.PARTY_COMMAND_ALREADY_IN_PARTY_EN)

                } else if (args[0].equals("kick", true)) {
                    if (player.isInParty()) {
                        val party = Party.get(player)!!
                        if (party.leader == player) {
                            val target = Bukkit.getPlayer(args[1])
                            if (target != null) {
                                if (party.players.contains(target)) {
                                    party.kick(target)
                                } else
                                    player.sendLocalizedMessage(Localization.PARTY_COMMAND_PLAYER_NOT_IN_OWN_DE, Localization.PARTY_COMMAND_PLAYER_NOT_IN_OWN_EN, "%playerName%", target.name)
                            } else
                                player.sendLocalizedMessage(Localization.PLAYER_NOT_ONLINE_DE, Localization.PLAYER_NOT_ONLINE_EN, "%playerName%", args[1])
                        } else
                            player.sendLocalizedMessage(Localization.PARTY_COMMAND_NOT_LEADER_DE, Localization.PARTY_COMMAND_NOT_LEADER_EN)
                    } else
                        player.sendLocalizedMessage(Localization.PARTY_COMMAND_NOT_IN_PARTY_DE, Localization.PARTY_COMMAND_NOT_IN_PARTY_DE)
                } else if (args[0].equals("info", true)) {
                    val target = Bukkit.getPlayer(args[1])
                    if (target != null) {
                        if (target.isInParty()) {
                            Party.get(target)?.sendInfo(player)
                        } else
                            player.sendLocalizedMessage(Localization.PARTY_COMMAND_PLAYER_NOT_IN_PARTY_DE, Localization.PARTY_COMMAND_PLAYER_NOT_IN_PARTY_EN)
                    } else
                        player.sendLocalizedMessage(Localization.PLAYER_NOT_ONLINE_DE, Localization.PLAYER_NOT_ONLINE_EN, "%playerName%", args[1])
                }
            } else
                Party.help(player)
        }
        return false
    }
}