package de.hglabor.plugins.duels.kits.kit

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.guis.ChooseKitGUI
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.party.Party
import de.hglabor.plugins.duels.tournament.Tournament
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.duel
import net.axay.kspigot.items.flag
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

class IceFishing : Kit(Kits.ICEFISHING) {
    override val name = "Ice Fishing"
    override fun itemInGUIs() = Kits.guiItem(Material.FISHING_ROD, name, "Pull your enemy into the water")
    override val arenaTag = ArenaTags.ICEFISHING
    override val type = KitType.NONE
    override val specials = listOf(Specials.NODAMAGE, Specials.DEADINWATER)

    override fun giveKit(player: Player) {
        player.inventory.clear()
        player.inventory.setItem(0, itemStack(Material.FISHING_ROD) { amount = 1
            meta {isUnbreakable = true; flag(ItemFlag.HIDE_UNBREAKABLE) } })

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