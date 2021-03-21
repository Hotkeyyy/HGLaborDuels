package de.hglabor.plugins.staff.utils

import org.bukkit.entity.Player

object StaffData {
    val playersInStaffmode = arrayListOf<Player>()
    val vanishedPlayers = arrayListOf<Player>()
    val followedPlayerFromStaff = hashMapOf<Player, Player>() // Staff, Player
    val followingStaffFromPlayer = hashMapOf<Player, ArrayList<Player>>() // Player, ArrayList of staff

    val Player.isStaff get() = this.hasPermission("hglabor.spectator")

    val Player.isInStaffMode: Boolean get() = playersInStaffmode.contains(player)
    val Player.isVanished: Boolean get() = vanishedPlayers.contains(player)
}