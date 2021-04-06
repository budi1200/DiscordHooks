package si.budimir.discordHooks.config

import si.budimir.discordHooks.DiscordHooksMain
import si.budimir.discordHooks.util.ColorFactory

class MainConfig(private val plugin: DiscordHooksMain) : ConfigBase(plugin, "config.yml") {
    fun getString(key: String): String {
        var value: String? = getConfig()!!.getString(key)

        if (value == null) {
            plugin.logger.warning("Missing config value for $key")
            value = ""
        }

        return ColorFactory().replaceColors(value)
    }

    fun getList(key: String): MutableList<String> {
        return getConfig()!!.getStringList(key)
    }
}