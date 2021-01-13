package de.hglabor.plugins.duels.database

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object MongoConfig {
    var file = File("plugins//HGLaborDuels//MongoDB.yml")
    var yamlConfiguration = YamlConfiguration.loadConfiguration(file)

    lateinit var host: String
    var port = 27017
    lateinit var username: String
    lateinit var password: String
    lateinit var database: String

    fun loadConfig() {
        host = yamlConfiguration["host"].toString()
        port = yamlConfiguration["port"] as Int
        username = yamlConfiguration["username"].toString()
        password = yamlConfiguration["password"].toString()
        database = yamlConfiguration["database"].toString()
    }

    fun uri(): String {
        return "mongodb://$username:$password@$host:$port/$database"
    }
}