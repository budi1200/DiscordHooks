package si.budimir.discordHooks.listeners

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import net.luckperms.api.LuckPerms
import net.luckperms.api.event.EventBus
import net.luckperms.api.event.log.LogNotifyEvent
import net.luckperms.api.event.log.LogReceiveEvent
import si.budimir.discordHooks.DiscordHooksMain
import si.budimir.discordHooks.WebHookHandler
import si.budimir.discordHooks.util.*
import java.util.concurrent.TimeUnit


class LuckpermsNodeChangeListener(private val plugin: DiscordHooksMain, private val luckperms: LuckPerms) {
    private var arrlist: MutableList<EmbedContent> = mutableListOf()
    private var isTimerRunning = false

    fun register() {
        val eventBus: EventBus = this.luckperms.eventBus
        eventBus.subscribe(plugin, LogNotifyEvent::class.java, this::onLog)
        eventBus.subscribe(plugin, LogReceiveEvent::class.java, this::onMessage)
    }

    private fun addEmbedEntry(type: String, sender: String, target: String, desc: String, timestamp: String) {
        val author = Author(type)
        val thumbnail = Thumbnail("https://raw.githubusercontent.com/lucko/LuckPermsWeb/master/public/logo.png")
        val footer = Footer("https://minotar.net/helm/${sender.split("@")[0]}/40.png", "Issuer: $sender")
        val main = EmbedContent("Detected a permissions change for: **${target}** ```${desc}```", 9756419, timestamp, footer, thumbnail, author)
        arrlist.add(main)

        if (!isTimerRunning) {
            isTimerRunning = true
            timerThing()
        }
    }

    private  fun onLog(e: LogNotifyEvent) {
        if (e.isCancelled) return

        addEmbedEntry(e.entry.target.type.name, e.entry.source.name, e.entry.target.name, e.entry.description, e.entry.timestamp.toString())
    }

    private fun onMessage(e: LogReceiveEvent) {
        addEmbedEntry(e.entry.target.type.name, e.entry.source.name, e.entry.target.name, e.entry.description, e.entry.timestamp.toString())
    }

    private fun timerThing() {
        plugin.proxy.scheduler.schedule(plugin, {
                                                val a = arrlist.take(8).toMutableList()
                                                arrlist = arrlist.drop(a.size).toMutableList()
                                                sendEmbed(a)

                                                if (arrlist.isNotEmpty()) {
                                                    timerThing()
                                                } else {
                                                    isTimerRunning = false
                                                }
        }, 3, TimeUnit.SECONDS)
    }

    private fun sendEmbed(arr: MutableList<EmbedContent>) {
        val embed = Embed(arr)

        val json = Json.encodeToJsonElement(embed)
        WebHookHandler.send(json.toString(), plugin.mainConfig.getString("webhooks.luckperms"))
    }
}