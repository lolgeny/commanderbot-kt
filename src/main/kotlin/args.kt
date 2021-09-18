/// Utilities for args
/// will rewrite in a better way to hook onto the builder

import dev.kord.common.entity.CommandArgument
import dev.kord.common.entity.optional.orEmpty
import dev.kord.core.cache.data.ApplicationInteractionData
import dev.kord.core.cache.data.OptionData


fun ApplicationInteractionData.arg(index: Int): OptionData? = options.orEmpty().getOrNull(index)

fun <R, T: CommandArgument<R?>> ApplicationInteractionData.value(index: Int): R? {
    val option = arg(index)
    return (option?.value?.value as? T)?.value
}

fun ApplicationInteractionData.string(index: Int): String? = value<String, CommandArgument.StringArgument>(index)
fun ApplicationInteractionData.bool(index: Int): Boolean? = value<Boolean, CommandArgument.BooleanArgument>(index)