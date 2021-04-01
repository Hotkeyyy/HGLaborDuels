
package de.hglabor.plugins.duels.pvpbots
import me.libraryaddict.disguise.DisguiseAPI
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise
import net.minecraft.server.v1_16_R3.*

import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack

import org.bukkit.entity.Zombie

import org.bukkit.craftbukkit.v1_16_R3.CraftWorld

import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.inventory.ItemStack
import kotlin.random.Random


class PvPBot(world: World, botName: String?) : EntityZombie((world as CraftWorld).handle) {
    init {
        val random = Random
        getWorld().addEntity(this)
        val zombie = bukkitEntity as Zombie
        zombie.removeWhenFarAway = false
        zombie.canPickupItems = true
        val health: Int = random.nextInt(250 - 50) + 50
        zombie.equipment?.setItemInMainHand(ItemStack(Material.STONE_SWORD))
        zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.35
        zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = health.toDouble()
        zombie.getAttribute(Attribute.GENERIC_FOLLOW_RANGE)?.baseValue = 25.0
        zombie.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 2.0
        zombie.health = health.toDouble()
        val chatComponentText = ChatComponentText(botName)
        this.customName = chatComponentText
        this.customNameVisible = true
        //this.goalSelector.a(0, PathfinderMoveToPlayer(player, this));
        goalSelector.a(0, PathfinderGoalMeleeAttack(this, 1.0, true))
        goalSelector.a(1, PathfinderGoalFloat(this))
        goalSelector.a(5, PathfinderGoalNearestAttackableTarget(this, EntityLiving::class.java, true))
        // this.goalSelector.a(2, new PathfinderFindTarget(this, true));
        val bukkitItem = ItemStack(Material.BOWL, 32)
        this.a(CraftItemStack.asNMSCopy(bukkitItem))
        val playerDisguise = PlayerDisguise(botName)
        playerDisguise.isDisplayedInTab = true
        playerDisguise.isNameVisible = true
        DisguiseAPI.disguiseEntity(bukkitEntity, playerDisguise)
    }

    override fun getSoundHurt(damagesource: DamageSource): SoundEffect? {
        return SoundEffects.ENTITY_PLAYER_HURT
    }

    override fun getSoundDeath(): SoundEffect? {
        return SoundEffects.ENTITY_PLAYER_DEATH
    }

    override fun getSoundStep(): SoundEffect {
        return SoundEffects.BLOCK_GRASS_STEP
    }
}
