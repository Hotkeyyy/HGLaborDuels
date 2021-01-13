package de.hglabor.plugins.duels.database


import com.google.gson.Gson
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document
import org.bukkit.entity.Player
import org.litote.kmongo.KMongo

object MongoManager {
    private lateinit var client: MongoClient
    private lateinit var database: MongoDatabase
    lateinit var playerStatsCollection: MongoCollection<Document>
    lateinit var playerSettingsCollection: MongoCollection<Document>

    fun connect() {

        MongoConfig.loadConfig()

        client = MongoClients.create(MongoConfig.uri())
        database = client.getDatabase(MongoConfig.database)

        playerStatsCollection = getCollection("duels_playerStats")
        playerSettingsCollection = getCollection("duels_playerSettings")
    }

    fun disconnect() {
        client.close()
    }

    fun getCollection(name: String): MongoCollection<Document> {
        if (!database.listCollectionNames().contains(name))
            database.createCollection(name)

        return database.getCollection(name)
    }
}