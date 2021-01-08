package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.party.Party
import de.hglabor.plugins.duels.party.Partys.getPartyFromPlayer
import de.hglabor.plugins.duels.party.Partys.hasParty
import de.hglabor.plugins.duels.party.Partys.isInOrHasParty
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
import de.hglabor.plugins.duels.utils.Ranks
import net.axay.kspigot.chat.KColors
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object PartyCommand : CommandExecutor {
    val test = arrayListOf<Player>()
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val player = sender
            if (test.contains(player) || Ranks.getRank(player) == Ranks.Rank.OWNER || Ranks.getRank(player) == Ranks.Rank.ADMIN) {
                if (args.size == 1) {
                    if (args[0].equals("create", true)) {
                        if (!player.isInOrHasParty())
                            Party(player)
                        else
                            player.sendMessage("${KColors.TOMATO}du hast schon eine party")

                    } else if (args[0].equals("leave", true)) {
                        if (player.isInOrHasParty()) {
                            if (player.hasParty())
                                getPartyFromPlayer(player).delete()
                            else
                                getPartyFromPlayer(player).removePlayer(player)
                        } else
                            player.sendMessage("du bist in keiner party")

                    } else if (args[0].equals("list", true)) {
                        if (player.isInOrHasParty()) {
                            getPartyFromPlayer(player).listPlayers(player)
                        } else
                            player.sendMessage("du bist in keiner party")

                    } else if (args[0].equals("public", true)) {
                        if (player.hasParty())
                            getPartyFromPlayer(player).togglePrivacy()
                        else
                            player.sendMessage("du hast keine party")
                    }
                } else if (args.size == 2) {
                    if (args[0].equals("givepermissions")) {
                        val target = Bukkit.getPlayer(args[1])
                        if (target != null) {
                            test.add(target)
                            target.sendMessage("rechte für party bekommen")
                            player.sendMessage("du hast ${target.name} rechte gegeben")
                        } else
                            player.sendMessage("${args[0]} ist nicht online")

                    } else if (args[0].equals("removepermissions")) {
                        val target = Bukkit.getPlayer(args[1])
                        if (target != null) {
                            test.remove(target)
                            target.sendMessage("rechte für party genommen")
                            player.sendMessage("du hast ${target.name} rechte genommen")
                        } else
                            player.sendMessage("${args[0]} ist nicht online")

                    } else if (args[0].equals("invite", true)) {
                        if (player.hasParty()) {
                            val target = Bukkit.getPlayer(args[1])
                            if (target != null)
                                if (!target.isInOrHasParty())
                                    getPartyFromPlayer(player).invitePlayer(target)
                                else
                                    player.sendMessage("spieler ${target.name} ist bereits in einer party")
                            else
                                player.sendMessage("spieler ${args[1]} ist nicht online")
                        } else
                            player.sendMessage("${KColors.TOMATO}du hast keine party")

                    } else if (args[0].equals("join", true)) {
                        if (!player.isInOrHasParty()) {
                            val target = Bukkit.getPlayer(args[1])
                            if (target != null)
                                if (target.hasParty()) {
                                    val party = getPartyFromPlayer(target)
                                    if (party.invitedPlayers.contains(player) || party.isPublic)
                                        getPartyFromPlayer(target).addPlayer(player)
                                    else
                                        player.sendMessage("${target.name} hat dich nicht eingeladen und party ist nicht public")
                                } else
                                    player.sendMessage("${target.name} hat keine party")
                            else
                                player.sendMessage("${args[0]} ist nicht online")
                        } else
                            player.sendMessage("${KColors.TOMATO}du bist bereits in einer party")

                    } else if (args[0].equals("list", true)) {
                        val target = Bukkit.getPlayer(args[1])
                        if (target != null) {
                            if (target.isInOrHasParty()) {
                                getPartyFromPlayer(target).listPlayers(player)
                            } else
                                player.sendMessage("${target.name} ist in keiner party")
                        } else
                            player.sendMessage("${args[0]} ist nicht online")
                    }
                } else {
                    player.sendMessage("/party create")
                    player.sendMessage("/party leave")
                    player.sendMessage("/party list (player)")
                    player.sendMessage("/party public")
                    player.sendMessage("/party givepermissions player")
                    player.sendMessage("/party removepermissions player")
                    player.sendMessage("/party invite player")
                    player.sendMessage("/party join player")
                }
            } else {
                player.sendLocalizedMessage(Localization.NO_PERM_DE, Localization.NO_PERM_EN)
            }
        }
        return false
    }
}