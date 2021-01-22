package de.hglabor.plugins.duels.duel

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.arenas.Arena
import de.hglabor.plugins.duels.arenas.Arenas
import de.hglabor.plugins.duels.data.PlayerStats
import de.hglabor.plugins.duels.kits.KitType
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.Kits.Companion.giveKit
import de.hglabor.plugins.duels.kits.Kits.Companion.info
import de.hglabor.plugins.duels.localization.Localization
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
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.runnables.async
import net.axay.kspigot.runnables.task
import net.axay.kspigot.runnables.taskRunLater
import net.axay.kspigot.utils.mark
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.io.File
import java.io.IOException


class Duel {
    companion object {
        fun create(teamOneLeader: Player, teamTwoLeader: Player, kit: Kits) {
            val duel = Duel()
            duel.kit = kit
            if (teamOneLeader.isInParty()) duel.teamOne = Party.get(teamOneLeader)!!.players
            else duel.teamOne = arrayListOf(teamOneLeader)
            
            if (teamTwoLeader.isInParty()) duel.teamTwo = Party.get(teamTwoLeader)!!.players
            else duel.teamTwo = arrayListOf(teamTwoLeader)
            duel.kit = kit
            duel.start()
        }

        fun create(teamOne: ArrayList<Player>, teamTwo: ArrayList<Player>, kit: Kits) {
            val duel = Duel()
            duel.kit = kit
            duel.teamOne = teamOne
            duel.teamTwo = teamTwo
            duel.start()
        }

        fun createTournament(teamOne: ArrayList<Player>, teamTwo: ArrayList<Player>, kit: Kits, tournament: Tournament) {
            val duel = Duel()
            duel.kit = kit
            duel.teamOne = teamOne
            duel.teamTwo = teamTwo
            duel.ifTournament = true
            duel.tournament = tournament
            duel.start()
        }
    }

    lateinit var kit: Kits
    val ID = Data.getFreeGameID()
    var state = GameState.STARTING

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
        arena = Arena(loc, Arenas.getRandomArena(kit.info.arenaTag))
        async {
            alivePlayers.addAll(teamOne)
            alivePlayers.addAll(teamTwo)
            alivePlayers.forEach {
                hits[it] = 0; currentCombo[it] = 0; longestCombo[it] = 0
                presoups[it] = 0; missedPots[it] = 0; wastedHealth[it] = 0
                playersAndSpecs.add(it)
                totalPlayers.add(it)
                Data.inFight.add(it)
                Data.challengeKit.remove(it)
                Data.challenged.remove(it)
                Data.duelIDFromPlayer[it] = ID
                Soupsimulator.get(it)?.stop()
            }
            alivePlayers.filter { it.isInSoupsimulator() }.forEach { Soupsimulator.forceStop(it) }
            Data.duelFromID[ID] = this
        }
    }

    fun start() {
        init()
        Data.gameIDs.add(ID)
        alivePlayers.forEach { player ->
            player.inventory.clear()
            player.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 40, 200))

            async { val stats = PlayerStats.get(player); stats.addTotalGame() }
        }

        arena.pasteSchematic()

        taskRunLater(40L, true) {
            alivePlayers.forEach {
                it.isGlowing = false
                it.inventory.clear()
                it.gameMode = GameMode.SURVIVAL
                it.giveKit(kit)
                teleportPlayersToSpawns()
                sendCountdown()
            }
        }
    }

    fun sendCountdown() {
        state = GameState.COUNTDOWN
        var count = 3
        var colorcode = 'a'
        task(true, 20, period = 20, howOften = 4) {
            if (count == 2) colorcode = 'e'
            if (count == 1) colorcode = 'c'
            if (count != 0) {
                playersAndSpecs.forEach { player ->
                    player.sendTitle("§$colorcode$count", "§$colorcode", 3, 13, 3)
                    player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 3f, 2f)
                }
            } else {
                playersAndSpecs.forEach { player ->
                    if (player.localization("de"))
                        player.sendTitle(Localization.DUEL_STARTING_TITLE_DE, "§b", 3, 13, 3)
                    else
                        player.sendTitle(Localization.DUEL_STARTING_TITLE_EN, "§b", 3, 13, 3)
                    player.playSound(player.location, Sound.EVENT_RAID_HORN, 10f, 1f)
                    player.closeInventory()
                    player.removePotionEffect(PotionEffectType.SLOW)
                    player.removePotionEffect(PotionEffectType.JUMP)
                }
                state = GameState.RUNNING
            }
            count--
        }
    }

    fun teleportPlayersToSpawns() {
        alivePlayers.forEach { player ->
            if (teamOne.contains(player)) {
                player.teleport(arena.spawn1Loc)
                direction(player, arena.spawn2Loc)
            } else {
                player.teleport(arena.spawn2Loc)
                direction(player, arena.spawn1Loc)
            }
            player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, Int.MAX_VALUE, 200))
            player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, Int.MAX_VALUE, 200))
        }
    }

    fun direction(player: Player, loc: Location) {
        val dir = loc.clone().subtract(player.eyeLocation).toVector()
        val finalLoc = player.location.setDirection(dir)
        finalLoc.pitch = 0f
        player.teleport(finalLoc)
    }

    fun playerDied(player: Player) {
        alivePlayers.remove(player)
        val newAlivePlayers = alivePlayers
        alivePlayers = newAlivePlayers
        savePlayerdata(player)
        if (lastAttackerOfPlayer[player] != null) {
            sendMessage("${KColors.DEEPSKYBLUE}${player.name} §rwrude von ${KColors.DEEPSKYBLUE}${lastAttackerOfPlayer[player]!!.name} §rtot")
            val killerStats = PlayerStats.get(lastAttackerOfPlayer[player]!!)
            killerStats.addKill()
        } else
            sendMessage("${KColors.DEEPSKYBLUE}${player.name} §rtot")

        if (ifTeamDied(getTeam(player))) {
            loser = getTeam(player)
            winner = getOtherTeam(player)
            stop()
        }
        addSpectator(player, false)
        val stats = PlayerStats.get(player)
        stats.addDeath()
    }

    fun playerLeft(player: Player) {
        alivePlayers.remove(player)
        savePlayerdata(player)
        sendMessage("${KColors.DEEPSKYBLUE}${player.name} §rverlassen")

        if (ifTeamDied(getTeam(player))) {
            loser = getTeam(player)
            winner = getOtherTeam(player)
            stop()
        }
        player.reset()
    }

    fun ifTeamDied(team: ArrayList<Player>): Boolean {
        var livingPlayers = 0
        team.filter { alivePlayers.contains(it) }.forEach { _ -> livingPlayers++ }
        return livingPlayers == 0
    }

    private fun savePlayerdata(player: Player) {
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

        if (kit.info.type == KitType.SOUP) {
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

        if (kit.info.type == KitType.POT) {
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
        val newPlayersAndSpecs = playersAndSpecs
        newPlayersAndSpecs.add(player)
        playersAndSpecs = newPlayersAndSpecs

        val newSpecList = specs
        newSpecList.add(player)
        specs = newSpecList

        if (notifyPlayers)
            sendMessage(
                Localization.PLAYER_STARTED_SPECTATING_DE.replace("%playerName%", player.displayName),
                Localization.PLAYER_STARTED_SPECTATING_EN.replace("%playerName%", player.displayName)
            )
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

        player.teleport(centerLoc)
        player.inventory.clear()
        player.allowFlight = true
        player.isFlying = true
        player.inventory.setItem(8, itemStack(Material.MAGENTA_GLAZED_TERRACOTTA) {
            meta {
                name = if (player.localization("de"))
                    Localization.STOP_SPECTATING_ITEM_NAME_DE
                else
                    Localization.STOP_SPECTATING_ITEM_NAME_EN
            }; mark("stopspec")
        })
        Data.duelFromSpec[player] = this
    }

    fun removeSpectator(player: Player, notifyPlayers: Boolean, removeFromDuel: Boolean) {
        player.reset()
        if (removeFromDuel)
            specs.remove(player)
        playersAndSpecs.remove(player)
        if (notifyPlayers)
            if (player.localization("de"))
                sendMessage(
                    Localization.PLAYER_STOPPED_SPECTATING_DE.replace("%playerName%", player.displayName),
                    Localization.PLAYER_STOPPED_SPECTATING_EN.replace("%playerName%", player.displayName)
                )
    }

    fun stop() {
        state = GameState.ENDED
        alivePlayers.forEach { savePlayerdata(it) }
        sendResults()

        taskRunLater(45, true) {
            resetAll()
        }

        if (ifTournament) {
            tournament?.duelEnded(this)
        }
    }

    private fun sendResults() {
        sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                        ")
        if (winner == teamOne) {
            sendMessage("${KColors.SPRINGGREEN}Winner: ${KColors.DEEPSKYBLUE}Team One")
            sendMessage("${KColors.TOMATO}Loser: ${KColors.DEEPPINK}Team Two")
        } else {
            sendMessage("${KColors.SPRINGGREEN}Winner: ${KColors.DEEPPINK}Team Two")
            sendMessage("${KColors.TOMATO}Loser: ${KColors.DEEPSKYBLUE}Team One")
        }
        sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                        ")
    }

    private fun resetAll() {
        specs.forEach { removeSpectator(it, false, false) }

        alivePlayers.forEach {
            savePlayerdata(it)
            if (StaffData.followingStaffFromPlayer.contains(it)) {
                if (StaffData.followingStaffFromPlayer[it]!!.isNotEmpty()) {
                    StaffData.followingStaffFromPlayer[it]!!.forEach { staff ->
                        staff.teleport(SpawnUtils.getSpawn())
                    }
                }
            }
            it.reset()
            Data.inFight.remove(it)
        }
    }

    fun sendMessage(germanMessage: String, englishMessage: String) {
        playersAndSpecs.forEach {
            if (it.localization("de"))
                it.sendMessage(germanMessage)
            else
                it.sendMessage(englishMessage)
        }
    }

    fun sendMessage(universalMessage: String) {
        playersAndSpecs.forEach {
            it.sendMessage(universalMessage)
        }
    }

    private fun getTeam(player: Player): ArrayList<Player> {
        return if (teamOne.contains(player))
            teamOne
        else
            teamTwo
    }

    private fun getOtherTeam(player: Player): ArrayList<Player> {
        return if (teamOne.contains(player))
            teamTwo
        else
            teamOne
    }
}
