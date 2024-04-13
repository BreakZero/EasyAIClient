package org.easy.ai.domain

import org.easy.ai.data.di.ModelPlatformQualifier
import org.easy.ai.data.repository.chat.AiModelChatRepository
import org.easy.ai.model.ModelPlatform
import javax.inject.Inject

internal class GetExactlyChatModelUseCase @Inject constructor(
    @ModelPlatformQualifier(ModelPlatform.GEMINI) private val geminiChatRepository: AiModelChatRepository,
    @ModelPlatformQualifier(ModelPlatform.CHAT_GPT) private val gptChatRepository: AiModelChatRepository
) {
    operator fun invoke(modelPlatform: ModelPlatform): AiModelChatRepository {
        return when(modelPlatform) {
            ModelPlatform.GEMINI -> geminiChatRepository
            ModelPlatform.CHAT_GPT -> gptChatRepository
        }
    }
}