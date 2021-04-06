package si.budimir.discordHooks.util

import kotlinx.serialization.Serializable

@Serializable
data class Embed(
    val embeds: MutableList<EmbedContent>
)

@Serializable
data class EmbedContent(
    val description: String,
    val color: Int,
    val timestamp: String,
    val footer: Footer,
    val thumbnail: Thumbnail,
    val author: Author,
)

@Serializable
data class Footer(
    val icon_url: String,
    val text: String
)

@Serializable
data class Thumbnail(
    val url: String
)

@Serializable
data class Author(
    val name: String
)

@Serializable
data class Fields(
    val fields: ArrayList<String>? = null
)