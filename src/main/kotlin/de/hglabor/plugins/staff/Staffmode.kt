package de.hglabor.plugins.staff

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.localization.sendMsg
import de.hglabor.plugins.duels.player.DuelsPlayer
import de.hglabor.plugins.duels.utils.PlayerFunctions.reset
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
            player.sendMsg("staff.enabled")
        } else {
            StaffData.playersInStaffmode.remove(player)
            if (followedPlayerFromStaff.containsKey(player)) {
                player.unfollow()
                followedPlayerFromStaff.remove(player)
            }

            toggleVanish(player)
            player.sendMsg("staff.disabled")
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

    private fun Player.teleportToFollowedPlayer() {
        if (followedPlayerFromStaff.containsKey(this)) {
            val target = followedPlayerFromStaff[this] ?: return
            val duelsTarget = DuelsPlayer.get(target)
            if (!duelsTarget.isInFight()) {
                this.sendMsg("staff.follow.playerAtSpawn", mutableMapOf("playerName" to target.name))
            } else {
                this.sendMsg("staff.follow.playerStartedFight", mutableMapOf("playerName" to target.name))
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