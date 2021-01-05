package de.hglabor.plugins.duels.spawn

import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.io.IOException
import kotlin.math.roundToInt

object SpawnUtils {

    var config = File("plugins//HGLaborDuels//spawn.yml")
    var yamlConfiguration = YamlConfiguration.loadConfiguration(config)

    fun setSpawn(player: Player) {
        yamlConfiguration["spawn"] = player.location

        if (player.localization("de"))
            player.sendMessage(Localization.SETSPAWN_DE)
        else
            player.sendMessage(Localization.SETSPAWN_EN)

        try {
            yamlConfiguration.save(config)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getSpawn(): Location {
        config = File("plugins//HGLaborDuels//spawn.yml")
        yamlConfiguration = YamlConfiguration.loadConfiguration(config)

        val loc = yamlConfiguration.getLocation("spawn")

        return loc!!
    }
}