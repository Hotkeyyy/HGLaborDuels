package de.hglabor.plugins.duels.duel

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.arenas.Arena
import de.hglabor.plugins.duels.arenas.Arenas
import de.hglabor.plugins.duels.data.PlayerSettings
import de.hglabor.plugins.duels.data.PlayerStats
import de.hglabor.plugins.duels.events.events.duel.DuelStartEvent
import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.kits.KitType
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.Kits.giveKit
import de.hglabor.plugins.duels.kits.kit.Random
import de.hglabor.plugins.duels.kits.kit.soup.Anchor
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.localization.sendMsg
import de.hglabor.plugins.duels.party.Party
import de.hglabor.plugins.duels.party.Partys.isInParty
import de.hglabor.plugins.duels.soupsimulator.Soupsim.isInSoupsimulator
import de.hglabor.plugins.duels.soupsimulator.Soupsimulator
import de.hglabor.plugins.duels.spawn.SpawnUtils
import de.hglabor.plugins.duels.tournament.Tournament
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.reset
import de.hglabor.plugins.staff.utils.StaffData
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.chat.sendMessage
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.runnables.*
import net.axay.kspigot.utils.mark
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.apache.commons.lang.mutable.Mutable
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class Duel {
    companion object {
        fun get(duelID: String): Duel? {
            return Data.duelFromID[duelID]
        }

        fun create(teamOneLeader: Player, teamTwoLeader: Player, kit: AbstractKit) {
            val duel = Duel()
            if (kit == Random.INSTANCE)
                duel.kit = Kits.random()
            else
                duel.kit = kit
            if (teamOneLeader.isInParty())
                duel.teamOne = ArrayList(Party.get(teamOneLeader)?.players)
            else
                duel.teamOne = arrayListOf(teamOneLeader)

            if (teamTwoLeader.isInParty())
                duel.teamTwo = ArrayList(Party.get(teamTwoLeader)?.players)
            else
                duel.teamTwo = arrayListOf(teamTwoLeader)
            duel.start()
        }

        fun create(teamOne: ArrayList<Player>, teamTwo: ArrayList<Player>, kit: AbstractKit) {
            val duel = Duel()
            if (kit == Random.INSTANCE)
                duel.kit = Kits.random()
            else
                duel.kit = kit
            duel.teamOne = ArrayList(teamOne)
            duel.teamTwo = ArrayList(teamTwo)
            duel.start()
        }

        fun createTournament(
            teamOne: ArrayList<Player>,
            teamTwo: ArrayList<Player>,
            kit: AbstractKit,
            tournament: Tournament
        ) {
            val duel = Duel()
            if (kit == Random.INSTANCE)
                duel.kit = Kits.random()
            else
                duel.kit = kit
            duel.teamOne = teamOne
            duel.teamTwo = teamTwo
            duel.ifTournament = true
            duel.tournament = tournament
            duel.start()
        }
    }

    lateinit var kit: AbstractKit
    val ID = Data.getFreeGameID()
    var state = GameState.STARTING
    var countdownTask: KSpigotRunnable? = null
    var knockbackType: PlayerSettings.Companion.Knockback? = null

    var ifTournament = false
    var tournament: Tournament? = null

    var teamOne = arrayListOf<Player>()
    var teamTwo = arrayListOf<Player>()
    var winner = arrayListOf<Player>()
    var loser = arrayListOf<Player>()

    var playersAndSpecs = arrayListOf<Player>()
    var alivePlayers = arrayListOf<Player>()
    private val totalPlayers = arrayListOf<Player>()
    var specs = arrayListOf<Player>()

    private val loc = Data.getFreeLocation()
    lateinit var arena: Arena
    val blocksPlacedDuringGame = arrayListOf<Block>()

    val path = File("plugins//HGLaborDuels//temp//duels//$ID//")
    val hits = hashMapOf<Player, Int>()
    val currentCombo = hashMapOf<Player, Int>()
    val longestCombo = hashMapOf<Player, Int>()
    val presoups = hashMapOf<Player, Int>()
    val missedPots = hashMapOf<Player, Int>()
    val wastedHealth = hashMapOf<Player, Int>()
    val lastHitOfPlayer = hashMapOf<Player, Player>() // Person who hit , Person who was hit
    val lastAttackerOfPlayer = hashMapOf<Player, Player>() // Person who was hit , Person who hit

    private fun init() {
        async { Bukkit.getPluginManager().callEvent(DuelStartEvent(this)) }

        arena = Arena(loc, Arenas.getRandomArena(kit.arenaTag))
        alivePlayers.addAll(teamOne)
        alivePlayers.addAll(teamTwo)
        knockbackType = getKnockbackForDuel()
        alivePlayers.forEach {
            it.closeInventory()
            hits[it] = 0; currentCombo[it] = 0; longestCombo[it] = 0
            presoups[it] = 0; missedPots[it] = 0; wastedHealth[it] = 0
            playersAndSpecs.add(it)
            totalPlayers.add(it)
            Soupsimulator.get(it)?.stop()
            it.isGlowing = false
            it.inventory.clear()
            //TODO PlayerStats.get(it).addTotalGame()

            if (knockbackType == PlayerSettings.Companion.Knockback.OLD)
                it.setMetadata("oldKnockback", FixedMetadataValue(Manager.INSTANCE, ""))
            else
                it.removeMetadata("oldKnockback", Manager.INSTANCE)
        }
        alivePlayers.filter { it.isInSoupsimulator() }.forEach { Soupsimulator.forceStop(it) }
        Data.duelFromID[ID] = this
    }

    private fun getKnockbackForDuel(): PlayerSettings.Companion.Knockback {
        // TODO
        if (kit == Anchor.INSTANCE) {
            return PlayerSettings.Companion.Knockback.ANCHOR
        }
        return PlayerSettings.Companion.Knockback.OLD
        var oldKB = 0
        var newKB = 0
        alivePlayers.forEach {
            if (PlayerSettings.get(it).knockback() == PlayerSettings.Companion.Knockback.OLD) oldKB++
            else newKB++
        }

        val oldPercentage = 100.0 / alivePlayers.size * oldKB

        return if (oldPercentage > 66)
            PlayerSettings.Companion.Knockback.OLD
        else
            PlayerSettings.Companion.Knockback.NEW
    }

    fun start() {
        init()
        Data.gameIDs.add(ID)
        alivePlayers.forEach { player ->
            player.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 40, 200))
            async {
                StaffData.followingStaffFromPlayer[player]?.forEach {
                    this.addSpectator(it, false)
                }
            }
        }

        arena.pasteSchematic()

        taskRunLater(20L, true) {
            alivePlayers.forEach {
                it.gameMode = GameMode.SURVIVAL
                it.giveKit(kit)
                teleportPlayersToSpawns()
                sendCountdown()
                if (ifTournament)
                    tournament?.duels?.add(this)
            }
        }
    }

    private fun sendCountdown() {
        state = GameState.COUNTDOWN
        var count = 3
        var colorcode = 'a'
        countdownTask = task(true, 20, period = 20, howOften = 4) {
            if (count == 2) colorcode = 'e'
            if (count == 1) colorcode = 'c'
            if (count != 0) {
                playersAndSpecs.forEach { player ->
                    player.sendTitle("§$colorcode$count", "§$colorcode", 3, 13, 3)
                    player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 3f, 2f)
                }
            } else {
                playersAndSpecs.forEach { player ->
                    player.sendTitle(Localization.INSTANCE.getMessage("duel.starting.title", player), "§b", 3, 13, 3)
                    player.playSound(player.location, Sound.EVENT_RAID_HORN, 10f, 1f)
                    player.closeInventory()
                    player.removePotionEffect(PotionEffectType.SLOW)
                    player.removePotionEffect(PotionEffectType.JUMP)
                }
                state = GameState.RUNNING
            }
            count--
        }!!
    }

    private fun teleportPlayersToSpawns() {
        sync {
            alivePlayers.forEach { player ->
                if (teamOne.contains(player)) {
                    player.teleport(arena.spawn1Loc)
                    direction(player, arena.spawn2Loc)
                } else {
                    player.teleport(arena.spawn2Loc)
                    direction(player, arena.spawn1Loc)
                }

                player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, Int.MAX_VALUE, 200, false, false))
                player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, Int.MAX_VALUE, 200, false, false))
                //player.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, Int.MAX_VALUE, 200, false, false))
            }
        }
    }


    fun direction(player: Player, loc: Location) {
        task(true, 5) {
            val dir = loc.clone().subtract(player.eyeLocation).toVector()
            val finalLoc = player.location.setDirection(dir)
            finalLoc.pitch = 0f
            player.teleport(finalLoc)
        }
    }

    fun ifTeamDied(team: ArrayList<Player>): Boolean {
        var livingPlayers = 0
        team.filter { alivePlayers.contains(it) }.forEach { _ -> livingPlayers++ }
        return livingPlayers == 0
    }

    fun savePlayerdata(player: Player) {
        val file = File("$path//playerdata//${player.uniqueId}.yml")
        if (!file.parentFile.exists())
            file.parentFile.mkdirs()
        file.createNewFile()

        val yamlConfiguration = YamlConfiguration.loadConfiguration(file)


        yamlConfiguration["data.playername"] = player.name
        yamlConfiguration["data.team"] = if (teamOne.contains(player)) "ONE" else "TWO"
        if (!alivePlayers.contains(player))
            yamlConfiguration["data.health"] = player.health.toInt()
        else
            yamlConfiguration["data.health"] = 0
        yamlConfiguration["data.hits"] = hits[player]
        yamlConfiguration["data.longestCombo"] = longestCombo[player]

        if (kit.type == KitType.SOUP) {
            var soupsLeft = 0
            for (slot in 0..35) {
                if (player.inventory.getItem(slot) != null) {
                    if (player.inventory.getItem(slot)!!.type == Material.MUSHROOM_STEW)
                        soupsLeft++
                }
            }
            yamlConfiguration["data.presoups"] = presoups[player]
            yamlConfiguration["data.wastedHealth"] = wastedHealth[player]
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
            yamlConfiguration["data.missedPots"] = missedPots[player]
            yamlConfiguration["data.wastedHealth"] = wastedHealth[player]
            yamlConfiguration["data.potsLeft"] = potsLeft
        }

        for (slot in 0..35) {
            if (player.inventory.getItem(slot) != null)
                yamlConfiguration["inventory.slot.$slot.itemStack"] = player.inventory.getItem(slot)
        }

        if (player.inventory.helmet != null)
            yamlConfiguration["inventory.slot.helmet.itemStack"] = player.inventory.helmet

        if (player.inventory.chestplate != null)
            yamlConfiguration["inventory.slot.chestplate.itemStack"] = player.inventory.chestplate

        if (player.inventory.leggings != null)
            yamlConfiguration["inventory.slot.leggings.itemStack"] = player.inventory.leggings

        if (player.inventory.boots != null)
            yamlConfiguration["inventory.slot.boots.itemStack"] = player.inventory.boots

        try {
            yamlConfiguration.save(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun addSpectator(player: Player, notifyPlayers: Boolean) {
        Data.duelFromSpec[player] = this
        val newPlayersAndSpecs = playersAndSpecs
        newPlayersAndSpecs.remove(player)
        newPlayersAndSpecs.add(player)
        playersAndSpecs = newPlayersAndSpecs

        val newSpecList = specs
        newSpecList.add(player)
        specs = newSpecList

        if (notifyPlayers)
            sendMsg("duel.playerSpectating", mutableMapOf("playerName" to player.name))
        spectate(player)
    }

    fun spectate(player: Player) {
        val spawnOne = arena.spawn1Loc
        val spawnTwo = arena.spawn2Loc
        val x = (spawnOne.x + spawnTwo.x) / 2
        val y = spawnOne.y + 3
        val z = (spawnOne.z + spawnTwo.z) / 2
        val centerLoc = Location(Bukkit.getWorld("FightWorld"), x, y, z)

        for (players in playersAndSpecs) {
            players.hidePlayer(Manager.INSTANCE, player)
        }

        player.health = 20.0
        player.teleport(centerLoc)
        player.inventory.clear()
        player.allowFlight = true
        player.isFlying = true
        player.inventory.setItem(8, itemStack(Material.MAGENTA_GLAZED_TERRACOTTA) {
            meta {
                name = Localization.INSTANCE.getMessage("duel.item.stopSpectating", player)
            }; mark("stopspec")
        })
        Data.duelFromSpec[player] = this
    }

    fun removeSpectator(player: Player, notifyPlayers: Boolean) {
        player.reset()
        specs.remove(player)
        playersAndSpecs.remove(player)
        if (notifyPlayers)
            sendMsg("duel.playerStoppedSpectating", mutableMapOf("playerName" to player.name))
    }

    fun stop() {
        state = GameState.ENDED
        countdownTask?.cancel()
        alivePlayers.forEach { savePlayerdata(it); Kits.inGame[kit]?.remove(it); Kits.removeCooldown(it) }
        sendResults()
        //QueueGUI.updateContents()

        taskRunLater(45, true) {
            resetAll()
            if (ifTournament) {
                tournament?.duelEnded(this)
                if (tournament!!.duels.size > 0) {
                    specs.forEach { tournament!!.duels.random().addSpectator(it, true) }
                    alivePlayers.forEach { tournament!!.duels.random().addSpectator(it, true) }
                }
            }
        }
    }

    private fun sendResults() {
        var teamOnePlayers = ""
        for (players in teamOne) {
            teamOnePlayers += "${KColors.LIGHTSKYBLUE}${players.name}"
            if (teamOne.last() != players)
                teamOnePlayers += "§8, "
        }
        var teamTwoPlayers = ""
        for (players in teamTwo) {
            teamTwoPlayers += "${KColors.HOTPINK}${players.name}"
            if (teamTwo.last() != players)
                teamTwoPlayers += "§8, "
        }

        sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                        ")
        if (winner == teamOne) {
            sendMsg("duel.result.winner.teamOne", mutableMapOf("teamColor" to teamColor(teamOne.first()).toString(), "teamPlayers" to teamOnePlayers))
            sendMsg("duel.result.loser.teamTwo", mutableMapOf("teamColor" to teamColor(teamTwo.first()).toString(), "teamPlayers" to teamTwoPlayers))
        } else {
            sendMsg("duel.result.winner.teamTwo", mutableMapOf("teamColor" to teamColor(teamTwo.first()).toString(), "teamPlayers" to teamTwoPlayers))
            sendMsg("duel.result.loser.teamOne", mutableMapOf("teamColor" to teamColor(teamOne.first()).toString(), "teamPlayers" to teamOnePlayers))
        }

        val message = TextComponent("Click to open the duel overview")
        message.color = KColors.GRAY
        message.isItalic = true
        message.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dueloverview $ID")

        playersAndSpecs.forEach { it.sendMessage(message) }
        sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                        ")
    }

    private fun resetAll() {
        // specs.iterator().forEachRemaining { removeSpectator(it, false) }

        playersAndSpecs.iterator().forEachRemaining {
            if (StaffData.followingStaffFromPlayer.contains(it)) {
                if (StaffData.followingStaffFromPlayer[it]!!.isNotEmpty()) {
                    StaffData.followingStaffFromPlayer[it]!!.iterator().forEachRemaining { staff ->
                        staff.teleport(SpawnUtils.getSpawn())
                    }
                }
            }
            it.reset()
            Data.inFight.remove(it)
            Data.duelFromSpec.remove(it)
        }
        arena.removeSchematic()
    }

    fun getTeam(player: Player): ArrayList<Player> {
        return if (teamOne.contains(player))
            teamOne
        else
            teamTwo
    }

    fun teamColor(player: Player): ChatColor {
        return if (teamOne.contains(player))
            KColors.DEEPSKYBLUE
        else
            KColors.DEEPPINK
    }

    fun getOtherTeam(player: Player): ArrayList<Player> {
        return if (teamOne.contains(player))
            teamTwo
        else
            teamOne
    }

    fun sendMessage(string: String) {
        playersAndSpecs.forEach {
            it.sendMessage(string)
        }
    }

    fun sendMsg(key: String, values: MutableMap<String, String>? = null) {
        playersAndSpecs.forEach {
            it.sendMsg(key, values)
        }
    }
}
