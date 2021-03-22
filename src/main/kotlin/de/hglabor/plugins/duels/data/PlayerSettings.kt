package de.hglabor.plugins.duels.data

import com.mongodb.client.model.Filters
import de.hglabor.plugins.duels.database.MongoManager
import org.bson.Document
import org.bukkit.entity.Player
import org.litote.kmongo.MongoOperator
import java.util.*

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

        fun exist(uuid: UUID): Boolean {
            val document = MongoManager.playerSettingsCollection.find(Filters.eq("uuid", uuid.toString())).first()
            return document != null
        }

        enum class Knockback(val version: String) { OLD("1.8"), NEW("1.16"), ANCHOR("Anchor") }
        enum class AllowSpectators(val key: String) { ALLOW("settingsgui.item.allowSpecs.lore.allow"), DENY("settingsgui.item.allowSpecs.lore.deny") }
    }

    private var values = mutableMapOf<String, Any>()

    fun load() {
        val document =
            MongoManager.playerSettingsCollection.find(Filters.eq("uuid", player.uniqueId.toString())).first()

        if (document == null) {
            values["knockback"] = Knockback.NEW.toString()
            values["allowSpectators"] = true

            MongoManager.playerSettingsCollection.insertOne(toDocument())

            return
        }

        values.putAll(document)
    }

    fun knockback() = Knockback.valueOf(values["knockback"] as String)
    fun setKnockback(knockback: Knockback) {
        values["knockback"] = knockback.toString()
    }

    fun allowSpectators() = AllowSpectators.valueOf(values["allowSpectators"] as String)
    fun ifAllowSpectators() = values["allowSpectators"] == "ALLOW"
    fun setAllowSpectators(allowSpectators: AllowSpectators) {
        values["allowSpectators"] = allowSpectators.toString()
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