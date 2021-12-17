package si.budimir.discordHooks.config;

import si.budimir.discordHooks.DiscordHooksMain

class MainConfig(plugin: DiscordHooksMain) : ConfigBase<MainConfigData>(plugin, "config.conf", MainConfigData::class.java)
