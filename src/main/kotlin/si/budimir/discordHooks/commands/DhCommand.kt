package si.budimir.discordHooks.commands

import com.velocitypowered.api.command.SimpleCommand
import si.budimir.discordHooks.DiscordHooksMain
import si.budimir.discordHooks.commands.subcommands.ReloadCommand
import si.budimir.discordHooks.util.MessageHelper
import si.budimir.discordHooks.util.Permissions
import java.util.concurrent.CompletableFuture

class DhCommand : SimpleCommand {
    private val plugin = DiscordHooksMain.instance
    private val subCommands: MutableMap<String, SubCommandBase> = HashMap()
    private var subCommandsList: List<String> = emptyList()

    init {
         subCommands["reload"] = ReloadCommand()

        subCommandsList = subCommands.keys.toList()
    }

    override fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()
        val args = invocation.arguments()

        if(args.isNotEmpty()) run {
            val sc: SubCommandBase = subCommands[args[0]] ?: return
            val reqPerm: String = sc.getPermission()

            if(reqPerm == Permissions.NONE.perm || sender.hasPermission(reqPerm)){
                sc.execute(invocation)
            }else{
                MessageHelper.sendMessage(sender, plugin.mainConfig.missingPermissions)
            }
        }
    }

    override fun suggestAsync(invocation: SimpleCommand.Invocation): CompletableFuture<MutableList<String>> {
        val args = invocation.arguments()

        return CompletableFuture.supplyAsync {
            return@supplyAsync when {
                args.isEmpty() -> {
                    subCommandsList.toMutableList()
                }
                args.size == 1 -> {
                    subCommandsList.filter { it.contains(args[0], ignoreCase = true) }.toMutableList()
                }
                else -> {
                    val sc: SubCommandBase = subCommands[args[0]] ?: return@supplyAsync mutableListOf()
                    sc.suggestAsync(invocation)
                }
            }
        }
    }
}