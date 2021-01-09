package de.hglabor.plugins.duels.utils

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.arenas.Arenas
import de.hglabor.plugins.duels.database.temporaryalternative.Stats
import de.hglabor.plugins.duels.functionality.MainInventory
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.Kits.Companion.info
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.scoreboard.LobbyScoreboard
import de.hglabor.plugins.duels.settings.Settings
import de.hglabor.plugins.duels.spawn.SpawnUtils
import de.hglabor.plugins.duels.utils.PlayerFunctions.reset
import de.hglabor.plugins.staff.utils.StaffData.isVanished
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.bukkit.feedSaturate
import net.axay.kspigot.extensions.bukkit.heal
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.utils.mark
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scoreboard.Team
import java.io.File


object PlayerFunctions {

    fun Player.isInFight(): Boolean {
        return Data.inFight.contains(player)
    }

    fun Player.getEnemy(): Player {
        return Data.duel[player]!!
    }

    fun Player.duel(target: Player, kit: Kits) {
        val player: Player = player!!
//        if (player == target) {
//            player.sendLocalizedMessage(Localization.CHALLENGE_COMMAND_ACCEPT_CANT_DUEL_SELF_DE, Localization.CHALLENGE_COMMAND_ACCEPT_CANT_DUEL_SELF_EN)
//            return
//        }

        if (!Arenas.arenaWithTagExists(kit.info.arenaTag)) {
            player.sendLocalizedMessage(
                Localization.NO_ARENA_FOUND_DE.replace("%tag%", kit.info.arenaTag.toString()),
                Localization.NO_ARENA_FOUND_EN.replace("%tag%", kit.info.arenaTag.toString())
            )
            return
        }

        player.sendLocalizedMessage(
            Localization.YOU_DUELED_DE.replace("%playerName%", target.name).replace("%kit%", kit.info.name),
            Localization.YOU_DUELED_EN.replace("%playerName%", target.name).replace("%kit%", kit.info.name)
        )

        target.sendLocalizedMessage(
            Localization.YOU_WERE_DUELED_DE.replace("%playerName%", player.name).replace("%kit%", kit.info.name),
            Localization.YOU_WERE_DUELED_EN.replace("%playerName%", player.name).replace("%kit%", kit.info.name)
        )

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
        Data.duelIDFromSpec.remove(player)
        Data.inFight.remove(player)
        LobbyScoreboard.setScoreboard(player)

        player.activePotionEffects.forEach {
            player.removePotionEffect(it.type)
        }
        player.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 25, 1))
        player.isGlowing = true
    }

    fun Player.spec(gameID: String, notifyPlayers: Boolean) {
        val player: Player = player!!
        val duel = Data.duelFromID[gameID]
        val spawnOne: Location = duel!!.arena.spawn1Loc
        val spawnTwo: Location = duel.arena.spawn2Loc
        val x = (spawnOne.x + spawnTwo.x) / 2
        val y = spawnOne.y + 3
        val z = (spawnOne.z + spawnTwo.z) / 2
        val centerLoc = Location(Bukkit.getWorld("FightWorld"), x, y, z)
        Data.duelIDFromSpec[player] = gameID
        duel.addSpectator(player)
        player.inventory.clear()
        player.teleport(centerLoc)
        player.allowFlight = true
        player.isFlying = true

        for (duelplayers in duel.players) {
            duelplayers.hidePlayer(Manager.INSTANCE, player)
        }

        if (notifyPlayers)
            duel.sendMessage(
                Localization.PLAYER_STARTED_SPECTATING_DE.replace("%playerName%", player.name),
                Localization.PLAYER_STARTED_SPECTATING_EN.replace("%playerName%", player.name)
            )

        player.inventory.setItem(
            8,
            itemStack(Material.MAGENTA_GLAZED_TERRACOTTA) {
                meta {
                    name = if (player.localization("de"))
                        Localization.STOP_SPECTATING_ITEM_NAME_DE
                    else
                        Localization.STOP_SPECTATING_ITEM_NAME_EN
                }; mark("stopspec")
            })
    }

    fun Player.stopSpectating(forced: Boolean) {
        val player: Player = player!!
        val duel = Data.duelFromID[Data.duelIDFromSpec[player]]
        player.reset()
        duel!!.specs.remove(player)
        duel.involvedInDuel.remove(player)
        if (!forced)
            if (player.localization("de"))
                duel.sendMessage(
                    Localization.PLAYER_STOPPED_SPECTATING_DE.replace("%playerName%", player.name),
                    Localization.PLAYER_STOPPED_SPECTATING_EN.replace("%playerName%", player.name)
                )
    }

    fun Player.localization(locale: String): Boolean {
        return player!!.locale.toLowerCase().contains(locale)
    }

    fun Player.getStats(): Stats {
        return Stats(player!!.uniqueId)
    }

    fun Player.sendLocalizedMessage(germanMessage: String, englishMessage: String) {
        if (player!!.localization("de"))
            player!!.sendMessage(germanMessage)
        else
            player!!.sendMessage(englishMessage)
    }

    fun Player.hasRank(): Boolean {
        val file = File("plugins//HGLaborDuels//ranks.yml")
        val yamlConfiguration = YamlConfiguration.loadConfiguration(file)
        return yamlConfiguration.contains(player!!.uniqueId.toString())
    }
}