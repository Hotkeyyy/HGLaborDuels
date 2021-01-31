package de.hglabor.plugins.duels.utils

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.arenas.Arenas
import de.hglabor.plugins.duels.functionality.MainInventory
import de.hglabor.plugins.duels.functionality.PartyInventory
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.Kits.Companion.info
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.party.Party
import de.hglabor.plugins.duels.party.Partys.hasParty
import de.hglabor.plugins.duels.party.Partys.isInParty
import de.hglabor.plugins.duels.scoreboard.LobbyScoreboard
import de.hglabor.plugins.duels.spawn.SpawnUtils
import de.hglabor.plugins.staff.functionality.StaffInventory
import de.hglabor.plugins.staff.utils.StaffData.isInStaffMode
import de.hglabor.plugins.staff.utils.StaffData.isVanished
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.bukkit.feedSaturate
import net.axay.kspigot.extensions.bukkit.heal
import net.axay.kspigot.extensions.onlinePlayers
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType


object PlayerFunctions {

    fun Player.isInFight(): Boolean {
        return Data.inFight.contains(player)
    }

    fun Player.isSpectator(): Boolean {
        return Data.duelFromSpec.contains(player)
    }

    fun Player.duel(target: Player, kit: Kits) {
        val player: Player = player!!

        if (!Arenas.arenaWithTagExists(kit.info.arenaTag)) {
            player.sendLocalizedMessage(
                Localization.NO_ARENA_FOUND_DE.replace("%tag%", kit.info.arenaTag.toString()),
                Localization.NO_ARENA_FOUND_EN.replace("%tag%", kit.info.arenaTag.toString())
            )
            return
        }

        player.sendLocalizedMessage(
            Localization.YOU_DUELED_DE.replace("%playerName%", target.displayName).replace("%kit%", kit.info.name),
            Localization.YOU_DUELED_EN.replace("%playerName%", target.displayName).replace("%kit%", kit.info.name))

        if (player.hasParty()) {
            target.sendLocalizedMessage(
                Localization.YOU_WERE_DUELED_DE.replace("%kit%", kit.info.name),
                Localization.YOU_WERE_DUELED_EN.replace("%kit%", kit.info.name),
                "%playerName%", "${player.name}'s ${KColors.CORNSILK}Party (${Party.get(player)!!.players.size})§7")
        } else {
            target.sendLocalizedMessage(
                Localization.YOU_WERE_DUELED_DE.replace("%kit%", kit.info.name),
                Localization.YOU_WERE_DUELED_EN.replace("%kit%", kit.info.name),
                "%playerName%", "${player.name}")
        }

        val message = TextComponent("")
        val one = TextComponent("  [")
        one.color = KColors.DARKGRAY
        val text = TextComponent("Click")
        message.color = KColors.DODGERBLUE
        message.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/challenge accept ${player.name}")
        if (target.localization("de"))
            message.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text(Localization.ACCEPT_BUTTON_DE))
        else
            message.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text(Localization.ACCEPT_BUTTON_EN))
        val two = TextComponent("]")
        two.color = KColors.DARKGRAY
        message.addExtra(one)
        message.addExtra(text)
        message.addExtra(two)
        target.spigot().sendMessage(message)
        target.playSound(target.location, Sound.BLOCK_NOTE_BLOCK_PLING, 3f, 1f)

        Data.challengeKit[player] = kit
        Data.challenged[player] = target
    }

    fun Player.reset() {
        val player: Player = player!!
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
        Data.duelIDFromPlayer.remove(player)
        Data.duelFromSpec.remove(player)
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

    fun Player.sendLocalizedMessage(germanMessage: String, englishMessage: String) {
        if (player!!.localization("de"))
            player!!.sendMessage(germanMessage)
        else
            player!!.sendMessage(englishMessage)
    }

    fun Player.sendLocalizedMessage(germanMessage: String, englishMessage: String, toReplace: String, replacement: String) {
        if (player!!.localization("de"))
            player!!.sendMessage(germanMessage.replace(toReplace, replacement))
        else
            player!!.sendMessage(englishMessage.replace(toReplace, replacement))
    }
}
