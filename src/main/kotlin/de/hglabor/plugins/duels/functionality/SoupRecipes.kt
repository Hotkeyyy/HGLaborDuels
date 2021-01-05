package de.hglabor.plugins.duels.functionality

import net.axay.kspigot.chat.KColors
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapelessRecipe

object SoupRecipes {

    fun register() {
        val cocosoup = ItemStack(Material.MUSHROOM_STEW, 1)
        val cocoa = cocosoup.itemMeta
        cocoa.setDisplayName("Chocolate Milk")
        cocosoup.setItemMeta(cocoa)
        val cocoamilk = ShapelessRecipe(cocosoup)
        cocoamilk.addIngredient(1, Material.COCOA_BEANS)
        cocoamilk.addIngredient(1, Material.BOWL)
        Bukkit.addRecipe(cocoamilk as Recipe)

        val cactisoup = ItemStack(Material.MUSHROOM_STEW, 1)
        val cacti = cactisoup.itemMeta
        cacti.setDisplayName("Cactus Stew")
        cactisoup.setItemMeta(cacti)
        val cactijuice = ShapelessRecipe(cactisoup)
        cactijuice.addIngredient(1, Material.CACTUS)
        cactijuice.addIngredient(1, Material.BOWL)
        Bukkit.addRecipe(cactijuice as Recipe)
    }
}