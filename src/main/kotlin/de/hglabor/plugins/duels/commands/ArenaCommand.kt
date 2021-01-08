package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.arenas.CreateArena
import de.hglabor.plugins.duels.guis.CreateArenaGUI
import de.hglabor.plugins.duels.arenas.arenaFromPlayer
import de.hglabor.plugins.duels.functionality.CreateArenaInventory
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.soupsimulator.isInSoupsimulator
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import net.axay.kspigot.chat.KColors
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.util.ArrayList

object ArenaCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val player = sender
            if (player.hasPermission("duels.createarena")) {
                if (!player.isInFight() && !player.isInSoupsimulator()) {
                    if (args.size == 1) {
                        if (args[0].equals("create", true)) {
                            if (!arenaFromPlayer.containsKey(player)) {
                                arenaFromPlayer[player] = CreateArena(player)
                                CreateArenaInventory.giveItems(player)
                            }
                            CreateArenaGUI.openCreateArenaGUI(player)
                            return true
                        } else if (args[0].equals("buildworld", true)) {
                            player.teleport(Location(Bukkit.getWorld("BuildWorld"), 0.0, 5.0, 0.0))
                            return true
                        } else if (args[0].equals("fightworld", true)) {
                            player.teleport(Location(Bukkit.getWorld("FightWorld"), 0.0, 150.0, 0.0))
                            return true
                        } else if (args[0].equals("list", true)) {
                            val file = File("arenas//arenas.yml")
                            val yamlConfiguration = YamlConfiguration.loadConfiguration(file)
                            val arenas = arrayListOf<String>()
                            var message = ""
                            for (arena in yamlConfiguration.getKeys(false)) {
                                arenas.add(arena)
                            }

                            for (i in 0 until arenas.size) {
                                message += arenas[i]
                                if (i < arenas.size)
                                    message += ", "
                            }

                            player.sendMessage("${Localization.PREFIX}Arenas ${KColors.DARKGRAY}» ${KColors.GRAY}$message")
                            return true
                        }
                    }
                    if (player.localization("de")) {
                        player.sendMessage(Localization.COMMAND_WRONG_ARGUMENTS_DE)
                        player.sendMessage(Localization.ARENA_COMMAND_HELP1_DE)
                        player.sendMessage(Localization.ARENA_COMMAND_HELP2_DE)
                        player.sendMessage(Localization.ARENA_COMMAND_HELP3_DE)
                        player.sendMessage(Localization.ARENA_COMMAND_HELP4_DE)
                    } else {
                        player.sendMessage(Localization.COMMAND_WRONG_ARGUMENTS_EN)
                        player.sendMessage(Localization.ARENA_COMMAND_HELP1_EN)
                        player.sendMessage(Localization.ARENA_COMMAND_HELP2_EN)
                        player.sendMessage(Localization.ARENA_COMMAND_HELP3_EN)
                        player.sendMessage(Localization.ARENA_COMMAND_HELP4_EN)
                    }
                } else {
                    if (player.localization("de"))
                        player.sendMessage(Localization.CANT_DO_THAT_RIGHT_NOW_DE)
                    else
                        player.sendMessage(Localization.CANT_DO_THAT_RIGHT_NOW_EN)
                }

            } else {
                if (player.localization("de"))
                    player.sendMessage(Localization.NO_PERM_DE)
                else
                    player.sendMessage(Localization.NO_PERM_EN)
            }
        }
        return false
    }

    fun onTabComplete(sender: CommandSender?, command: Command, alias: String?, args: Array<String?>): List<String>? {
        if (command.name.equals("arena", true)) {
            val l: MutableList<String> = ArrayList()
            if (args.size == 1) {
                l.add("create")
                l.add("arenaworld")
                l.add("buildworld")
                l.add("list")
            }
            return l
        }
        return null
    }
}


