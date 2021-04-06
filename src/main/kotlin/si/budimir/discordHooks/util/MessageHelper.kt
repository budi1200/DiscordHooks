package si.budimir.discordHooks.util

import net.md_5.bungee.api.chat.*
import net.md_5.bungee.api.chat.hover.content.Text
import net.md_5.bungee.api.connection.ProxiedPlayer
import si.budimir.discordHooks.DiscordHooksMain
import si.budimir.discordHooks.config.MainConfig

class MessageHelper {
    private val plugin: DiscordHooksMain = DiscordHooksMain.instance
    private val config: MainConfig = plugin.mainConfig
    private val pluginPrefix = config.getString("pluginPrefix")

    private fun getPrefix(): TextComponent {
        return TextComponent(pluginPrefix)
    }

    fun sendWithPrefix(player: ProxiedPlayer, msg: String){
        val builder = ComponentBuilder()

        builder.append(getPrefix())
        builder.append(msg)

        player.sendMessage(*builder.create())
    }

    fun sendMessage(player: ProxiedPlayer, msg: Array<BaseComponent>){
        player.sendMessage(*msg)
    }

    fun parsePlaceholders(addPrefix: Boolean, configString: String, player: String?, code: String? = null, discordUser: String? = null): Array<BaseComponent> {
        val builder = ComponentBuilder()
        if (addPrefix)
            builder.append(getPrefix())

        val message: String = config.getString(configString)
        val messageArr: List<String> = message.split(Regex("(?<=[%])|(?=[%])"))
        var tmpString: String
        val newCode: String? = code
        val messageSize = messageArr.size
        var i = 0

        while(i < messageSize) {
            val item = messageArr[i]
            val secondItem = if (i + 1 < messageArr.size) messageArr[i + 1] else "null"
            val thirdItem = if (i + 2 < messageArr.size) messageArr[i + 2] else "null"

            if (item == "%" && thirdItem == "%") {
                if (secondItem == "discord") {
                    tmpString = config.getString("discordInvite")

                    builder.append(TextComponent.fromLegacyText(tmpString))
                    builder.append("").retain(ComponentBuilder.FormatRetention.FORMATTING)
                    i+=3
                } else if (secondItem == "code" && newCode != null){
                    tmpString = config.getString("lang.clickToCopy")

                    builder.append(TextComponent.fromLegacyText(newCode)).event(ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, code)).event(
                        HoverEvent(HoverEvent.Action.SHOW_TEXT, Text(tmpString))
                    )
                    builder.append("").retain(ComponentBuilder.FormatRetention.FORMATTING)
                    i+=3
                } else if (secondItem == "player" && player != null) {
                    tmpString = player

                    builder.append(TextComponent.fromLegacyText(tmpString))
                    i+=3
                } else if (secondItem == "discordUser" && discordUser != null) {
                    builder.append(TextComponent.fromLegacyText(discordUser))

                    i+=3
                } else {
                    builder.append(TextComponent.fromLegacyText(item))
                    i+=1
                }
            } else {
                builder.append(TextComponent.fromLegacyText(item))
                i+=1
            }
        }
        return builder.create()
    }
}
