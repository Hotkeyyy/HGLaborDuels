package de.hglabor.plugins.duels.database

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object MongoConfig {
    var file = File("plugins//HGLaborDuels//MongoDB.yml")
    var yamlConfiguration = YamlConfiguration.loadConfiguration(file)

    var host = yamlConfiguration["host"].toString()
    var port = yamlConfiguration["port"]
    var username = yamlConfiguration["username"].toString()
    var password = yamlConfiguration["password"].toString()
    var database = yamlConfiguration["database"].toString()
    var collection = yamlConfiguration["collection"].toString()
}