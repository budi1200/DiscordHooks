package si.budimir.discordHooks

import litebans.api.Events
import net.luckperms.api.LuckPermsProvider
import net.md_5.bungee.api.plugin.Plugin
import si.budimir.discordHooks.commands.DhCommand
import si.budimir.discordHooks.config.MainConfig
import si.budimir.discordHooks.listeners.LuckpermsNodeChangeListener


class DiscordHooksMain: Plugin() {
    lateinit var mainConfig: MainConfig
    lateinit var dhCommand: DhCommand

    companion object {
        lateinit var instance: DiscordHooksMain
    }

    override fun onEnable() {
        instance = this

        // Load config
        mainConfig = MainConfig(instance)

        // Commands
        dhCommand = DhCommand("dh")
        instance.proxy.pluginManager.registerCommand(this, dhCommand)

        // Litebans Integration
        Events.get().register(LitebansListener())

        // Luckperms integration
        try {
            val api = LuckPermsProvider.get()

            LuckpermsNodeChangeListener(this, api).register()
        } catch (e: Exception) {
            logger.severe("Error hooking into LuckPerms!")
        }

        logger.info("DiscordHooks Loaded!")
    }

    override fun onDisable() {
        super.onDisable()
    }
}