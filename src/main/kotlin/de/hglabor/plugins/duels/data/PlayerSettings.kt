package de.hglabor.plugins.duels.data

import com.mongodb.client.model.Filters
import de.hglabor.plugins.duels.database.MongoManager
import org.bson.Document
import org.bukkit.entity.Player
import org.litote.kmongo.MongoOperator

class PlayerSettings(val player: Player) {


    companion object {
        fun get(player: Player): PlayerSettings {
            if (DataHolder.playerSettings.containsKey(player)) {
                return DataHolder.playerSettings[player]!!
            }
            val settings = PlayerSettings(player)
            settings.load()
            DataHolder.playerSettings[player] = settings
            return settings
        }

        enum class Knockback { OLD, NEW }
        enum class Chat { ALL, FIGHT, NONE }
    }

    private var values = mutableMapOf<String, Any>()

    fun load() {
        val document =
            MongoManager.playerSettingsCollection.find(Filters.eq("uuid", player.uniqueId.toString())).first()

        if (document == null) {
            values["knockback"] = Knockback.NEW.toString()
            values["attackSound"] = true
            values["allowSpectators"] = true
            values["chatInFight"] = Chat.ALL.toString()

            MongoManager.playerSettingsCollection.insertOne(toDocument())

            return
        }

        values.putAll(document)
    }

    fun knockback() = Knockback.valueOf(values["knockback"] as String)
    fun setKnockback(knockback: Knockback) {
        values["knockback"] = knockback.toString()
    }

    fun ifAttackSound() = values["attackSound"] as Boolean
    fun setAttackSound(boolean: Boolean) {
        values["attackSound"] = boolean
    }

    fun ifAllowSpectators() = values["allowSpectators"] as Boolean
    fun setAllowSpectators(boolean: Boolean) {
        values["allowSpectators"] = boolean
    }

    fun chatInFight() = Chat.valueOf(values["chatInFight"] as String)
    fun setChatInFight(chat: Chat) {
        values["chatInFight"] = chat.toString()
    }

    private fun toDocument(): Document {
        val document = Document("uuid", player.uniqueId.toString())
        values.forEach(document::append)
        return document
    }

    fun update() {
        MongoManager.playerSettingsCollection.updateOne(
            Filters.eq("uuid", player.uniqueId.toString()),
            Document("${MongoOperator.set}", toDocument())
        )
    }
}