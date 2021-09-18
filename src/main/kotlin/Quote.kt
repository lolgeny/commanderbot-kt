
import dev.kord.common.Color
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.cache.data.ApplicationInteractionData
import dev.kord.core.entity.channel.MessageChannel
import dev.kord.core.entity.interaction.Interaction
import dev.kord.core.exception.EntityNotFoundException
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import dev.kord.rest.builder.interaction.boolean
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.message.create.allowedMentions
import dev.kord.rest.builder.message.create.embed
import java.net.URI
import java.net.URISyntaxException

class Quote: SlashCommand("quote", "Quotes a message") {
    override fun ChatInputCreateBuilder.register() {
        string(
            "url",
            "The url of the message to quote"
        ) {
            this.required = true
        }
        boolean(
            "ping",
            "Whether to ping the author of the message"
        )
    }


    override suspend fun handle(interaction: Interaction, data: ApplicationInteractionData) {
        val url = data.string(0)!!
        val (msg, channelName) = try {
            val parsed = URI(url)
            val parts = parsed.path.split("/")
            val channelId = Snowflake(parts[3])
            val channel = interaction.kord.getChannelOf<MessageChannel>(channelId) ?: throw EntityNotFoundException("")
            val msgId = Snowflake(parts[4])
            Pair(channel.getMessage(msgId), channel.mention)
        } catch (e: Exception) {
            if (e is URISyntaxException || e is EntityNotFoundException) {
                interaction.respondEphemeral {
                    content = "Sorry, I couldn't parse your url - try again"
                }
                return
            } else throw e
        }
        val author = msg.author
        val authorMention = author?.mention
        val time = msg.timestamp.epochSeconds
        val content = msg.content
        interaction.respondPublic {
            this.content =
                "$authorMention in $channelName from <t:$time:R>:\n$url"
            this.allowedMentions {
                if (data.bool(1) == true && author != null) this.users.add(author.id)
            }
            this.embed {
                color = Color(0x00aced)
                if (author != null) {
                    this.author {
                        name = author.tag
                        icon = author.avatar.url
                    }
                }
                description = content
            }
        }
    }
}