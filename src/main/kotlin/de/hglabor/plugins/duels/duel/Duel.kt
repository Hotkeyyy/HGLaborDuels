package de.hglabor.plugins.duels.duel

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.arenas.Arena
import de.hglabor.plugins.duels.arenas.Arenas
import de.hglabor.plugins.duels.data.PlayerSettings
import de.hglabor.plugins.duels.data.PlayerStats
import de.hglabor.plugins.duels.guis.QueueGUI
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
import net.axay.kspigot.runnables.*
import net.axay.kspigot.utils.mark
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.io.File
import java.io.IOException


class Duel {
    companion object {
        fun create(teamOneLeader: Player, teamTwoLeader: Player, kit: Kits) {
            val duel = Duel()
            if (kit == Kits.RANDOM)
                duel.kit = Kits.random()
            else
                duel.kit = kit
            if (teamOneLeader.isInParty()) duel.teamOne = Party.get(teamOneLeader)!!.players
            else duel.teamOne = arrayListOf(teamOneLeader)

            if (teamTwoLeader.isInParty()) duel.teamTwo = Party.get(teamTwoLeader)!!.players
            else duel.teamTwo = arrayListOf(teamTwoLeader)
            duel.start()
        }

        fun create(teamOne: ArrayList<Player>, teamTwo: ArrayList<Player>, kit: Kits) {
            val duel = Duel()
            if (kit == Kits.RANDOM)
                duel.kit = Kits.random()
            else
                duel.kit = kit
            duel.teamOne.addAll(teamOne)
            duel.teamTwo.addAll(teamTwo)
            duel.start()
        }

        fun createTournament(
            teamOne: ArrayList<Player>,
            teamTwo: ArrayList<Player>,
            kit: Kits,
            tournament: Tournament
        ) {
            val duel = Duel()
            if (kit == Kits.RANDOM)
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

    lateinit var kit: Kits
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
        arena = Arena(loc, Arenas.getRandomArena(kit.info.arenaTag))
        alivePlayers.addAll(teamOne)
        alivePlayers.addAll(teamTwo)
        knockbackType = getKnockbackForDuel()
        alivePlayers.forEach {
            it.closeInventory()
            hits[it] = 0; currentCombo[it] = 0; longestCombo[it] = 0
            presoups[it] = 0; missedPots[it] = 0; wastedHealth[it] = 0
            playersAndSpecs.add(it)
            totalPlayers.add(it)
            Data.challengeKit.remove(it)
            Data.challenged.remove(it)
            Data.duelIDFromPlayer[it] = ID
            Data.inFight.add(it)
            Soupsimulator.get(it)?.stop()
            it.isGlowing = false
            it.inventory.clear()
            Kits.inGame[kit]?.add(it)
            Kits.playerQueue.remove(it)
            Kits.queue[kit]!!.remove(it)
            Kits.queue[Kits.RANDOM]?.remove(it)
            PlayerStats.get(it).addTotalGame()

            if (knockbackType == PlayerSettings.Companion.Knockback.OLD)
                it.setMetadata("oldKnockback", FixedMetadataValue(Manager.INSTANCE, ""))
            else
                it.removeMetadata("oldKnockback", Manager.INSTANCE)
        }
        alivePlayers.filter { it.isInSoupsimulator() }.forEach { Soupsimulator.forceStop(it) }
        Data.duelFromID[ID] = this
        QueueGUI.updateContents()
    }

    private fun getKnockbackForDuel(): PlayerSettings.Companion.Knockback {
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
                StaffData.followedPlayerFromStaff
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

    fun sendCountdown() {
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
        }!!
    }

    fun teleportPlayersToSpawns() {
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

    fun playerDied(player: Player, germanMessage: String, englishMessage: String) {
        Data.duelFromSpec[player] = this
        Data.inFight.remove(player)
        alivePlayers.remove(player)
        val newAlivePlayers = alivePlayers
        alivePlayers = newAlivePlayers
        savePlayerdata(player)
        Kits.inGame[kit]?.remove(player)
        Kits.removeCooldown(player)

        sendMessage(germanMessage, englishMessage)

        if (ifTeamDied(getTeam(player))) {
            loser = getTeam(player)
            winner = getOtherTeam(player)
            stop()
        } /*else {
            val items = arrayListOf<ItemStack>()
            items.addAll(player.inventory.contents)
            items.addAll(player.inventory.armorContents)
            items.forEach { item ->
                if (item.type != Material.AIR) player.world.dropItem(player.location, item)
            }
        }*/

        val stats = PlayerStats.get(player)
        stats.addDeath()
    }

    fun playerLeft(player: Player) {
        Data.inFight.remove(player)
        alivePlayers.remove(player)
        Kits.inGame[kit]?.remove(player)
        Kits.removeCooldown(player)
        savePlayerdata(player)
        sendMessage(
            "${teamColor(player)}${player.name} ${KColors.GRAY}hat den Kampf verlassen.",
            "${teamColor(player)}${player.name} ${KColors.GRAY}left the fight."
        )


        if (ifTeamDied(getTeam(player))) {
            loser = getTeam(player)
            winner = getOtherTeam(player)
            stop()
        } /*else {
            val items = arrayListOf<ItemStack>()
            items.addAll(player.inventory.contents)
            items.addAll(player.inventory.armorContents)
            items.forEach { item ->
                if (item.type != Material.AIR) player.world.dropItem(player.location, item)
            }
        }*/
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
        Data.duelFromSpec[player] = this
        val newPlayersAndSpecs = playersAndSpecs
        newPlayersAndSpecs.remove(player)
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

        player.health = 20.0
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

    fun removeSpectator(player: Player, notifyPlayers: Boolean) {
        player.reset()
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
        countdownTask?.cancel()
        alivePlayers.forEach { savePlayerdata(it); Kits.inGame[kit]?.remove(it); Kits.removeCooldown(it) }
        sendResults()
        QueueGUI.updateContents()

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
            sendMessage("${KColors.GREEN}Winner: ${KColors.DEEPSKYBLUE}Team One §8($teamOnePlayers§8)")
            sendMessage("${KColors.RED}Loser: ${KColors.DEEPPINK}Team Two §8($teamTwoPlayers§8)")
        } else {
            sendMessage("${KColors.GREEN}Winner: ${KColors.DEEPPINK}Team Two §8($teamTwoPlayers§8)")
            sendMessage("${KColors.RED}Loser: ${KColors.DEEPSKYBLUE}Team One §8($teamOnePlayers§8)")
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
}
