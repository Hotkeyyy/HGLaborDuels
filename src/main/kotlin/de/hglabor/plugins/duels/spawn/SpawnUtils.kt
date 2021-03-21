package de.hglabor.plugins.duels.spawn

import de.hglabor.plugins.duels.localization.sendMsg
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.io.IOException

object SpawnUtils {

    private var spawnLoc: Location? = null

    var config = File("plugins//HGLaborDuels//spawn.yml")
    var yamlConfiguration = YamlConfiguration.loadConfiguration(config)

    fun setSpawn(player: Player) {
        yamlConfiguration["spawn"] = player.location
        spawnLoc = player.location
        player.sendMsg("spawn.set")

        try {
            yamlConfiguration.save(config)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getSpawn(): Location {
        return if (spawnLoc != null)
            spawnLoc as Location
        else {
            config = File("plugins//HGLaborDuels//spawn.yml")
            yamlConfiguration = YamlConfiguration.loadConfiguration(config)
            yamlConfiguration.getLocation("spawn")?: Bukkit.getWorld("world")!!.spawnLocation
        }
    }
}