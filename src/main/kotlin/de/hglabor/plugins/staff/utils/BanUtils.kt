package de.hglabor.plugins.staff.utils

import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import org.bukkit.BanList
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*


object BanUtils {
    fun banPlayer(rulebreakerName: String?, banner: Player, reason: String) {
        Bukkit.getBanList(BanList.Type.NAME).addBan(rulebreakerName!!, reason, null, banner.name)
        val rulebreaker: Player? = Bukkit.getPlayer(rulebreakerName)
        if (rulebreaker != null)
            if (rulebreaker.localization("de"))
                rulebreaker.kickPlayer(
                    "§3§lHGLabor.de\n\n" +
                            "§7Du wurdest §cgebannt§7.\n\n" +
                            "§7Gebannt von §8» §b${banner.name}\n" +
                            "§7Grund §8» §c$reason\n" +
                            "§7Dauer §8» §9Permanent\n\n" +
                            "§7Falls du denkst, dass dies ein §n§fFehlbann§7 ist, melde dich bitte auf dem Discord.\n" +
                            "§7Discord §8» §3https://discord.gg/pamTgCPnD7")
            else
                rulebreaker.kickPlayer(
                    "§3§lHGLabor.de\n\n" +
                            "§7You have been §cbanned§7.\n\n" +
                            "§7Banned by §8» §b${banner.name}\n" +
                            "§7Reason §8» §c$reason\n" +
                            "§7Expires in §8» §9Never\n\n" +
                            "§7If you believe you were §n§ffalsely banned§7, you may appeal on our discord\n" +
                            "§7Discord §8» §3https://discord.gg/pamTgCPnD7")
    }

    fun tempbanPlayer(rulebreakerName: String?, banner: Player, amount: Int, timeUnit: String, reason: String) {
        val timeunit = timeUnit[0]
        val unbanDate: Date = getDate(amount, timeunit)
        var timeunitString: String? = ""
        Bukkit.getBanList(BanList.Type.NAME).addBan((rulebreakerName)!!, reason, unbanDate, banner.name)
        val rulebreaker: Player? = Bukkit.getPlayer((rulebreakerName))
        if (rulebreaker != null)
            if (rulebreaker.getLocale().toLowerCase().contains("de")) {
                timeunitString = when (timeunit) {
                    'h' -> "Stunden"
                    'd' -> "Tage"
                    'w' -> "Wochen"
                    'm' -> "Monate"
                    else -> null
                }
                rulebreaker.kickPlayer(
                    "§3§lHGLabor.de\n\n" +
                            "§7Du wurdest §cgebannt§7.\n\n" +
                            "§7Gebannt von §8» §b${banner.name}\n" +
                            "§7Grund §8» §c$reason\n" +
                            "§7Dauer §8» §9$amount $timeunitString\n\n" +
                            "§7Falls du denkst, dass dies ein §n§fFehlbann§7 ist, melde dich bitte auf dem Discord.\n" +
                            "§7Discord §8» §3https://discord.gg/pamTgCPnD7")
            } else {
                when (timeunit) {
                    'h' -> timeunitString = "Hours"
                    'd' -> timeunitString = "Days"
                    'w' -> timeunitString = "Weeks"
                    'm' -> timeunitString = "Months"
                    else -> { }
                }
                rulebreaker.kickPlayer(
                    "§3§lHGLabor.de\n\n" +
                            "§7You have been §cbanned§7.\n\n" +
                            "§7Banned by §8» §b${banner.name}\n" +
                            "§7Reason §8» §c$reason\n" +
                            "§7Expires in §8» §9$amount $timeunitString\n\n" +
                            "§7If you believe you were §n§ffalsely banned§7, you may appeal on our discord\n" +
                            "§7Discord §8» §3https://discord.gg/pamTgCPnD7")
            }
    }

    private fun getDate(amount: Int, timeunit: Char?): Date {
        val cal = Calendar.getInstance()
        when (timeunit) {
            'h' -> cal.add(Calendar.HOUR_OF_DAY, amount)
            'd' -> cal.add(Calendar.DAY_OF_MONTH, amount)
            'w' -> cal.add(Calendar.WEEK_OF_MONTH, amount)
            'm' -> cal.add(Calendar.MONTH, amount)
        }
        return cal.time
    }
}