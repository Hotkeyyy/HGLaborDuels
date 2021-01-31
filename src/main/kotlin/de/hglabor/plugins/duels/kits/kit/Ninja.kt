package de.hglabor.plugins.duels.kits.kit

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.data.PlayerSettings
import de.hglabor.plugins.duels.duel.GameState
import de.hglabor.plugins.duels.guis.ChooseKitGUI
import de.hglabor.plugins.duels.guis.PlayerSettingsGUI
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.broadcast
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerToggleSneakEvent
import kotlin.math.cos
import kotlin.math.sin

class Ninja : Kit(Kits.NINJA) {
    override val name = "Ninja"
    override fun itemInGUIs() = Kits.guiItem(Material.INK_SAC, name, "Soup")
    override val arenaTag = ArenaTags.NONE
    override val type = KitType.SOUP
    override val specials = listOf(Specials.NINJA)

    override fun giveKit(player: Player) {
        player.inventory.clear()
        player.inventory.setItem(0, KitUtils.sword(Material.STONE_SWORD, false))

        KitUtils.giveRecraft(player, 32)
        KitUtils.giveSoups(player)

        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    override fun enable() {
        ChooseKitGUI.addContent(ChooseKitGUI.KitsGUICompoundElement(itemInGUIs()))
        kitMap[kits] = this

        listen<PlayerToggleSneakEvent> {
            val player = it.player
            if (player.isInFight()) {
                val duel = Data.duelFromPlayer(player)
                if (duel.state == GameState.RUNNING) {
                    if (kitMap[duel.kit]!!.specials.contains(Specials.NINJA)) {
                        if (player.isSneaking) {
                            if (!Kits.hasCooldown(player)) {
                                val t: Player = duel.lastHitOfPlayer[player] ?: return@listen
                                Kits.setCooldown(player, 13)
                                var nang: Float = t.location.yaw + 90
                                if (nang < 0) nang += 360f
                                val nX = cos(Math.toRadians(nang.toDouble()))
                                val nZ = sin(Math.toRadians(nang.toDouble()))
                                val newDamagerLoc = t.location.clone().subtract(nX, 0.0, nZ)
                                player.teleport(newDamagerLoc)
                            }
                        }
                    }
                }
            }
        }
        kitMap[kits] = this
    }
}
