package de.hglabor.plugins.duels.data

import org.bukkit.entity.Player

object DataHolder {

    val playerStats = mutableMapOf<Player, PlayerStats>()
    val playerSettings = mutableMapOf<Player, PlayerSettings>()
}