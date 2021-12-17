package si.budimir.discordHooks.commands.subcommands

import com.velocitypowered.api.command.SimpleCommand
import si.budimir.discordHooks.DiscordHooksMain
import si.budimir.discordHooks.commands.SubCommandBase
import si.budimir.discordHooks.util.MessageHelper
import si.budimir.discordHooks.util.Permissions

class ReloadCommand: SubCommandBase {

    override fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()

        if(DiscordHooksMain.instance.mainConfigObj.reloadConfig()){
            MessageHelper.reloadPrefix()
            MessageHelper.sendMessage(sender, "Plugin Reloaded!")
        }else{
            MessageHelper.sendMessage(sender, "Failed to reload plugin!")
        }
    }

    override fun suggestAsync(invocation: SimpleCommand.Invocation): MutableList<String> {
        return mutableListOf()
    }

    override fun getPermission(): String {
        return Permissions.RELOAD.perm
    }

    override fun getDesc(): String {
        return "Reloads the plugin"
    }

    override fun canConsoleUse(): Boolean {
        return true
    }
}