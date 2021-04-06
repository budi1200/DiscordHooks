package si.budimir.discordHooks

import litebans.api.Database
import litebans.api.Entry
import litebans.api.Events
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException


class LitebansListener : Events.Listener() {
    private val plugin = DiscordHooksMain.instance

    override fun entryAdded(entry: Entry) {
        DiscordHooksMain.instance.logger.info("ENTRY ADDED")

        getPlayerName(entry.uuid) { result ->
            WebHookHandlerLegacy().send(
                entry.type,
                entry.id.toInt(),
                result,
                entry.reason,
                entry.executorName,
                entry.durationString,
                entry.isIpban
            )
        }
    }

    private fun getPlayerName(uuid: String?, callback: (String) -> Unit) {
        var playerName = ""
        plugin.proxy.scheduler.runAsync(plugin) {
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
        }
    }
}