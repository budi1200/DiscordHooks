package si.budimir.discordHooks

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class WebHookHandler {
    companion object {
        var client: OkHttpClient = OkHttpClient()

        fun send(data: String, url: String) {
            val body: RequestBody = data.toRequestBody("application/json".toMediaType())
            val request: Request = Request.Builder().url(url).post(body).build()
            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    DiscordHooksMain.instance.logger.info("Failed to send Luckperms Data")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.code != 204 && response.code != 200) {
                        DiscordHooksMain.instance.logger.info("Error sending LP event ${response.code}")
                    }
                }
            })
        }
    }
}