package de.hglabor.plugins.duels.duel

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.arenas.Arena
import de.hglabor.plugins.duels.arenas.Arenas
import de.hglabor.plugins.duels.kits.KitType
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.Kits.Companion.giveKit
import de.hglabor.plugins.duels.kits.Kits.Companion.info
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.spawn.SpawnUtils
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.getStats
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.reset
import de.hglabor.plugins.duels.utils.PlayerFunctions.stopSpectating
import de.hglabor.plugins.staff.Staffmode.teleportToFollowedPlayer
import de.hglabor.plugins.staff.utils.StaffData
import net.axay.kspigot.chat.KColors
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.block.Block
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.io.File
import java.io.IOException


class Duel(val p1: Player, val p2: Player, val kit: Kits, private val ID: String) {
    var hasEnded = false
    var hasBegan = false

    val players = arrayListOf(p1, p2)
    var specs = mutableListOf<Player>()

    var involvedInDuel = arrayListOf(p1, p2)

    var speccount = 0
    val loc = Data.getFreeLocation()
    val arena = Arena(loc, Arenas.getRandomArena(kit.info.arenaTag))

    lateinit var winner: Player
    lateinit var loser: Player

    val blocksPlacedDuringGame = arrayListOf<Block>()

    val hits = hashMapOf(p1 to 0, p2 to 0)
    val currentCombo = hashMapOf(p1 to 0, p2 to 0)
    val longestCombo = hashMapOf(p1 to 0, p2 to 0)
    val presoups = hashMapOf(p1 to 0, p2 to 0)
    val missedPots = hashMapOf(p1 to 0, p2 to 0)
    val wastedHealth = hashMapOf(p1 to 0, p2 to 0)
    val path = File("plugins//HGLaborDuels//temp//duels//$ID//")

    fun start() {
        setPlayersInLists()

        p1.inventory.clear()
        p2.inventory.clear()
        p1.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 40, 200))
        p2.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 40, 200))

        arena.pasteSchematic()

        object : BukkitRunnable() {
            override fun run() {
                players.forEach {
                    setPlayersInLists()
                    it.getStats().addGame()
                    it.isGlowing = false
                    it.inventory.clear()
                    it.gameMode = GameMode.SURVIVAL
                }
                giveKit()
                teleportPlayersToSpawns()
                countdown()

                object : BukkitRunnable() {
                    override fun run() {
                        Data.frozenBecauseCountdown.remove(p1)
                        Data.frozenBecauseCountdown.remove(p2)

                    }
                }.runTaskLater(Manager.INSTANCE, 60)

                if (StaffData.followingStaffFromPlayer[p1]?.isNotEmpty() == true)
                    StaffData.followingStaffFromPlayer[p1]!!.forEach {
                        it.teleportToFollowedPlayer()
                    }
                if (p1.localization("de"))
                    p1.spigot()
                        .sendMessage(ChatMessageType.ACTION_BAR, TextComponent(Localization.LEAVE_COMMAND_TO_LEAVE_DE))
                else
                    p1.spigot()
                        .sendMessage(ChatMessageType.ACTION_BAR, TextComponent(Localization.LEAVE_COMMAND_TO_LEAVE_EN))

                if (StaffData.followingStaffFromPlayer[p2]?.isNotEmpty() == true)
                    StaffData.followingStaffFromPlayer[p2]!!.forEach {
                        it.teleportToFollowedPlayer()
                    }
                if (p2.localization("de"))
                    p2.spigot()
                        .sendMessage(ChatMessageType.ACTION_BAR, TextComponent(Localization.LEAVE_COMMAND_TO_LEAVE_DE))
                else
                    p2.spigot()
                        .sendMessage(ChatMessageType.ACTION_BAR, TextComponent(Localization.LEAVE_COMMAND_TO_LEAVE_EN))

            }
        }.runTaskLater(Manager.INSTANCE, 30)
    }

    fun countdown() {
        p1.sendTitle("${KColors.GREEN}3", "§c", 3, 13, 3)
        p1.playSound(p1.location, Sound.BLOCK_NOTE_BLOCK_BASS, 3f, 5f)
        p2.sendTitle("${KColors.GREEN}3", "§c", 3, 13, 3)
        p2.playSound(p2.location, Sound.BLOCK_NOTE_BLOCK_BASS, 3f, 5f)
        object : BukkitRunnable() {
            override fun run() {
                p1.sendTitle("${KColors.YELLOW}2", "§c", 3, 13, 3)
                p1.playSound(p1.location, Sound.BLOCK_NOTE_BLOCK_BASS, 3f, 3f)
                p2.sendTitle("${KColors.YELLOW}2", "§c", 3, 13, 3)
                p2.playSound(p2.location, Sound.BLOCK_NOTE_BLOCK_BASS, 3f, 3f)
            }
        }.runTaskLater(Manager.INSTANCE, 20)

        object : BukkitRunnable() {
            override fun run() {
                p1.sendTitle("${KColors.RED}1", "§c", 3, 13, 3)
                p1.playSound(p1.location, Sound.BLOCK_NOTE_BLOCK_BASS, 3f, 2f)
                p2.sendTitle("${KColors.RED}1", "§c", 3, 13, 3)
                p2.playSound(p2.location, Sound.BLOCK_NOTE_BLOCK_BASS, 3f, 2f)

            }
        }.runTaskLater(Manager.INSTANCE, 40)

        object : BukkitRunnable() {
            override fun run() {

                if (p1.localization("de"))
                    p1.sendTitle(Localization.DUEL_STARTING_TITLE_DE, "§b", 3, 13, 3)
                else
                    p1.sendTitle(Localization.DUEL_STARTING_TITLE_EN, "§b", 3, 13, 3)
                p1.closeInventory()
                p1.playSound(p1.location, Sound.EVENT_RAID_HORN, 4f, 1f)

                if (p2.localization("de"))
                    p2.sendTitle(Localization.DUEL_STARTING_TITLE_DE, "§b", 3, 13, 3)
                else
                    p2.sendTitle(Localization.DUEL_STARTING_TITLE_EN, "§b", 3, 13, 3)
                p2.closeInventory()
                p2.playSound(p2.location, Sound.EVENT_RAID_HORN, 4f, 1f)

                hasBegan = true

            }
        }.runTaskLater(Manager.INSTANCE, 60)
    }

    private fun teleportPlayersToSpawns() {
        p1.teleport(arena.spawn1Loc)
        direction(p1, arena.spawn2Loc)

        p2.teleport(arena.spawn2Loc)
        direction(p2, arena.spawn1Loc)

        p1.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 20 * 3, 200))
        p1.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 20 * 3, 200))
        p2.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 20 * 3, 200))
        p2.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 20 * 3, 200))
    }

    fun giveKit() {
        p1.giveKit(kit)
        p2.giveKit(kit)
    }

    fun stop() {
        hasEnded = true
        savePlayerdata(p1)
        savePlayerdata(p2)
        sendGameEndMessage()

        loser.health = loser.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue!!
        loser.inventory.clear()
        loser.velocity = Vector(0, 10, 0)

        object : BukkitRunnable() {
            override fun run() {
                getSpectators().forEach {
                    it.stopSpectating(true)
                }
                p1.reset()
                p2.reset()
                if (StaffData.followingStaffFromPlayer[p1]?.isNotEmpty() == true)
                    StaffData.followingStaffFromPlayer[p1]!!.forEach {
                        it.teleport(SpawnUtils.getSpawn())
                    }
                if (StaffData.followingStaffFromPlayer[p2]?.isNotEmpty() == true)
                    StaffData.followingStaffFromPlayer[p2]!!.forEach {
                        it.teleport(SpawnUtils.getSpawn())
                    }
                arena.removeSchematic()
            }
        }.runTaskLater(Manager.INSTANCE, 45)
        players.remove(p1)
        players.remove(p2)
        Data.inFight.remove(p1)
        Data.inFight.remove(p2)
    }

    private fun setPlayersInLists() {
        Data.inFight.add(p1)
        Data.inFight.add(p2)
        Data.frozenBecauseCountdown.add(p1)
        Data.frozenBecauseCountdown.add(p2)
        Data.gameIDs.add(ID)
        Data.duel[p1] = p2
        Data.duel[p2] = p1

        Data.challengeKit.remove(p1)
        Data.challengeKit.remove(p2)
        Data.challenged.remove(p1)
        Data.challenged.remove(p2)
        Data.duelFromID[ID] = this
        Data.duelIDFromPlayer[p1] = ID
        Data.duelIDFromPlayer[p2] = ID
    }

    private fun direction(player: Player, loc: Location) {
        val dir = loc.clone().subtract(player.eyeLocation).toVector()
        val finalLoc = player.location.setDirection(dir)
        finalLoc.pitch = 0f
        player.teleport(finalLoc)
    }

    fun sendMessage(germanMessage: String, englishMessage: String) {
        involvedInDuel.forEach {
            if (it.localization("de"))
                it.sendMessage(germanMessage)
            else
                it.sendMessage(englishMessage)
        }
    }

    fun sendMessage(universalMessage: String) {
        involvedInDuel.forEach {
            it.sendMessage(universalMessage)
        }
    }

    fun getOtherPlayer(player: Player): Player {
        if (player == p1)
            return p2
        return p1
    }

    fun addSpectator(player: Player) {
        val newInvolvedInDuel = involvedInDuel
        newInvolvedInDuel.add(player)
        involvedInDuel = newInvolvedInDuel

        val newSpecList = specs
        newSpecList.add(player)
        specs = newSpecList
        Data.duelFromID[ID] = this
        speccount++
    }

    private fun getSpectators(): ArrayList<Player> {
        val spectators = arrayListOf<Player>()
        val iter: MutableIterator<Player> = specs.iterator()
        while (iter.hasNext()) {
            val player = iter.next()
            spectators.add(player)
        }
        return spectators
    }

    private fun savePlayerdata(player: Player) {
        val file = File("$path//playerdata//${player.uniqueId}.yml")
        if (!file.parentFile.exists())
            file.parentFile.mkdirs()
        file.createNewFile()

        val yamlConfiguration = YamlConfiguration.loadConfiguration(file)


        yamlConfiguration["data.playername"] = player.name
        if (player == winner)
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

        object : BukkitRunnable() {
            override fun run() {
                File("$path//playerdata//${player.uniqueId}.yml").delete()
            }
        }.runTaskLaterAsynchronously(Manager.INSTANCE, 7*20*60)
    }

    private fun sendGameEndMessage() {
        winner.getStats().addKill()
        loser.getStats().addDeath()

        sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                         ")

        involvedInDuel.forEach {
            val winnerComponent = if (it.localization("de"))
                TextComponent(Localization.DUEL_WINNER_DE)
            else
                TextComponent(Localization.DUEL_WINNER_EN)
            winnerComponent.color = KColors.DARKGRAY
            val winnername = TextComponent(winner.name)
            winnername.color = KColors.DODGERBLUE
            winnername.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dueloverview $ID ${winner.name}")
            winnerComponent.addExtra(winnername)
            it.spigot().sendMessage(winnerComponent)

            val loserComponent = if (it.localization("de"))
                TextComponent(Localization.DUEL_LOSER_DE)
            else
                TextComponent(Localization.DUEL_LOSER_EN)
            loserComponent.color = KColors.DARKGRAY
            val losername = TextComponent(loser.name)
            losername.color = KColors.MEDIUMPURPLE
            losername.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dueloverview $ID ${loser.name}")
            loserComponent.addExtra(losername)
            it.spigot().sendMessage(loserComponent)
        }
        sendMessage(Localization.CLICK_NAME_TO_OPEN_INV_DE, Localization.CLICK_NAME_TO_OPEN_INV_EN)
        sendMessage("${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                         ")
    }
}
