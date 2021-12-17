package si.budimir.discordHooks.util

import com.velocitypowered.api.command.CommandSource
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import si.budimir.discordHooks.DiscordHooksMain
import java.util.function.Function

abstract class MessageHelper {
    companion object {
        private lateinit var plugin: DiscordHooksMain
        private lateinit var pluginPrefix: Component

        fun load(plugin: DiscordHooksMain) {
            this.plugin = plugin
            pluginPrefix = parseString(plugin.mainConfig.pluginPrefix)
        }

        val resolver = Function<String, ComponentLike?> { name: String ->
            null
        }

        private val miniMessage = MiniMessage.builder().markdown().placeholderResolver(resolver).build()

        fun reloadPrefix(){
            pluginPrefix = parseString(plugin.mainConfig.pluginPrefix)
        }

        fun sendMessage(commandSource: CommandSource, rawMessage: String, placeholders: MutableMap<String, String> = hashMapOf(), prefix: Boolean = true) {
            var output = Component.text("")

            // Add prefix if required
            if (prefix)
                output = output.append(pluginPrefix)

            output = output.append(parseString(rawMessage, placeholders))

            commandSource.sendMessage(output)
        }

        fun parseString(key: String, placeholders: Map<String, String> = hashMapOf()): Component {
            return Component
                .text("")
                .decoration(TextDecoration.ITALIC, false)
                .append(
                    miniMessage.parse(key, placeholders)
                )
        }

        fun getParsedString(key: String, placeholders: Map<String, String> = hashMapOf()): Component {
            return Component
                .text("")
                .decoration(TextDecoration.ITALIC, false)
                .append(
                    miniMessage.parse(key, placeholders)
                )
        }

        fun getNonMarkdownParsed(key: String, placeholders: Map<String, String> = hashMapOf()): Component {
            return miniMessage.parse(key, placeholders)
        }
    }
}
