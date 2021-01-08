package de.hglabor.plugins.duels.database


import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document
import org.bukkit.entity.Player

object MongoManager {
    private lateinit var client: MongoClient
    private lateinit var database: MongoDatabase
    private lateinit var collection: MongoCollection<Document>

    fun connect() {
        this.client = MongoClients.create("mongodb://${MongoConfig.username}:${MongoConfig.password}@${MongoConfig.host}:${MongoConfig.port}/${MongoConfig.database}")
        this.database = this.client.getDatabase(MongoConfig.database)
        this.collection = this.database.getCollection(MongoConfig.collection)
    }

    fun playerStatsExists(player: Player): Boolean {
        val filter = Document("uuid", player.uniqueId.toString())
        if(collection.find(filter).count() < 1)
            return true
        return false
    }

    fun createPlayerStats(player: Player) {
        val document = Document("uuid", player.uniqueId.toString())
            .append("lastPlayerName", player.name)
            .append("elo", 1000)
            .append("kills", 0)
            .append("deaths", 0)
            .append("soupsEaten", 0)
            .append("soupAccuracy", 0) //prozent
            .append("totalHits", 0)
            .append("soupsimulatorHighscore", 0)
        collection.insertOne(document)
    }
}