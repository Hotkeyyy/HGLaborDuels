package de.hglabor.plugins.duels.database


import com.mongodb.MongoException
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document

object MongoManager {
    private lateinit var client: MongoClient
    private lateinit var database: MongoDatabase
    lateinit var playerStatsCollection: MongoCollection<Document>
    lateinit var playerSettingsCollection: MongoCollection<Document>

    fun connect() {

        try {
            MongoConfig.loadConfig()

            client = MongoClients.create(MongoConfig.uri())
            database = client.getDatabase(MongoConfig.database)

            playerStatsCollection = getCollection("duels_playerStats")
            playerSettingsCollection = getCollection("duels_playerSettings")
        } catch (e: MongoException) {
            e.printStackTrace()
        }
    }

    fun disconnect() {
        client.close()
    }

    private fun getCollection(name: String): MongoCollection<Document> {
        if (!database.listCollectionNames().contains(name))
            database.createCollection(name)

        return database.getCollection(name)
    }
}