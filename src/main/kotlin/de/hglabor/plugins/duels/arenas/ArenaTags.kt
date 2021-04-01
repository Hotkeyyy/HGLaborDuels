package de.hglabor.plugins.duels.arenas

import de.hglabor.plugins.duels.guis.ArenaTagsGUI
import de.hglabor.plugins.duels.guis.CreateArenaGUI
import net.axay.kspigot.gui.openGUI
import org.bukkit.Material

enum class ArenaTags(val material: Material) {
    NONE(Material.BARRIER),
    SUMO(Material.LEAD),
    HG(Material.MUSHROOM_STEW),
    UNDERWATER(Material.WATER_BUCKET),
    ICEFISHING(Material.ICE),
    NETHER(Material.NETHERRACK),
    JUMPANDRUN(Material.DIAMOND_BOOTS),
    HARDJUMPANDRUN(Material.NETHERITE_BOOTS),
    SPLEEF(Material.DIAMOND_SHOVEL),
    DEFAULT(Material.GRASS_BLOCK);

    companion object {
        fun enable() {
            enumValues<ArenaTags>().forEach { tag ->
                ArenaTagsGUI.addContent(
                    ArenaTagsGUI.TagsGUICompoundElement(
                        tag.material,
                        tag.toString().toLowerCase().capitalize(),
                        onClick = {
                            it.player.closeInventory()
                            val arena = arenaFromPlayer[it.player] ?: return@TagsGUICompoundElement
                            arena.tag = tag
                            //it.player.sendMsg("arena.creation.choseTag", mutableMapOf("tag" to tag.toString()))
                            it.player.openGUI(CreateArenaGUI.guiBuilder(it.player))
                        }
                    ))
            }
        }
    }
}