package de.hglabor.plugins.duels.team

import net.axay.kspigot.chat.KColors
import net.md_5.bungee.api.ChatColor

class TeamColor(val mainColor: ChatColor, val secondaryColor: ChatColor, val bukkitColor: ChatColor) {
    companion object {
        val RED = TeamColor(KColors.DARKRED, ChatColor.RED, ChatColor.DARK_RED)
        val BLUE = TeamColor(KColors.DEEPSKYBLUE, KColors.LIGHTSKYBLUE, ChatColor.AQUA)
        val PINK = TeamColor(KColors.DEEPPINK, KColors.HOTPINK, ChatColor.LIGHT_PURPLE)
        val GREEN = TeamColor(KColors.DARKGREEN, KColors.LIGHTGREEN, ChatColor.GREEN)
        val ORANGE = TeamColor(KColors.DARKORANGE, KColors.ORANGE, ChatColor.GOLD)
        val PURPLE = TeamColor(KColors.PURPLE, KColors.LIGHTPURPLE, ChatColor.DARK_PURPLE)
        val BLACK = TeamColor(KColors.BLACK, KColors.DARKGRAY, ChatColor.BLACK)
    }
}