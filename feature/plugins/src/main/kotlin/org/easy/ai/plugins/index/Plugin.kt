package org.easy.ai.plugins.index

import javax.annotation.concurrent.Immutable
import org.easy.ai.model.AiModel

@Immutable
data class Plugin(
    val supportedAis: List<AiModel>,
    val name: String
)

internal fun supportedPlugins(): List<Plugin> {
    return listOf(
        Plugin(
            supportedAis = listOf(AiModel.GEMINI),
            name = "Multi Modal"
        )
    )
}