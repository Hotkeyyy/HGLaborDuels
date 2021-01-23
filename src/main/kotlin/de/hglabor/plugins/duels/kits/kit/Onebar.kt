package de.hglabor.plugins.duels.kits.kit

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.guis.ChooseKitGUI
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.party.Party
import de.hglabor.plugins.duels.tournament.Tournament
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.duel
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Onebar : Kit(Kits.ONEBAR) {
    override val name = "Onebar"
    override fun itemInGUIs() = Kits.guiItem(Material.WOODEN_SWORD, name, "Soup")
    override val arenaTag = ArenaTags.NONE
    override val type = KitType.SOUP
    override val specials = listOf(null)

    override fun giveKit(player: Player) {
        player.inventory.clear()
        player.inventory.setItem(0, KitUtils.sword(Material.WOODEN_SWORD, false))

        for (i in 1..8) {
            if (player.inventory.getItem(i) == null) {
                player.inventory.setItem(i, ItemStack(Material.MUSHROOM_STEW))
            }
        }
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    override fun enable() {
        ChooseKitGUI.addContent(
            ChooseKitGUI.KitsGUICompoundElement(
                itemInGUIs(),
                onClick = {
                    it.player.closeInventory()
                    if (Data.openedKitInventory[it.player] == Data.KitInventories.DUEL)
                        Data.openedDuelGUI[it.player]?.let { it1 -> it.player.duel(it1, kits) }

                    else if (Data.openedKitInventory[it.player] == Data.KitInventories.SPLITPARTY) {
                        val team = Party.get(it.player)!!
                        Duel.create(team.getSplitTeams().first, team.getSplitTeams().second, kits)

                    } else if (Data.openedKitInventory[it.player] == Data.KitInventories.TOURNAMENT)
                        Tournament.createPublic(it.player, kits)
                }
            ))
        kitMap[kits] = this
    }

}