package si.budimir.discordHooks.util

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ProxyServer
import java.lang.StringBuilder

class ColorFactory {

    fun replaceColors(msg: String): String {
        val serverVer = ProxyServer.getInstance().version
        var newMsg = msg

        if(serverVer.contains("1.16") || serverVer.contains("1.17")){
            for (i in msg.indices) {
                if (msg.length - i > 8) {
                    val tempString = msg.substring(i, i + 8)
                    if (tempString.startsWith("&#")) {
                        val tempChars = tempString.replaceFirst("&#".toRegex(), "").toCharArray()
                        val rgbColor = StringBuilder()
                        rgbColor.append("§x")
                        for (tempChar in tempChars) {
                            rgbColor.append("§").append(tempChar)
                        }
                        newMsg = msg.replace(tempString.toRegex(), rgbColor.toString())
                    }
                }
            }
        }
        return ChatColor.translateAlternateColorCodes('&', newMsg)
    }

    fun getStrippedMessage(msg: String): String {
        return ChatColor.stripColor(msg)
    }
}