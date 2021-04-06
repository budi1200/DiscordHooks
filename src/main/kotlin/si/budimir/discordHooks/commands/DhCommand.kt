package si.budimir.discordHooks.commands

import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.TabExecutor
import si.budimir.discordHooks.commands.subcommands.ReloadCommand
import si.budimir.discordHooks.util.MessageHelper
import si.budimir.discordHooks.util.Permissions

class DhCommand(name: String) : Command(name), TabExecutor {
    private val subCommands: MutableMap<String, SubCommandBase> = HashMap()
    private var subCommandsList: List<String> = emptyList()

    init {
         subCommands["reload"] = ReloadCommand()

        subCommandsList = subCommands.keys.toList()
    }

    override fun execute(sender: CommandSender, args: Array<String>) {
        val messenger = MessageHelper()

        if(args.isNotEmpty()) run {
            val sc: SubCommandBase = subCommands[args[0]] ?: return
            val reqPerm: String = sc.getPermission()

            if(reqPerm == Permissions.NONE.perm || sender.hasPermission(reqPerm)){
                sc.execute(sender, args)
            }else{
                messenger.sendMessage(sender as ProxiedPlayer, messenger.parsePlaceholders(true,"lang.missingPermissions", sender.name))
            }
        }
    }

    override fun onTabComplete(sender: CommandSender, args: Array<String>): List<String> {
        return when {
            args[0] == "" -> {
                subCommandsList
            }
            args.size == 1 -> {
                subCommandsList.filter { it.contains(args[0], ignoreCase = true) }
            }
            else -> {
                val sc: SubCommandBase = subCommands[args[0]] ?: return emptyList()
                sc.onTabComplete(sender, args)
            }
        }
    }
}