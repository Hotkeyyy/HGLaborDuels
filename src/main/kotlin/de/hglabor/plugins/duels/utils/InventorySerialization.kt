import de.hglabor.plugins.duels.utils.KitUtils
import net.axay.kspigot.utils.hasMark
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionType


object InventorySerialization {
    fun serializeInventory(inventoryMap: MutableMap<Int, ItemStack>): String {
        var serializedInventory = String()

        for (slot in inventoryMap.keys) {
            val item = inventoryMap[slot]

            if (item == null || item.type == Material.AIR) {
                continue
            }
            var serializedItemStack = String()

            // slot
            serializedItemStack += "s=$slot"

            // material (type)
            var type = item.type
            serializedItemStack +=
                if (!item.hasMark("goldenhead")) "-t=$type"
                else "-t=GOLDENHEAD"

            // durability
            if (item is Damageable && item.hasDamage()) {
                serializedItemStack += "-d=${item.damage}"
            }

            // amount
            val amount = item.amount
            if (amount != 1)
                serializedItemStack += "-a=$amount"

            // enchantments
            val enchantments = item.enchantments
            if (enchantments.isNotEmpty()) {
                enchantments.forEach { (enchantment, level) ->
                    serializedItemStack += "-e=${enchantment.key}:$level"
                }
            }

            // potioneffects
            if (item.itemMeta != null) {
                val itemMeta = item.itemMeta ?: continue
                // potioneffect
                if (itemMeta is PotionMeta) {
                    serializedItemStack += "-p=${itemMeta.basePotionData.type}:${itemMeta.basePotionData.isExtended}:${itemMeta.basePotionData.isUpgraded}"
                }
            }
            serializedInventory += "$serializedItemStack;"
        }
        return serializedInventory
    }

    fun deserializeInventory(string: String): MutableMap<Int, ItemStack> {
        val inventoryContents = mutableMapOf<Int, ItemStack>()
        val serializedBlocks = string.split(";").toMutableList()
        serializedBlocks.removeLast()

        // one block is one inventory slot
        serializedBlocks.forEach { block ->
            var slot = -1
            var itemStack: ItemStack? = null

            val attributes = block.split('-').toTypedArray()
            attributes.forEach { attribute ->
                val value = attribute.split('=')[1]
                when (attribute[0]) {
                    's' -> slot = value.toInt()
                    't' -> itemStack =
                        if (value != "GOLDENHEAD") ItemStack(Material.getMaterial(value)!!)
                        else KitUtils.goldenHead(1)
                    'd' -> {
                        val itemMeta = itemStack?.itemMeta
                        if (itemMeta is Damageable) {
                            itemMeta.damage = value.toInt()
                        }
                        itemStack?.itemMeta = itemMeta
                    }
                    'a' -> itemStack?.amount = value.toInt()
                    'e' -> {
                        val enchantmentName = NamespacedKey(value.split(':')[0], value.split(':')[1])
                        val enchantmentLevel = value.split(':')[2].toInt()
                        val enchantment = Enchantment.getByKey(enchantmentName)
                        if (enchantment != null) {
                            itemStack?.addUnsafeEnchantment(enchantment, enchantmentLevel)
                        }
                    }
                    'n' -> itemStack?.itemMeta?.setDisplayName(value)
                    'p' -> {
                        val itemMeta = itemStack?.itemMeta as PotionMeta
                        val potionDataAttributes = value.split(':')
                        val potionType = PotionType.valueOf(potionDataAttributes[0])
                        val isExtended = potionDataAttributes[1].toBoolean()
                        val isUpgraded = potionDataAttributes[2].toBoolean()
                        itemMeta.basePotionData = PotionData(potionType, isExtended, isUpgraded)
                        itemStack?.itemMeta = itemMeta
                    }
                }
            }
            inventoryContents[slot] = itemStack ?: ItemStack(Material.AIR)
        }

        return inventoryContents
    }
}