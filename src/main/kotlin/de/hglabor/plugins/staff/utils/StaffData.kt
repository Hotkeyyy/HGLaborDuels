package de.hglabor.plugins.staff.utils

import de.hglabor.plugins.duels.utils.Ranks
import org.bukkit.entity.Player

object StaffData {
    val playersInStaffmode = arrayListOf<Player>()
    val vanishedPlayers = arrayListOf<Player>()
    val followedPlayerFromStaff = hashMapOf<Player, Player>() // Staff, Player
    val followingStaffFromPlayer = hashMapOf<Player, ArrayList<Player>>() // Player, ArrayList of staff

    val Player.isStaff get() = when (Ranks.getRank(this)) {
            Ranks.Rank.OWNER, Ranks.Rank.ADMIN, Ranks.Rank.MOD, Ranks.Rank.HELPER -> true
            else -> false
        }

    val Player.isInStaffMode: Boolean get() = playersInStaffmode.contains(player)
    val Player.isVanished: Boolean get() = vanishedPlayers.contains(player)
}