package de.hglabor.plugins.duels.kits

import de.hglabor.plugins.duels.Manager
import de.hglabor.plugins.duels.guis.QueueGUI
import de.hglabor.plugins.duels.kits.kit.`fun`.HardJumpAndRun
import de.hglabor.plugins.duels.kits.kit.`fun`.IceFishing
import de.hglabor.plugins.duels.kits.kit.`fun`.JumpAndRun
import de.hglabor.plugins.duels.kits.kit.`fun`.Spleef
import de.hglabor.plugins.duels.kits.kit.cooldown.Classic
import de.hglabor.plugins.duels.kits.kit.cooldown.Crystal
import de.hglabor.plugins.duels.kits.kit.cooldown.Diamond
import de.hglabor.plugins.duels.kits.kit.pot.Debuff
import de.hglabor.plugins.duels.kits.kit.pot.NoDebuff
import de.hglabor.plugins.duels.kits.kit.soup.*
import de.hglabor.plugins.duels.kits.kit.trash.Archer
import de.hglabor.plugins.duels.kits.kit.trash.OnlySword
import de.hglabor.plugins.duels.kits.kit.trash.SG
import de.hglabor.plugins.duels.kits.kit.trash.Sumo
import de.hglabor.plugins.duels.kits.kit.uhc.UHC
import de.hglabor.plugins.duels.localization.sendMsg
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.bukkit.feedSaturate
import net.axay.kspigot.extensions.bukkit.heal
import net.axay.kspigot.gui.ForInventorySixByNine
import net.axay.kspigot.gui.InventorySlotCompound
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.items.flag
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.runnables.async
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

enum class KitType { SOUP, POT, BOW }
enum class KitCategory(val itemStack: ItemStack, val magentaSlot: InventorySlotCompound<ForInventorySixByNine>) {
    MAIN(itemStack(Material.LODESTONE) { meta { name = "${KColors.DODGERBLUE}Main" } }, Slots.RowFourSlotTwo),
    SOUP(itemStack(Material.MUSHROOM_STEW) { meta { name = "${KColors.DODGERBLUE}Soup" } }, Slots.RowFourSlotThree),
    POT(itemStack(Material.SPLASH_POTION) {
        meta<org.bukkit.inventory.meta.PotionMeta> {
            name = "${KColors.DODGERBLUE}Pot"
            basePotionData = org.bukkit.potion.PotionData(org.bukkit.potion.PotionType.INSTANT_HEAL, false, true)
            flag(ItemFlag.HIDE_ATTRIBUTES)
        }
    }, Slots.RowFourSlotFour),
    UHC(itemStack(Material.GOLDEN_APPLE) { meta { name = "${KColors.DODGERBLUE}UHC" } }, Slots.RowFourSlotFive),
    FUN(itemStack(Material.NETHER_STAR) { meta { name = "${KColors.DODGERBLUE}Fun" } }, Slots.RowFourSlotSix),
    COOLDOWN(itemStack(Material.CLOCK) { meta { name = "${KColors.DODGERBLUE}Cooldown" } }, Slots.RowFourSlotSeven),
    TRASH(itemStack(Material.LEAD) { meta { name = "${KColors.DODGERBLUE}Trash" } }, Slots.RowFourSlotEight)
}

object Kits {
    val mainKits = mutableListOf<AbstractKit>()
    val cooldownStart = hashMapOf<Player, Long>()
    val cooldownTime = hashMapOf<Player, Long>()
    val cooldownTask = hashMapOf<Player, BukkitTask>()

    fun guiItem(material: Material, name: String) =
        itemStack(material) {
            meta {
                this.name = "${KColors.DEEPSKYBLUE}$name"
                flag(ItemFlag.HIDE_ATTRIBUTES)
                flag(ItemFlag.HIDE_POTION_EFFECTS)
            }
        }


    fun random(): AbstractKit {
        return kits.filter { it != JumpAndRun && it != HardJumpAndRun }.random()
    }

    fun enable() {
        Anchor.enable()
        Archer.enable()
        Classic.enable()
        Crystal.enable()
        Debuff.enable()
        Diamond.enable()
        EHG.enable()
        Feast.enable()
        HG().enable()
        HardJumpAndRun.enable()
        IceFishing.enable()
        JumpAndRun.enable()
        Ninja.enable()
        NoDebuff.enable()
        Onebar.enable()
        OnlySword.enable()
        Speed.enable()
        SG.enable()
        Spleef.enable()
        Sumo.enable()
        UHC.enable()
        Underwater.enable()

        async {
            kits.forEach { kit ->
                QueueGUI.updateQueueItem(kit)
                nameToKit[kit.name] = kit
            }
        }
    }

    fun setCooldown(player: Player, seconds: Long) {
        val jetzt: Long = System.currentTimeMillis()
        cooldownStart[player] = jetzt
        cooldownTime[player] = seconds

        cooldownTask[player] = object : BukkitRunnable() {
            override fun run() {
                player.sendMsg("kits.canUseAgain")
            }
        }.runTaskLater(Manager.INSTANCE, 20 * seconds)
    }

    fun hasCooldown(player: Player): Boolean {
        val jetzt: Long = System.currentTimeMillis()
        if (cooldownStart.containsKey(player) && cooldownTime.containsKey(player)) {
            val be = cooldownStart[player]
            val rest: Long = (be!! + (1000 * cooldownTime[player]!!)) - jetzt

            if (rest > 0) {
                val second: Int = (rest / 1000).toInt()
                val ms = rest % 1000
                player.sendMsg("kits.cooldown", mutableMapOf("second" to "$second", "ms" to "$ms"))
                return true
            }
        }
        return false
    }

    fun removeCooldown(player: Player) {
        cooldownTask[player]?.cancel()
        cooldownTask.remove(player)
        cooldownTime.remove(player)
        cooldownStart.remove(player)
    }

    fun Player.giveKit(kit: AbstractKit) {
        val player: Player = player!!
        player.heal()
        player.feedSaturate()
        player.exp = 0f
        player.level = 0
        for (effect in player.activePotionEffects) {
            player.removePotionEffect(effect.type)
        }
        kit.giveKit(player)
    }
}
