package si.budimir.discordHooks

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import litebans.api.Database
import litebans.api.Entry
import litebans.api.Events
import si.budimir.discordHooks.util.*
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException


class LitebansListener : Events.Listener() {
    private val plugin = DiscordHooksMain.instance

    override fun entryAdded(entry: Entry) {
        getPlayerName(entry.uuid) { result ->
            val embed = buildEmbed(
                entry.type,
                entry.id.toInt(),
                result,
                entry.reason,
                entry.executorName,
                entry.durationString,
                entry.isIpban
            )

            sendEmbed(embed)
        }
    }

    private fun getPlayerName(uuid: String?, callback: (String) -> Unit) {
        var playerName = ""
        plugin.server.scheduler.buildTask(plugin) {
            if (uuid == null) {
                callback("")
            }

            val query = "SELECT name FROM {history} WHERE uuid=? ORDER BY date DESC LIMIT 1"
            var st: PreparedStatement? = null
            var rs: ResultSet? = null

            try {
                st = Database.get().prepareStatement(query)
                st.setString(1, uuid)

                rs = st.executeQuery()
                if (rs.next()) {
                    playerName = rs.getString("name")
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            } finally {
                st?.close()
                rs?.close()
            }
            callback(playerName)
        }.schedule()
    }

    private fun buildEmbed(
        type: String,
        id: Int,
        player: String?,
        reason: String,
        issuer: String?,
        duration: String,
        isIpBan: Boolean
    ): EmbedContent {
        val ip: String = if (isIpBan) "IP " else ""

        val title = ip + type.lowercase().replaceFirstChar { it.uppercase() }
        val thumbnail = Thumbnail("https://crafthead.net/helm/${player}/40.png")
        val footer = Footer("https://crafthead.net/helm/${issuer}/40.png", "Issuer: $issuer • ID #${id}")
        val fields = arrayListOf<Field>()

        fields.add(Field("Player", player ?: "Error", true))
        fields.add(Field("Duration", duration, true))
        fields.add(Field("Reason", reason.replace("§.".toRegex(), "")))

        return EmbedContent(title, null, 12597820, null, footer, thumbnail, null, fields)
    }

    private fun sendEmbed(entry: EmbedContent) {
        val embed = Embed(arrayListOf(entry))

        val json = Json.encodeToJsonElement(embed)
        WebHookHandler.send(json.toString(), plugin.mainConfig.litebansWebhook)
    }
}