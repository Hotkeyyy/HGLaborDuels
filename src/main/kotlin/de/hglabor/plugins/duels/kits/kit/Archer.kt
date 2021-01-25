package de.hglabor.plugins.duels.kits.kit

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.guis.ChooseKitGUI
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.party.Party
import de.hglabor.plugins.duels.tournament.Tournament
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.duel
import net.axay.kspigot.items.itemStack
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class Archer : Kit(Kits.ARCHER) {
    override val name = "Archer"
    override fun itemInGUIs() = Kits.guiItem(Material.BOW, name, null)
    override val arenaTag = ArenaTags.NONE
    override val type = KitType.NONE
    override val specials = listOf(null)

    override fun giveKit(player: Player) {
        player.inventory.clear()
        KitUtils.armor(player, Material.LEATHER_HELMET, Material.CHAINMAIL_CHESTPLATE,
            Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS)

        player.inventory.setItem(0, itemStack(Material.BOW) {
            addEnchantment(Enchantment.ARROW_INFINITE, 1) })

        player.inventory.setItem(1, ItemStack(Material.GOLDEN_APPLE, 8))

        player.inventory.setItem(7, ItemStack(Material.IRON_AXE, 1))
        player.inventory.setItem(8, ItemStack(Material.OAK_PLANKS, 24))

        object : BukkitRunnable() {
            override fun run() {
                player.inventory.setItem(9, ItemStack(Material.ARROW))
            }
        }.runTaskLater(Manager.INSTANCE, 20*4)


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