package de.hglabor.plugins.duels

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.arenas.Arenas
import de.hglabor.plugins.duels.eventmanager.arena.CreateArenaListener
import de.hglabor.plugins.duels.commands.*
import de.hglabor.plugins.duels.duel.overview.DuelOverviewGUI
import de.hglabor.plugins.duels.eventmanager.*
import de.hglabor.plugins.duels.eventmanager.duel.*
import de.hglabor.plugins.duels.eventmanager.soupsimulator.SoupsimulatorEvents
import de.hglabor.plugins.duels.functionality.SoupHealing
import de.hglabor.plugins.duels.functionality.SoupRecipes
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.protection.Protection
import de.hglabor.plugins.duels.scoreboard.LobbyScoreboard
import de.hglabor.plugins.duels.settings.Settings
import de.hglabor.plugins.duels.spawn.SetSpawnCommand
import de.hglabor.plugins.duels.utils.CreateFiles
import de.hglabor.plugins.duels.utils.PlayerFunctions.reset
import de.hglabor.plugins.duels.utils.WorldManager
import de.hglabor.plugins.staff.commands.BanCommand
import de.hglabor.plugins.staff.commands.FollowCommand
import de.hglabor.plugins.staff.commands.StaffmodeCommand
import de.hglabor.plugins.staff.commands.TempBanCommand
import de.hglabor.plugins.staff.eventmanager.StaffOnInteract
import de.hglabor.plugins.staff.eventmanager.StaffOnInventoryClick
import de.hglabor.plugins.staff.eventmanager.StaffOnItemDrop
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.extensions.bukkit.info
import net.axay.kspigot.extensions.bukkit.success
import net.axay.kspigot.extensions.console
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.main.KSpigot
import net.axay.kspigot.sound.sound
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Sound
import java.io.File


class Manager : KSpigot() {

    companion object {
        lateinit var INSTANCE: Manager; private set
        //lateinit var mongoManager: MongoManager
    }

    override fun load() {
        INSTANCE = this

        console.info("Loading Duels plugin...")

        onlinePlayers.forEach {
            it.sound(Sound.BLOCK_BEACON_ACTIVATE)
            Settings.setPlayerSettings(it)
        }

        WorldManager.deleteFightWorld()
        val duelsPath = File("plugins//HGLaborDuels//temp//duels//")
        if(duelsPath.exists()) {
            duelsPath.deleteRecursively()
        }
        File("plugins//HGLaborDuels//temp//duels//").mkdir()

        broadcast("${Localization.PREFIX}${KColors.DODGERBLUE}ENABLED PLUGIN ")
    }

    override fun startup() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord")
        registerEventsAndCommands()
        SoupRecipes.register()
        LobbyScoreboard.startRunnable()
        CreateFiles
        //connectMongo()
        WorldManager.createFightWorld()
        WorldManager.createBuildWorld()
        console.success("Duels plugin enabled.")
    }

    override fun shutdown() {
        broadcast("${Localization.PREFIX}${KColors.TOMATO}DISABLING PLUGIN ${KColors.DARKGRAY}(maybe a reload)")
        onlinePlayers.forEach {
            if(it.gameMode != GameMode.CREATIVE)
                it.reset()
            it.sound(Sound.BLOCK_BEACON_DEACTIVATE)
        }
    }

    private fun registerEventsAndCommands() {
        SoupHealing.enable()
        ArenaTags.enable()
        OnPlayerChat.enable()
        OnPlayerQuit.enable()
        OnPlayerJoin.enable()
        OnChallenge.enable()
        OnAccept.enable()
        OnDamage.enable()
        OnMove.enable()
        OnFoodLevelChange.enable()
        OnInteractWithItem.enable()
        OnItemPickUp.enable()
        OnBuild.enable()
        OnPlayerCommandPreprocess.enable()
        OnInteractAtEntity.enable()
        OnDropItem.enable()
        OnWorldLoad.enable()
        OnPotionSplash.enable()
        Protection.enable()
        Kits.enable()
        SoupsimulatorEvents.enable()
        CreateArenaListener.enable()
        DuelOverviewGUI.enable()

        getCommand("challenge")!!.setExecutor(ChallengeCommand)
        getCommand("setspawn")!!.setExecutor(SetSpawnCommand)
        getCommand("arena")!!.setExecutor(ArenaCommand)
        getCommand("spec")!!.setExecutor(SpecCommand)
        getCommand("stats")!!.setExecutor(StatsCommand)
        getCommand("dueloverview")!!.setExecutor(DuelOverviewCommand)
        getCommand("hub")!!.setExecutor(HubCommand)
        getCommand("leave")!!.setExecutor(LeaveCommand)
        getCommand("rank")!!.setExecutor(RankCommand)
        getCommand("party")!!.setExecutor(PartyCommand)

        StaffOnItemDrop.enable()
        StaffOnInteract.enable()
        StaffOnInventoryClick.enable()
        getCommand("follow")!!.setExecutor(FollowCommand)
        getCommand("staffmode")!!.setExecutor(StaffmodeCommand)
        getCommand("ban")!!.setExecutor(BanCommand)
        getCommand("tempban")!!.setExecutor(TempBanCommand)

        Arenas.enable()
    }

    /*fun connectMongo() {
        mongoManager = MongoManager
        mongoManager.connect()
    }

    fun getMongoManager(): MongoManager {
        return mongoManager;
    }*/
}