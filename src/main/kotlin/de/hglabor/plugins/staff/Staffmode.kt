package de.hglabor.plugins.staff

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.duels.utils.PlayerFunctions.reset
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
import de.hglabor.plugins.staff.functionality.StaffInventory
import de.hglabor.plugins.staff.utils.StaffData
import de.hglabor.plugins.staff.utils.StaffData.followedPlayerFromStaff
import de.hglabor.plugins.staff.utils.StaffData.followingStaffFromPlayer
import de.hglabor.plugins.staff.utils.StaffData.isInStaffMode
import de.hglabor.plugins.staff.utils.StaffData.isStaff
import de.hglabor.plugins.staff.utils.StaffData.vanishedPlayers
import net.axay.kspigot.extensions.onlinePlayers
import org.bukkit.entity.Player

object Staffmode {

    fun toggle(player: Player) {
        if (!player.isInStaffMode) {
            StaffData.playersInStaffmode.add(player)
            toggleVanish(player)
            StaffInventory.giveItems(player)
            player.sendLocalizedMessage(Localization.STAFFMODE_ENABLED_DE, Localization.STAFFMODE_ENABLED_EN)

        } else {
            StaffData.playersInStaffmode.remove(player)
            if (followedPlayerFromStaff.containsKey(player)) {
                player.unfollow()
                followedPlayerFromStaff.remove(player)
            }

            toggleVanish(player)
            player.sendLocalizedMessage(Localization.STAFFMODE_DISABLED_DE, Localization.STAFFMODE_DISABLED_EN)
            player.reset()
        }
    }

    fun toggleVanish(player: Player) {
        if(vanishedPlayers.contains(player)) {
            vanishedPlayers.remove(player)
            onlinePlayers.forEach { it.showPlayer(Manager.INSTANCE, player) }
            player.inventory.setItem(3, StaffInventory.vanishItem(player))
        } else {
            vanishedPlayers.add(player)
            onlinePlayers.filter { !it.isStaff }.forEach { it.hidePlayer(Manager.INSTANCE, player) }
            player.inventory.setItem(3, StaffInventory.unvanishItem(player))
        }
    }

    fun Player.follow(target: Player) {
        val player: Player = player!!
        val stafflist: ArrayList<Player>
        if(followingStaffFromPlayer.containsKey(target))
            stafflist = followingStaffFromPlayer[target]!!
        else
            stafflist = arrayListOf()
        stafflist.add(player)

        followingStaffFromPlayer[target] = stafflist
        followedPlayerFromStaff[player] = target
        player.teleportToFollowedPlayer()
    }

    fun Player.teleportToFollowedPlayer() {
        if (followedPlayerFromStaff.containsKey(player)) {
            val target = followedPlayerFromStaff[player]
            if (!target!!.isInFight()) {
                player!!.sendLocalizedMessage(Localization.STAFF_PLAYER_IS_AT_SPAWN_DE.replace("%playerName%", target.displayName),
                    Localization.STAFF_PLAYER_IS_AT_SPAWN_EN.replace("%playerName%", target.displayName))
            } else {
                player!!.sendLocalizedMessage(Localization.FOLLOW_COMMAND_PLAYER_STARTED_FIGHTING_DE.replace("%playerName%", target.displayName),
                    Localization.FOLLOW_COMMAND_PLAYER_STARTED_FIGHTING_EN.replace("%playerName%", target.displayName))
                player!!.teleport(target)
            }
        }
    }

    fun Player.unfollow() {
        val player: Player = player!!
        val target = followedPlayerFromStaff[player]
        val stafflist = followingStaffFromPlayer[target]
        stafflist!!.remove(player)
        followingStaffFromPlayer[target!!] = stafflist
        followedPlayerFromStaff.remove(player)
    }
}