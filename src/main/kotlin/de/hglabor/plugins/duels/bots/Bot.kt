package de.hglabor.plugins.duels.bots

import de.hglabor.plugins.duels.Manager
import de.hglabor.utils.noriskutils.pvpbots.PvPBot
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

object Bot {

    // ka halt so random names man kann auch einfach den namen + skin vom spieler nehmen
    val names = listOf(
        "NoRiskk",
        "BastiGHG",
        "Syriuus",
        "r0yzer",
        "Sasukey",
        "smoothyy",
        "bluefireoly",
        "WWWWWWWWWWWANTRO",
    )

    fun createBot(player: Player, world: World): PvPBot? {
        val settings = BotSettings.botSettings[player.uniqueId] ?: return null
        val name = names.random()

        return PvPBot(world, name, player, Manager.INSTANCE)
            .withHealth(settings.health)
            .withRange(settings.range)
            .withSkin(name)
            .withItemInSlot(EquipmentSlot.HAND, ItemStack(Material.STONE_SWORD))
            .withFollowRange(settings.followRange)
            .withMovementSpeed(settings.movementSpeed)
    }

}