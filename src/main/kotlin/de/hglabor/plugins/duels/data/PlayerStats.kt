package de.hglabor.plugins.duels.data

import com.mongodb.client.model.Filters
import de.hglabor.plugins.duels.database.MongoManager
import org.bson.Document
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.util.NumberConversions
import org.litote.kmongo.MongoOperator
import java.text.DecimalFormat
import java.util.*

class PlayerStats(val uuid: UUID) {
    companion object {
        fun get(player: Player): PlayerStats {
            if (DataHolder.playerStats.containsKey(player)) {
                return DataHolder.playerStats[player]!!
            }
            val stats = PlayerStats(player.uniqueId)
            stats.load()
            DataHolder.playerStats[player] = stats
            return stats
        }

        fun get(offlinePlayer: OfflinePlayer): PlayerStats {
            val stats = PlayerStats(offlinePlayer.uniqueId)
            stats.load()
            return stats
        }

        fun exist(uuid: UUID): Boolean {
            val document = MongoManager.playerStatsCollection.find(Filters.eq("uuid", uuid.toString())).first()
            return document != null
        }
    }

    private var values = mutableMapOf<String, Any>()

    fun load() {
        val document = MongoManager.playerStatsCollection.find(Filters.eq("uuid", uuid.toString())).first()

        if (document == null) {
            values["elo"] = 1000
            values["kills"] = 0
            values["deaths"] = 0
            values["soupsEaten"] = 0
            values["totalHits"] = 0
            values["soupsimulatorHighscore"] = 0
            MongoManager.playerStatsCollection.insertOne(toDocument())
            return
        }
        values.putAll(document)
    }

    fun totalGames() = NumberConversions.toInt(values["totalGames"])
    fun addTotalGame() {
        values["totalGames"] = totalGames() + 1
    }

    fun elo() = NumberConversions.toInt(values["elo"])
    fun setElo(elo: Int) {
        values["elo"] = elo
    }

    fun kills() = NumberConversions.toInt(values["kills"])
    fun addKill() {
        values["kills"] = kills() + 1
    }

    fun deaths() = NumberConversions.toInt(values["deaths"])
    fun addDeath() {
        values["deaths"] = deaths() + 1
    }

    fun kd(): Double {
        val kd = if (kills() != 0) kills().toDouble() / deaths().toDouble() else kills().toDouble()
        val df = DecimalFormat("###.##")
        return df.format(kd).toDouble()
    }

    fun soupsEaten() = NumberConversions.toInt(values["soupsEaten"])
    fun addEatenSoup() {
        values["soupsEaten"] = soupsEaten() + 1
    }

    fun totalHits() = NumberConversions.toInt(values["totalHits"])
    fun addTotalHit() {
        values["totalHits"] = totalHits() + 1
    }

    fun soupsimulatorHighscore() = NumberConversions.toInt(values["soupsimulatorHighscore"])
    fun setSoupsimulatorHighscore(score: Int) {
        values["soupsimulatorHighscore"] = score
    }

    private fun toDocument(): Document {
        val document = Document("uuid", uuid.toString())
        values.forEach(document::append)
        return document
    }

    fun update() {
        MongoManager.playerStatsCollection.updateOne(
            Filters.eq("uuid", uuid.toString()), Document("${MongoOperator.set}", toDocument()))
    }

}