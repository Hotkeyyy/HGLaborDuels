package de.hglabor.plugins.duels.kits.kit

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.guis.ChooseKitGUI
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.kits.Kits.Companion.info
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.duel
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.cos
import kotlin.math.sin

class Ninja : Kit(Kits.NINJA) {
    override val name = "Ninja"
    override val itemInGUIs = Kits.guiItem(Material.INK_SAC, name, "Soup")
    override val arenaTag = ArenaTags.NONE
    override val type = KitType.SOUP
    override val specials = listOf(Specials.NINJA)

    override fun giveKit(player: Player) {
        player.inventory.clear()
        player.inventory.setItem(0, KitUtils.sword(Material.STONE_SWORD, false))

        KitUtils.giveRecraft(player, 64)
        KitUtils.giveSoups(player)

        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    override fun enable() {
        ChooseKitGUI.addContent(
            ChooseKitGUI.KitsGUICompoundElement(
                itemInGUIs,
                onClick = {
                    it.player.closeInventory()
                    Data.openedDuelGUI[it.player]?.let { it1 -> it.player.duel(it1, kits) }
                }
            ))

        listen<PlayerToggleSneakEvent> {
            val player = it.player
            if (player.isInFight()) {
                val duel = Data.duelFromPlayer(player)
                if (duel.hasBegan) {
                    if (kitMap[duel.kit]!!.specials.contains(Specials.NINJA)) {
                        if (player.isSneaking) {
                            val jetzt: Long = System.currentTimeMillis()
                            if (Kits.cooldown.containsKey(player)) {
                                val be = Kits.cooldown[player]
                                val rest: Long = (be!! + (1000 * 13)) - jetzt

                                if (rest > 0) {
                                    val second: Int = (rest / 1000).toInt()
                                    val ms = rest % 1000
                                    if (player.localization("de"))
                                        player.sendMessage("${Localization.PREFIX}Du musst noch ${KColors.DODGERBLUE}$second${KColors.DARKGRAY}:${KColors.DODGERBLUE}$ms ${(if (second == 1) " Sekunde" else " Sekunden")} ${KColors.GRAY}warten.")
                                    else
                                        player.sendMessage("${Localization.PREFIX}You still have to wait ${KColors.DODGERBLUE}$second${KColors.DARKGRAY}:${KColors.DODGERBLUE}$ms ${(if (second == 1) " second" else " seconds")}${KColors.GRAY}.")
                                    return@listen
                                }
                            }
                            val t: Player = duel.getOtherPlayer(player)
                            var nang: Float = t.location.yaw + 90
                            if (nang < 0) nang += 360f
                            val nX = cos(Math.toRadians(nang.toDouble()))
                            val nZ = sin(Math.toRadians(nang.toDouble()))
                            val newDamagerLoc = t.location.clone().subtract(nX, 0.0, nZ)

                            player.teleport(newDamagerLoc)
                            Kits.cooldown[player] = jetzt

                            object : BukkitRunnable() {
                                override fun run() {
                                    if (player.localization("de"))
                                        player.sendMessage(Localization.CAN_USE_KIT_AGAIN_DE)
                                    else
                                        player.sendMessage(Localization.CAN_USE_KIT_AGAIN_EN)
                                }
                            }.runTaskLater(Manager.INSTANCE, 20 * 13)
                        }
                    }
                }
            }
        }
        kitMap[kits] = this
    }
}
