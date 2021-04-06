package si.budimir.discordHooks

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

class WebHookHandlerLegacy {
    fun send(type: String, id: Int, player: String?, reason: String, issuer: String?, duration: String, isIpBan: Boolean) {
        val ip: String = if (isIpBan) "IP" else ""

        val messageTemplate: String = "{\n" +
                "  \"embeds\": [{\n" +
                "    \"title\": \"%TYPE%\",\n" +
                "    \"color\": 12597820,\n" +
                "    \"thumbnail\": {\n" +
                "      \"url\": \"https://minotar.net/helm/%PLAYER%/40.png\"\n" +
                "    },\n" +
                "    \"footer\": {\n" +
                "      \"icon_url\": \"https://minotar.net/helm/%ISSUER%/40.png\",\n" +
                "      \"text\": \"Issuer: %ISSUER% • #%ID%\"\n" +
                "    },\n" +
                "    \"fields\": [\n" +
                "      {\n" +
                "        \"name\": \"Player\",\n" +
                "        \"value\": \"%PLAYER%\",\n" +
                "        \"inline\": true\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"Duration\",\n" +
                "        \"value\": \"%DURATION%\",\n" +
                "        \"inline\": true\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"Reason\",\n" +
                "        \"value\": \"%REASON%\",\n" +
                "        \"inline\": false\n" +
                "      }\n" +
                "    ]\n" +
                "  }]\n" +
                "}";

        val finalMessage: String =
            messageTemplate.replace("%TYPE%".toRegex(), "$ip ${type.toLowerCase().capitalize()}")
                .replace("%ID%".toRegex(), id.toString()).replace("%PLAYER%".toRegex(), player?: "")
                .replace("%REASON%".toRegex(), reason).replace("%ISSUER%".toRegex(), issuer?: "")
                .replace("%DURATION%".toRegex(), duration)

        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(DiscordHooksMain.instance.mainConfig.getString("webhooks.litebans")))
            .timeout(Duration.ofMinutes(1))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(finalMessage))
            .build()
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply { obj: HttpResponse<String?> -> obj.body() }
            .thenAccept { x: String? -> println(x) }
    }
}