package de.hglabor.plugins.duels.events.listeners.duel

import net.axay.kspigot.event.listen
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.ItemFrame
import org.bukkit.event.player.PlayerInteractAtEntityEvent

object OnInteractAtEntity {
    fun enable() {
        listen<PlayerInteractAtEntityEvent> {
            if (it.rightClicked is ArmorStand || it.rightClicked is ItemFrame)
                it.isCancelled = true
        }
    }
}