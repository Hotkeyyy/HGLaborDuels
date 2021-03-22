package de.hglabor.plugins.duels.localization

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import de.hglabor.plugins.duels.Manager
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.broadcast
import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player
import java.io.FileReader
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

class Localization {
    private val translations = mutableMapOf<Locale, MutableMap<String, String>>()
    private val kcolors = mutableMapOf<String, ChatColor>()
    private val gson: Gson = Gson()

    private fun loadLanguageFiles(folder: Path) {
        try {
            val files = folder.toFile().listFiles() ?: throw Exception("Folder files are null")
            for (languageFile in files) {
                var name = languageFile.name
                System.out.printf("Parsing file %s\n", name)
                if (name.contains("_")) {
                    name = name.substring(name.indexOf("_") + 1, name.lastIndexOf(".json"))
                    val reader = JsonReader(FileReader(languageFile))
                    val json = gson.fromJson<MutableMap<String, String>>(reader, MutableMap::class.java)
                    if (json != null) {
                        val key = Locale.forLanguageTag(name)
                        if (translations.containsKey(key)) {
                            translations[key]!!.putAll(json)
                        } else {
                            translations[key] = json
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadKColors() {
        val fields = KColors::class.java.declaredFields
        fields.forEach { field ->
            kcolors[field.name] = field.get(field) as ChatColor
        }
    }

    private fun getLocalization(player: Player): Locale {
        val playerLocale = Locale(player.locale)
        return if (playerLocale.language.startsWith("de"))
            Locale.GERMAN
        else
            Locale.ENGLISH
    }

    fun getMessage(key: String, player: Player): String {
        val locale = INSTANCE.getLocalization(player)

        if (INSTANCE.translations.containsKey(locale)) {
            if (INSTANCE.translations[locale]!!.getOrDefault(key, key) == "")
                return key
        }

        return if (INSTANCE.translations.containsKey(locale))
            INSTANCE.translations[locale]!!.getOrDefault(key, key)
        else
            INSTANCE.translations[Locale.ENGLISH]!!.getOrDefault(key, key)
    }

    fun getMessage(key: String, values: MutableMap<String, String>? = null, player: Player): String {
        var message = getMessage(key, player)

        message = message.replace("%prefix%", PREFIX)
        message = message.replace("%soupsimulatorPrefix%", SOUPSIMULATOR_PREFIX)
        message = message.replace("%partyPrefix%", PARTY_PREFIX)
        message = message.replace("%staffPrefix%", STAFF_PREFIX)

        message = message.replace("#n", "\n")
        values?.forEach { (key, value) ->
            message = message.replace("%$key%", value)
        }

        while (message.contains('$')) {
            val color = message.substringAfter('$').substringBefore('$')
            message.replace("$$color$", kcolors[color.toUpperCase()].toString())

            /*message = if (kcolors.containsKey(color))
                message.replace("$$color$", kcolors[color].toString())
            else
                message.replace("$", "")*/
        }

        return message
    }

    companion object {
        lateinit var INSTANCE: Localization
        val PREFIX = " ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}Duels ${KColors.DARKGRAY}» ${KColors.GRAY}"
        val SOUPSIMULATOR_PREFIX = " ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}Soupsimulator ${KColors.DARKGRAY}» ${KColors.GRAY}"
        val PARTY_PREFIX = " ${KColors.DARKGRAY}| ${KColors.MAGENTA}Party ${KColors.DARKGRAY}» ${KColors.GRAY}"
        val STAFF_PREFIX = " ${KColors.DARKGRAY}| ${KColors.DARKPURPLE}Staff ${KColors.DARKGRAY}» ${KColors.GRAY}"
    }

    init {
        INSTANCE = this
        loadLanguageFiles(Paths.get("${Manager.INSTANCE.dataFolder}/lang"))
        loadKColors()
    }
}


fun Player.sendMsg(key: String, values: MutableMap<String, String>? = null) {
    if (player != null) {
        val message = Localization.INSTANCE.getMessage(key, values, player!!)
        player?.sendMessage(message)
    }
}


