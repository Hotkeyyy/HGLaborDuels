package de.hglabor.plugins.duels.utils

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

object CreateFiles {
    init {
        arenasFile()
        mongoDBFile()
        statsFile()
    }

    private fun arenasFile() {
        val file = File("arenas//arenas.yml")
        val yamlConfiguration = YamlConfiguration.loadConfiguration(file)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        try {
            yamlConfiguration.save(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun mongoDBFile() {
        val file = File("plugins//HGLaborDuels//MongoDB.yml")
        val yamlConfiguration = YamlConfiguration.loadConfiguration(file)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            yamlConfiguration["host"] = "localhost"
            yamlConfiguration["port"] = 27017
            yamlConfiguration["username"] = "admin"
            yamlConfiguration["password"] = "password123"
            yamlConfiguration["database"] = "duels"
            yamlConfiguration["collection"] = "stats"
        }

        try {
            yamlConfiguration.save(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun statsFile() {
        val file = File("plugins//HGLaborDuels//temp//stats.yml")
        val yamlConfiguration = YamlConfiguration.loadConfiguration(file)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            yamlConfiguration["totalPlayers"] = 0
        }

        try {
            yamlConfiguration.save(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}