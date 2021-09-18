
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.cache.data.ApplicationInteractionData
import dev.kord.core.entity.interaction.Interaction
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import kotlinx.coroutines.flow.first

abstract class SlashCommand(private val name: String, private val description: String) {
    abstract fun ChatInputCreateBuilder.register()
    abstract suspend fun handle(interaction: Interaction, data: ApplicationInteractionData)

    private lateinit var id: Snowflake
    suspend fun registerCommand(client: Kord, guildId: Snowflake) {
        id = client.createGuildApplicationCommands(guildId) {
            input(name, description) {this.register()}
        }.first().id
    }
    suspend fun interactionCreate(interaction: Interaction) {
        val data = interaction.data.data
        if (data.id.value == id) {
            println("Handling command $name")
            handle(interaction, data)
        }
    }
}