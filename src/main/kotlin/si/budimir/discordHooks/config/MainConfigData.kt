package si.budimir.discordHooks.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class MainConfigData(
    val pluginPrefix: String = "<bold>Hooks » <reset>",
    val missingPermissions: String = "&cYou don't have the required permissions!",
    val litebansWebhook: String = "",
    val luckpermsWebhook: String = ""
)