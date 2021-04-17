package de.hglabor.plugins.duels.team

import net.axay.kspigot.chat.KColors
import net.md_5.bungee.api.ChatColor

class TeamColor(val mainColor: ChatColor, val secondaryColor: ChatColor, val bukkitColor: org.bukkit.ChatColor) {
    companion object {
        val RED = TeamColor(KColors.DARKRED, ChatColor.RED, org.bukkit.ChatColor.DARK_RED)
        val BLUE = TeamColor(KColors.DEEPSKYBLUE, KColors.LIGHTSKYBLUE, org.bukkit.ChatColor.AQUA)
        val PINK = TeamColor(KColors.DEEPPINK, KColors.HOTPINK, org.bukkit.ChatColor.LIGHT_PURPLE)
        val GREEN = TeamColor(KColors.DARKGREEN, KColors.LIGHTGREEN, org.bukkit.ChatColor.GREEN)
        val ORANGE = TeamColor(KColors.DARKORANGE, KColors.ORANGE, org.bukkit.ChatColor.GOLD)
        val PURPLE = TeamColor(KColors.PURPLE, KColors.LIGHTPURPLE, org.bukkit.ChatColor.DARK_PURPLE)
        val BLACK = TeamColor(KColors.BLACK, KColors.DARKGRAY, org.bukkit.ChatColor.BLACK)
        val teamColors = mutableListOf(RED, BLUE, PINK, GREEN, ORANGE, PURPLE, BLACK)

        fun randomColor(excludedColors: TeamColor? = null): TeamColor {
            return teamColors.filter { it != excludedColors }.random()
        }
    }
}