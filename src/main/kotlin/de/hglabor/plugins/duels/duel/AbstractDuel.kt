package de.hglabor.plugins.duels.duel

import InventorySerialization.SerializeItemStack
import com.boydti.fawe.FaweAPI
import com.boydti.fawe.`object`.RelightMode
import com.sk89q.worldedit.bukkit.BukkitWorld
import com.sk89q.worldedit.math.Vector3
import com.sk89q.worldedit.regions.CuboidRegion
import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.arenas.Arena
import de.hglabor.plugins.duels.arenas.Arenas
import de.hglabor.plugins.duels.database.data.PlayerSettings
import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.KitType
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.Kits.giveKit
import de.hglabor.plugins.duels.kits.kit.soup.Anchor
import de.hglabor.plugins.duels.player.DuelsPlayer
import de.hglabor.plugins.duels.team.Team
import de.hglabor.plugins.duels.utils.*
import de.hglabor.plugins.duels.utils.PlayerFunctions.reset
import de.hglabor.plugins.soupsimulator.Soupsimulator
import de.hglabor.plugins.staff.utils.StaffData
import me.libraryaddict.disguise.utilities.json.SerializerItemStack
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.chat.sendMessage
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.runnables.KSpigotRunnable
import net.axay.kspigot.runnables.task
import net.axay.kspigot.runnables.taskRunLater
import net.axay.kspigot.utils.mark
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.io.File
import java.io.IOException

abstract class AbstractDuel(val teamOne: Team, val teamTwo: Team, val kit: AbstractKit) {
    companion object {
        enum class DuelKnockback(val key: String) { OLD("1.8"), NEW("1.16"), ANCHOR("Anchor") }
        enum class GameState { STARTING, COUNTDOWN, INGAME, ENDED }
        enum class GameType { UNRANKED, RANKED, LMS, TOURNAMENT }
    }

    val gameID = Data.getFreeGameID()
    val path = File("plugins//HGLaborDuels//temp//duels//$gameID//")
    var gameState = GameState.STARTING
    val knockback = getDuelKnockback()
    var countdownTask: KSpigotRunnable? = null

    val competitors = mutableListOf<LivingEntity>()
    val spectators = mutableListOf<Player>()

    val arenaLocation = Data.getFreeLocation()
    val arena: Arena = Arena(arenaLocation, Arenas.getRandomArena(kit.arenaTag))
    val blocksPlacedDuringGame = mutableSetOf<Block>()

    val stats = mutableMapOf<Player, MutableMap<String, Any>>()

    init {
        competitors.addAll(teamOne.members)
        competitors.addAll(teamTwo.members)
        //TODO async { Bukkit.getPluginManager().callEvent(DuelStartEvent(this)) }
        competitors.filterIsInstance<Player>().forEach { competitor ->
            competitor.closeInventory()
            Soupsimulator.get(competitor)?.stop()
            competitor.isGlowing = false
            competitor.inventory.clear()

            if (knockback == DuelKnockback.OLD)
                competitor.setMetadata("oldKnockback", FixedMetadataValue(Manager.INSTANCE, ""))
            else
                competitor.removeMetadata("oldKnockback", Manager.INSTANCE)
        }
        //TODO Data.duelFromID[gameID] = this
    }

    fun prepareGame() {
        competitors.forEach { competitor ->
            competitor.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 40, 200))
        }
        arena.pasteSchematic()

        taskRunLater(20L, true) {
            competitors.forEach { competitor ->
                if (competitor is Player) {
                    competitor.gameMode = GameMode.SURVIVAL
                    competitor.giveKit(kit)
                }
                sendCountdown()
                teleportPlayersToSpawns()
            }
        }
    }

    open fun start() {}

    private fun sendCountdown() {
        gameState = GameState.COUNTDOWN
        var count = 3
        var colorcode = 'a'
        countdownTask = task(true, 20, period = 20, howOften = 4) {
            if (count == 2) colorcode = 'e'
            if (count == 1) colorcode = 'c'
            if (count != 0) {
                allPlayers().forEach { player ->
                    player.sendTitle("§$colorcode$count", "§$colorcode", 3, 13, 3)
                    player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 3f, 2f)
                }
            } else {
                allPlayers().forEach { player ->
                    player.sendTitle(Localization.getMessage("duel.starting.title", player), "§b", 3, 13, 3)
                    player.playSound(player.location, Sound.EVENT_RAID_HORN, 10f, 1f)
                    player.closeInventory()
                    player.removePotionEffect(PotionEffectType.SLOW)
                    player.removePotionEffect(PotionEffectType.JUMP)
                }
                gameState = GameState.INGAME
                start()
            }
            count--
        }
    }

    private fun teleportPlayersToSpawns() {
        competitors.forEach { competitor ->
            if (teamOne.members.contains(competitor)) {
                competitor.teleport(arena.spawn1Loc)
                LocationUtils.setDirection(competitor, arena.spawn2Loc)
            } else {
                competitor.teleport(arena.spawn2Loc)
                LocationUtils.setDirection(competitor, arena.spawn1Loc)
            }

            competitor.addPotionEffect(PotionEffect(PotionEffectType.SLOW, Int.MAX_VALUE, 200, false, false))
            competitor.addPotionEffect(PotionEffect(PotionEffectType.JUMP, Int.MAX_VALUE, 200, false, false))
            //player.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, Int.MAX_VALUE, 200, false, false))
        }
        val v1 =
            Vector3.at(
                arenaLocation.first * Data.locationMultiplier,
                100.0,
                arenaLocation.second * Data.locationMultiplier
            ).toBlockPoint()
        val v2 = Vector3.at(
            arenaLocation.first * Data.locationMultiplier + arena.clipboard.dimensions.x,
            100.0 + arena.clipboard.dimensions.y,
            arenaLocation.second * Data.locationMultiplier + arena.clipboard.dimensions.z
        ).toBlockPoint()
        val region = CuboidRegion(v1, v2)
        val bukkitWorld = BukkitWorld(Bukkit.getWorld("FightWorld")!!)
        FaweAPI.fixLighting(bukkitWorld, region, null, RelightMode.OPTIMAL)
    }

    fun savePlayerdata(player: Player) {
        val file = File("$path//playerdata//${player.uniqueId}.yml")
        if (!file.parentFile.exists())
            file.parentFile.mkdirs()
        file.createNewFile()
        val yamlConfiguration = YamlConfiguration.loadConfiguration(file)

        yamlConfiguration["data.playername"] = player.name
        yamlConfiguration["data.team"] = if (teamOne.members.contains(player)) "ONE" else "TWO"
        if (competitors.contains(player))
            yamlConfiguration["data.health"] = player.health.toInt()
        else
            yamlConfiguration["data.health"] = 0
        yamlConfiguration["data.hits"] = stats[player]?.get("hits") ?: 0
        yamlConfiguration["data.longestCombo"] = stats[player]?.get("longesCombo") ?: 0

        if (kit.type == KitType.SOUP) {
            var soupsLeft = 0
            for (slot in 0..35) {
                if (player.inventory.getItem(slot) != null) {
                    if (player.inventory.getItem(slot)!!.type == Material.MUSHROOM_STEW)
                        soupsLeft++
                }
            }
            yamlConfiguration["data.presoups"] = stats[player]?.get("presoups") ?: 0
            yamlConfiguration["data.wastedHealth"] = stats[player]?.get("wastedHealth") ?: 0
            yamlConfiguration["data.soupsLeft"] = soupsLeft
        }

        if (kit.type == KitType.POT) {
            var potsLeft = 0
            for (slot in 0..35) {
                if (player.inventory.getItem(slot) != null) {
                    if (player.inventory.getItem(slot)!!.type == Material.SPLASH_POTION)
                        potsLeft++
                }
            }
            yamlConfiguration["data.missedPots"] = stats[player]?.get("missedPots") ?: 0
            yamlConfiguration["data.wastedHealth"] = stats[player]?.get("wastedHealth") ?: 0
            yamlConfiguration["data.potsLeft"] = potsLeft
        }

        for (slot in 0..35) {
            yamlConfiguration["inventory.slot.$slot.itemStack"] = SerializeItemStack(player.inventory.getItem(slot))
        }
        yamlConfiguration["inventory.slot.helmet.itemStack"] = SerializeItemStack(player.inventory.helmet)
         yamlConfiguration["inventory.slot.chestplate.itemStack"] = SerializeItemStack(player.inventory.chestplate)
         yamlConfiguration["inventory.slot.leggings.itemStack"] = SerializeItemStack(player.inventory.leggings)
         yamlConfiguration["inventory.slot.boots.itemStack"] = SerializeItemStack(player.inventory.boots)

        try {
            yamlConfiguration.save(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun addSpectator(player: Player, notifyPlayers: Boolean){
        //TODO Data.duelOfSpec[player] = this
        spectators.add(player)

        if (notifyPlayers) {
            sendMsg("duel.playerSpectating", mutableMapOf("playerName" to player.name))
        }

        val spawnOne = arena.spawn1Loc
        val spawnTwo = arena.spawn2Loc
        val x = (spawnOne.x + spawnTwo.x) / 2
        val y = spawnOne.y + 2
        val z = (spawnOne.z + spawnTwo.z) / 2
        val centerLoc = Location(Bukkit.getWorld("FightWorld"), x, y, z)

        for (players in allPlayers()) {
            players.hidePlayer(Manager.INSTANCE, player)
        }

        player.health = 20.0
        player.teleport(centerLoc)
        player.inventory.clear()
        player.allowFlight = true
        player.isFlying = true
        player.inventory.setItem(8, itemStack(Material.MAGENTA_GLAZED_TERRACOTTA) {
            meta {
                name = Localization.getMessage("duel.item.stopSpectating", player)
            }; mark("stopspec")
        })
    }

    fun removeSpectator(player: Player, notifyPlayers: Boolean) {
        player.reset()
        spectators.remove(player)
        allPlayers().remove(player)
        if (notifyPlayers)
            sendMsg("duel.playerStoppedSpectating", mutableMapOf("playerName" to player.name))
    }

    fun stop() {
        gameState = GameState.ENDED
        countdownTask?.cancel()
        competitors.filterIsInstance<Player>().forEach { savePlayerdata(it); Kits.removeCooldown(it) }
        results()

        taskRunLater(45, true) {
            allPlayers().iterator().forEachRemaining {
                if (StaffData.followingStaffFromPlayer.contains(it)) {
                    if (StaffData.followingStaffFromPlayer[it]!!.isNotEmpty()) {
                        StaffData.followingStaffFromPlayer[it]!!.iterator().forEachRemaining { staff ->
                            staff.teleport(SpawnUtils.getSpawn())
                        }
                    }
                }
                it.reset()
                Data.inFight.remove(it)
                Data.duelOfSpec.remove(it)
            }
            arena.removeSchematic()
            // TODO
            /*if (ifTournament) {
                tournament?.duelEnded(this)
                if (tournament!!.duels.size > 0) {
                    specs.forEach { tournament!!.duels.random().addSpectator(it, true) }
                    alivePlayers.forEach { tournament!!.duels.random().addSpectator(it, true) }
                }
            }*/
        }
    }

    open fun results() {
        sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                        ")
        if (teamOne.winner) {
            sendMsg("duel.result.winner.teamOne",
                mutableMapOf("teamColor" to "${teamOne.teamColor.mainColor}", "teamPlayers" to teamOne.playerListString()))
            sendMsg("duel.result.loser.teamTwo",
                mutableMapOf("teamColor" to "${teamTwo.teamColor.mainColor}", "teamPlayers" to teamTwo.playerListString()))
        } else {
            sendMsg("duel.result.winner.teamTwo",
                mutableMapOf("teamColor" to "${teamTwo.teamColor.mainColor}", "teamPlayers" to teamTwo.playerListString()))
            sendMsg("duel.result.loser.teamOne",
                mutableMapOf("teamColor" to "${teamOne.teamColor.mainColor}", "teamPlayers" to teamOne.playerListString()))
        }

        val message = TextComponent("Click to open the duel overview")
        message.color = KColors.GRAY
        message.isItalic = true
        message.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dueloverview $gameID")

        allPlayers().forEach { it.sendMessage(message) }
        sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                        ")
    }

    private fun getDuelKnockback(): DuelKnockback {
        if (kit == Anchor) {
            return DuelKnockback.ANCHOR
        }
        var oldKB = 0
        var newKB = 0

        competitors.filterIsInstance<Player>().forEach {
            val duelsPlayer = DuelsPlayer.get(it)
            if (duelsPlayer.settings.knockback() == PlayerSettings.Companion.Knockback.OLD) oldKB++
            else newKB++
        }

        val oldPercentage = 100.0 / competitors.filterIsInstance<Player>().size * oldKB

        return if (oldPercentage > 66) DuelKnockback.OLD
        else DuelKnockback.NEW
    }

    fun allPlayers(): MutableList<Player> {
        val players = mutableListOf<Player>()
        players.addAll(competitors.filterIsInstance<Player>())
        players.addAll(spectators)
        return players
    }

    fun sendMessage(string: String) {
        allPlayers().forEach { player ->
            player.sendMessage(string)
        }
    }

    fun sendMsg(key: String, values: MutableMap<String, String>? = null) {
        allPlayers().forEach { player ->
            player.sendMsg(key, values)
        }
    }
}