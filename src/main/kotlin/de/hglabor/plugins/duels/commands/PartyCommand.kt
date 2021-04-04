package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.utils.sendMsg
import de.hglabor.plugins.duels.party.Party
import de.hglabor.plugins.duels.party.Partys.hasParty
import de.hglabor.plugins.duels.party.Partys.isInParty
import de.hglabor.plugins.duels.player.DuelsPlayer
import net.axay.kspigot.extensions.onlinePlayers
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.*

object PartyCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val player = sender
            val duelsPlayer = DuelsPlayer.get(player)

            if (duelsPlayer.isBusy()) {
                if (!args[0].equals("info", true))
                    player.sendMsg("command.cantExecuteNow")
            }

            if (args.size == 1) {
                if (args[0].equals("create", true)) {
                    if (!player.isInParty()) {
                        Party(player).create(true)
                    } else {
                        player.sendMsg("party.fail.alreadyInParty")
                    }

                } else if (args[0].equals("leave", true)) {
                    if (player.isInParty()) {
                        if (player.hasParty())
                            Party.get(player)?.delete()
                        else
                            Party.get(player)?.leave(player)
                    } else {
                        player.sendMsg("party.fail.notInParty")
                    }

                } else if (args[0].equals("info", true)) {
                    if (player.isInParty()) {
                        Party.get(player)?.sendInfo(player)
                    } else
                        player.sendMsg("party.fail.notInParty")


                } else if (args[0].equals("public", true)) {
                    if (player.hasParty())
                        Party.get(player)?.togglePrivacy()
                    else
                        player.sendMsg("party.fail.dontOwnParty")
                }
            } else if (args.size == 2) {
                if (args[0].equals("invite", true)) {
                    val target = Bukkit.getPlayer(args[1])
                    if (target != null)
                        if (!target.isInParty()) {
                            val party = Party.getOrCreate(player, true)
                            if (party.leader == player) {
                                if (!party.invitedPlayers.contains(target)) {
                                    party.invitePlayer(target)
                                } else
                                    player.sendMsg("party.fail.playerAlreadyInvited",
                                        mutableMapOf("playerName" to target.name))
                            }
                        } else
                            player.sendMsg("party.fail.playerAlreadyInParty", mutableMapOf("playerName" to target.name))
                    else
                        player.sendMsg("playerNotOnline", mutableMapOf("playerName" to args[1]))

                } else if (args[0].equals("join", true)) {
                    if (!player.isInParty()) {
                        if (duelsPlayer.isBusy()) {
                            val target = Bukkit.getPlayer(args[1])
                            if (target != null)
                                if (target.hasParty()) {
                                    val party = Party.get(target)
                                    if (party!!.invitedPlayers.contains(player) || party.isPublic)
                                        party.addPlayer(player)
                                    else
                                        player.sendMsg(
                                            "party.fail.playerNotOwningPublicParty",
                                            mutableMapOf("playerName" to target.name))
                                } else
                                    player.sendMsg(
                                        "party.fail.playerNotOwningPublicParty",
                                        mutableMapOf("playerName" to target.name))
                            else
                                player.sendMsg("playerNotOnline", mutableMapOf("playerName" to args[1]))

                        } else
                            player.sendMsg("command.cantExecuteNow")

                    } else
                        player.sendMsg("party.fail.alreadyInParty")

                } else if (args[0].equals("kick", true)) {
                    if (player.isInParty()) {
                        val party = Party.get(player)!!
                        if (party.leader == player) {
                            val target = Bukkit.getPlayer(args[1])
                            if (target != null) {
                                if (party.players.contains(target)) {
                                    party.kick(target)
                                } else
                                    player.sendMsg(
                                        "party.fail.playerNotInYourParty",
                                        mutableMapOf("playerName" to target.name)
                                    )

                            } else
                                player.sendMsg("playerNotOnline", mutableMapOf("playerName" to args[1]))

                        } else
                            player.sendMsg("party.fail.dontOwnParty")
                    } else
                        player.sendMsg("party.fail.dontOwnParty")

                } else if (args[0].equals("info", true)) {
                    val target = Bukkit.getPlayer(args[1])
                    if (target != null) {
                        if (target.isInParty()) {
                            Party.get(target)?.sendInfo(player)
                        } else
                            player.sendMsg("party.fail.playerNotInParty", mutableMapOf("playerName" to target.name))

                    } else
                        player.sendMsg("playerNotOnline", mutableMapOf("playerName" to args[1]))
                }
            } else
                Party.help(player)
        }
        return false
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String>? {
        val l: MutableList<String> = ArrayList()
        if (args.size == 1) {
            l.add("create")
            l.add("info")
            l.add("leave")
            l.add("invite")
            l.add("join")
            l.add("kick")
            l.add("public")
            l.sort()
        } else if (args.size == 2) {
            if (args[0].equals("create", true)) {
                return null
            } else if (args[0].equals("info", true)) {
                onlinePlayers.forEach {
                    if (it.isInParty()) {
                        l.add(it.name)
                    }
                }
                l.sort()
                return l
            } else if (args[0].equals("leave", true)) {
                return null
            } else if (args[0].equals("invite", true)) {
                onlinePlayers.forEach {
                    if (!it.isInParty()) {
                        l.add(it.name)
                    }
                }
                l.sort()
                return l
            } else if (args[0].equals("kick", true)) {
                onlinePlayers.forEach {
                    if (it.isInParty()) {
                        l.add(it.name)
                    }
                }
                l.sort()
                return l
            } else if (args[0].equals("invite", true)) {
                onlinePlayers.forEach {
                    if (!it.isInParty()) {
                        l.add(it.name)
                    }
                }
                l.sort()
                return l
            } else if (args[0].equals("public", true)) {
                return null
            }
        }
        return l
    }
}