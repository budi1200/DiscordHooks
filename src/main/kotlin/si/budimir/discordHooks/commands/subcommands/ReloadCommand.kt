package si.budimir.discordHooks.commands.subcommands

import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import si.budimir.discordHooks.DiscordHooksMain
import si.budimir.discordHooks.commands.SubCommandBase
import si.budimir.discordHooks.util.MessageHelper
import si.budimir.discordHooks.util.Permissions

class ReloadCommand: SubCommandBase {
    override fun execute(sender: CommandSender, args: Array<String>) {
        if (!sender.hasPermission(Permissions.RELOAD.perm))
            MessageHelper().sendWithPrefix(sender as ProxiedPlayer, "No perm!")
        if(DiscordHooksMain.instance.mainConfig.reloadConfig()){
            MessageHelper().sendWithPrefix(sender as ProxiedPlayer, "Plugin Reloaded!")
        }else{
            MessageHelper().sendWithPrefix(sender as ProxiedPlayer, "Failed to reload plugin!")
        }
    }

    override fun getPermission(): String {
        return Permissions.RELOAD.perm
    }

    override fun getDesc(): String {
        return "Reloads the plugin"
    }
}