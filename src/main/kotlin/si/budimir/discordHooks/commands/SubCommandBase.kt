package si.budimir.discordHooks.commands

import net.md_5.bungee.api.CommandSender
import si.budimir.discordHooks.util.Permissions

interface SubCommandBase {
    fun execute(sender: CommandSender, args: Array<String>)

    fun onTabComplete(commandSender: CommandSender, args: Array<String>): List<String>{
        return emptyList()
    }

    fun getPermission(): String {
        return Permissions.NONE.perm
    }

    fun getDesc(): String
}