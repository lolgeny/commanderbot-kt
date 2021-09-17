import dev.kord.core.Kord
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import kotlinx.coroutines.delay

suspend fun main(args: Array<String>) {
    if (args.size != 1) println("Expected usage: commanderbot <token>")
    val client = Kord(args[0])
    val pingPong = ReactionEmoji.Unicode("\uD83C\uDFD3")
    client.on<MessageCreateEvent> {
        if (message.content != "!ping") return@on

        val response = message.channel.createMessage("Pong!")
        response.addReaction(pingPong)

        delay(5000)
        message.delete()
        response.delete()
    }
    client.login()
}