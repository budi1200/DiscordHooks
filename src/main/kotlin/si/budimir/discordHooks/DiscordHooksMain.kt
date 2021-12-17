package si.budimir.discordHooks

import com.google.inject.Inject
import com.velocitypowered.api.command.CommandManager
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import net.luckperms.api.LuckPermsProvider
import org.slf4j.Logger
import si.budimir.discordHooks.commands.DhCommand
import si.budimir.discordHooks.config.MainConfigData
import si.budimir.discordHooks.listeners.LuckpermsNodeChangeListener
import si.budimir.discordHooks.util.Constants
import si.budimir.discordHooks.util.MessageHelper
import si.budimir.discordHooks.config.MainConfig
import java.nio.file.Path
import java.time.Duration
import java.time.Instant

@Plugin(
    id = "discordhooks",
    name = "DiscordHooks",
    version = Constants.VERSION,
    description = "DiscordHooks",
    authors = ["budi1200"],
    dependencies = [
        Dependency(id = "luckperms"),
//        Dependency(id = "litebans")
    ]
)
class DiscordHooksMain {
    private val startTime = Instant.now()

    @Inject
    lateinit var server: ProxyServer

    @Inject
    lateinit var logger: Logger

    @Inject
    @DataDirectory
    lateinit var dataDirectory: Path

    @Inject
    lateinit var commandManager: CommandManager

    // Configs
    lateinit var mainConfigObj: MainConfig

    lateinit var mainConfig: MainConfigData

    lateinit var dhCommand: DhCommand

    companion object {
        lateinit var instance: DiscordHooksMain
    }

    init {
        instance = this
    }

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent?) {
        // Load config
        mainConfigObj = MainConfig(instance)
        mainConfig = mainConfigObj.getConfig()

        // Commands
        dhCommand = DhCommand()

        commandManager.register("dh", dhCommand)

        // Litebans Integration
//        Events.get().register(LitebansListener())

        // Luckperms integration
        try {
            val api = LuckPermsProvider.get()

            LuckpermsNodeChangeListener(this, api).register()
        } catch (e: Exception) {
            logger.error("Error hooking into LuckPerms!")
        }

        MessageHelper.load(this)

        val loadTime = Duration.between(startTime, Instant.now())
        logger.info("DiscordHooks Loaded (took ${loadTime.toMillis()}ms)")
    }
}