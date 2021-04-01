package de.hglabor.plugins.duels.utils

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.arenas.Arenas
import de.hglabor.plugins.duels.functionality.MainInventory
import de.hglabor.plugins.duels.functionality.PartyInventory
import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.localization.sendMsg
import de.hglabor.plugins.duels.party.Party
import de.hglabor.plugins.duels.party.Partys.hasParty
import de.hglabor.plugins.duels.party.Partys.isInParty
import de.hglabor.plugins.duels.player.DuelsPlayer
import de.hglabor.plugins.duels.scoreboard.LobbyScoreboard
import de.hglabor.plugins.duels.spawn.SpawnUtils
import de.hglabor.plugins.staff.functionality.StaffInventory
import de.hglabor.plugins.staff.utils.StaffData.isInStaffMode
import de.hglabor.plugins.staff.utils.StaffData.isVanished
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.chat.sendMessage
import net.axay.kspigot.extensions.bukkit.feedSaturate
import net.axay.kspigot.extensions.bukkit.heal
import net.axay.kspigot.extensions.onlinePlayers
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType


object PlayerFunctions {
    fun Player.challenge(target: Player, kit: AbstractKit) {
        val player: Player = player!!

        if (!Arenas.arenaWithTagExists(kit.arenaTag)) {
            player.sendMsg("arena.notFound", mutableMapOf("tag" to kit.arenaTag.toString()))
            return
        }

        player.sendMsg("duel.dueledPlayer", mutableMapOf("playerName" to target.name, "kit" to kit.name))
        if (player.hasParty()) {
            target.sendMsg("duel.playerDueled", mutableMapOf("playerName" to "${player.name}'s ${KColors.CORNSILK}Party (${Party.get(player)!!.players.size})§7", "kit" to kit.name))
        } else {
            target.sendMsg("duel.playerDueled", mutableMapOf("playerName" to player.name, "kit" to kit.name))
        }

        val message = TextComponent("")
        val one = TextComponent("  [")
        one.color = KColors.DARKGRAY
        val text = TextComponent("Click")
        message.color = KColors.DODGERBLUE
        message.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/challenge accept ${player.name}")
        val two = TextComponent("]")
        two.color = KColors.DARKGRAY
        message.addExtra(one)
        message.addExtra(text)
        message.addExtra(two)
        target.sendMessage(message)
        target.playSound(target.location, Sound.BLOCK_NOTE_BLOCK_PLING, 3f, 1f)

        Data.challengeKit[player] = kit
        Data.challenged[player] = target
    }

    fun Player.reset() {
        val player = this
        DuelsPlayer.get(player)
        if (player.isInStaffMode)
            StaffInventory.giveItems(player)
        else if (player.isInParty())
            PartyInventory.giveItems(player)
        else
            MainInventory.giveItems(player)
        player.gameMode = GameMode.ADVENTURE
        player.fireTicks = 0
        player.exp = 0f
        player.level = 0
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = 20.0
        player.health = 20.0
        player.absorptionAmount = 0.0
        player.isFlying = false
        player.allowFlight = false
        player.feedSaturate()
        player.heal()
        onlinePlayers.filter { !it.isVanished }.forEach { player.showPlayer(Manager.INSTANCE, it) }
        player.teleport(SpawnUtils.getSpawn())
        Data.openedDuelGUI.remove(player)
        Data.challengeKit.remove(player)
        Data.challenged.remove(player)
        Data.duelOfSpec.remove(player)
        Data.duelOfPlayer.remove(player)
        Data.inFight.remove(player)
        LobbyScoreboard.setScoreboard(player)

        player.activePotionEffects.forEach {
            player.removePotionEffect(it.type)
        }
        player.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 25, 1))
        player.isGlowing = true
    }

    fun Player.localization(locale: String): Boolean {
        return player!!.locale.toLowerCase().contains(locale)
    }
}
