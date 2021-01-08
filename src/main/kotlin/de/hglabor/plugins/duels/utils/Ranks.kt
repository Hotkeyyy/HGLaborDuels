package de.hglabor.plugins.duels.utils

import org.bukkit.ChatColor
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.io.IOException

object Ranks {
    enum class Rank(val prefix: String?, val color: ChatColor) {
        OWNER("§4Owner §8| ", ChatColor.DARK_RED),
        ADMIN("", ChatColor.RED),
        MOD("", ChatColor.DARK_PURPLE),
        HELPER("", ChatColor.LIGHT_PURPLE),
        NORMIEPLUS("", ChatColor.DARK_GREEN),
        NORMIE("", ChatColor.GRAY)
    }

    val file = File("plugins//HGLaborDuels//ranks.yml")
    val yamlConfiguration = YamlConfiguration.loadConfiguration(file)

    fun setRank(player: Player, rank: Rank) {
        yamlConfiguration[player.uniqueId.toString()] = rank.toString()

        try {
            yamlConfiguration.save(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getRank(player: Player): Rank {
        return Rank.valueOf(yamlConfiguration[player.uniqueId.toString()] as String)
    }

    fun enumContains(name: String): Boolean {
        return enumValues<Rank>().any { it.name == name}
    }
}