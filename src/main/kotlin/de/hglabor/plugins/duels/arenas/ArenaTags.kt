package de.hglabor.plugins.duels.arenas

import de.hglabor.plugins.duels.guis.ArenaTagsGUI
import de.hglabor.plugins.duels.guis.CreateArenaGUI
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
import net.axay.kspigot.extensions.broadcast
import org.bukkit.Material

enum class ArenaTags(val material: Material) {
    NONE(Material.BARRIER), SUMO(Material.LEAD), GLADIATOR(Material.IRON_BARS), UNDERWATER(Material.WATER_BUCKET), ICEFISHING(Material.ICE), NETHER(Material.NETHERRACK), JUMPANDRUN(Material.DIAMOND_BOOTS);

    companion object {
        fun enable() {
            enumValues<ArenaTags>().forEach { tag ->
                ArenaTagsGUI.addContent(
                    ArenaTagsGUI.TagsGUICompoundElement(
                        tag.material,
                        tag.toString().toLowerCase().capitalize(),
                        onClick = {
                            it.player.closeInventory()
                            val arena = arenaFromPlayer[it.player]
                            arena!!.tag = tag
                            it.player.sendLocalizedMessage(Localization.CHOOSE_TAG_DE.replace("%tag%", tag.toString()),
                                Localization.CHOOSE_TAG_EN.replace("%tag%", tag.toString()))
                            CreateArenaGUI.openCreateArenaGUI(it.player)
                        }
                    ))
                broadcast(tag.material.toString())
            }
        }
    }
}