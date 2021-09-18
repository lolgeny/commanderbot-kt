
import dev.kord.common.entity.InteractionType
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.interaction.InteractionCreateEvent
import dev.kord.core.on
import kotlinx.coroutines.flow.collect

val commands: List<SlashCommand> = listOf(
    Quote()
)

suspend fun main(args: Array<String>) {
    if (args.isEmpty()) println("Expected usage: commanderbot <token>")
    val client = Kord(args[0])
    client.on<ReadyEvent> {
        // TODO: cli
        // re-register commands in dev mode
        if (args.size > 1 && args[1] == "-r") {
            println("Registering commands...")
            val guildId = Snowflake(args[2].toLong())
            client.getGuildApplicationCommands(guildId).collect {
                it.delete()
            }
            for (command in commands) {
                command.registerCommand(client, guildId)
            }
        }
        println("Ready!")
    }
    client.on<InteractionCreateEvent> {
        println("Interaction received")
        if (interaction.type == InteractionType.ApplicationCommand) {
            for (command in commands) {
                command.interactionCreate(interaction)
            }
        }
    }
    client.login()
}