import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.guis.KitsGUI
import de.hglabor.plugins.duels.kits.*
import de.hglabor.plugins.duels.kits.kit.`fun`.HardJumpAndRun
import de.hglabor.plugins.duels.kits.specials.Specials
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class Spleef: AbstractKit() {
    companion object {
        val INSTANCE = Spleef()
    }

    override val name = "Spleef"
    override val itemInGUI = Kits.guiItem(Material.DIAMOND_SHOVEL, name)
    override val arenaTag = ArenaTags.SPLEEF
    override val category = KitCategory.FUN
    override val specials = setOf(Specials.INVINICIBLE)

    override fun giveKit(player: Player) {
        player.inventory.clear()
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    fun enable() {
        kits += INSTANCE

    }
}