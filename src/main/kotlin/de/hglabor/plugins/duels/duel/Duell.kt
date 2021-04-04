package de.hglabor.plugins.duels.duel

import de.hglabor.plugins.duels.kits.AbstractKit
import de.hglabor.plugins.duels.team.Team
import org.bukkit.block.Block
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import java.io.File

class Duell(teamOne: Team, teamTwo: Team) : AbstractDuel(teamOne, teamTwo) {
