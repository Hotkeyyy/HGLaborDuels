package de.hglabor.plugins.duels.database.temporaryalternative

import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException
import java.util.*

class Stats(val uuid: UUID) {
    val file = File("plugins//HGLaborDuels//temp//stats.yml")
    private val yamlConfiguration = YamlConfiguration.loadConfiguration(file)

    fun create() {
        yamlConfiguration[uuid.toString()]
        yamlConfiguration["$uuid.name"] = Bukkit.getOfflinePlayer(uuid).name
        yamlConfiguration["$uuid.stats.totalGames"] = 0
        yamlConfiguration["$uuid.stats.kills"] = 0
        yamlConfiguration["$uuid.stats.deaths"] = 0
        yamlConfiguration["$uuid.stats.soupsEaten"] = 0
        yamlConfiguration["$uuid.stats.totalHits"] = 0
        saveFile()
    }

    fun exist(): Boolean {
        return yamlConfiguration.contains(uuid.toString())
    }

    fun getTotalGames(): Int {
        return yamlConfiguration["$uuid.stats.totalGames"] as Int
    }

    fun addGame() {
        yamlConfiguration["$uuid.stats.totalGames"] = getTotalGames() + 1
        saveFile()
    }

    fun getKills(): Int {
        return yamlConfiguration["$uuid.stats.kills"] as Int
    }

    fun addKill() {
        yamlConfiguration["$uuid.stats.kills"] = getKills() + 1
        saveFile()
    }

    fun getDeaths(): Int {
        return yamlConfiguration["$uuid.stats.deaths"] as Int
    }

    fun addDeath() {
        yamlConfiguration["$uuid.stats.deaths"] = getDeaths() + 1
        saveFile()
    }

    fun getSoupsEaten(): Int {
        return yamlConfiguration["$uuid.stats.soupsEaten"] as Int
    }

    fun addSoupEaten() {
        yamlConfiguration["$uuid.stats.soupsEaten"] = getSoupsEaten() + 1
        saveFile()
    }

    fun getTotalHits(): Int {
        return yamlConfiguration["$uuid.stats.totalHits"] as Int
    }

    fun addTotalHit() {
        yamlConfiguration["$uuid.stats.totalHits"] = getTotalHits() + 1
        saveFile()
    }

    private fun saveFile() {
        try {
            yamlConfiguration.save(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}