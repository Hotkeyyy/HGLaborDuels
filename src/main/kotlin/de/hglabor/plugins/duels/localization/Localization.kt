package de.hglabor.plugins.duels.localization

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import de.hglabor.plugins.duels.Manager
import net.axay.kspigot.chat.KColors
import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player
import java.io.FileReader
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

object Localization {
    private val translations = mutableMapOf<Locale, MutableMap<String, String>>()
    private val kcolors = mutableMapOf<String, ChatColor>()
    private val gson: Gson = Gson()

    val PREFIX = " ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}Duels ${KColors.DARKGRAY}» ${KColors.GRAY}"
    val SOUPSIMULATOR_PREFIX = " ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}Soupsimulator ${KColors.DARKGRAY}» ${KColors.GRAY}"
    val PARTY_PREFIX = " ${KColors.DARKGRAY}| ${KColors.MAGENTA}Party ${KColors.DARKGRAY}» ${KColors.GRAY}"
    val STAFF_PREFIX = " ${KColors.DARKGRAY}| ${KColors.DARKPURPLE}Staff ${KColors.DARKGRAY}» ${KColors.GRAY}"

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

    private fun getRawMessage(key: String, player: Player): String {
        val locale = getLocalization(player)

        if (translations.containsKey(locale)) {
            if (translations[locale]!!.getOrDefault(key, key) == "")
                return key
        }

        return if (translations.containsKey(locale))
            translations[locale]!!.getOrDefault(key, key)
        else
            translations[Locale.ENGLISH]!!.getOrDefault(key, key)
    }

    fun getMessage(key: String, player: Player): String {
        val m = getRawMessage(key, player)
        var message = getRawMessage(key, player)
        val locale = getLocalization(player)

        message = message.replace("%prefix%", PREFIX)
        message = message.replace("%soupsimulatorPrefix%", SOUPSIMULATOR_PREFIX)
        message = message.replace("%partyPrefix%", PARTY_PREFIX)
        message = message.replace("%staffPrefix%", STAFF_PREFIX)

        while (message.contains('$')) {
            val color = message.substringAfter('$').substringBefore('$')
            //message.replace("$$color$", kcolors[color.toUpperCase()].toString())

            message = if (kcolors.containsKey(color))
                message.replace("$$color$", kcolors[color].toString())
            else
                message.replace("$", "")
        }
        if (m != message)
            translations[locale]?.set(key, message)
        return message
    }

    fun getMessage(key: String, values: MutableMap<String, String>? = null, player: Player): String {
        var message = getMessage(key, player)

        values?.forEach { (key, value) ->
            message = message.replace("%$key%", value)
        }
        return message
    }

    init {
        loadLanguageFiles(Paths.get("${Manager.INSTANCE.dataFolder}/lang"))
        loadKColors()
    }
}


fun Player.sendMsg(key: String, values: MutableMap<String, String>? = null) {
    if (player != null) {
        val message = Localization.getMessage(key, values, player!!)
        player?.sendMessage(message)
    }
}


