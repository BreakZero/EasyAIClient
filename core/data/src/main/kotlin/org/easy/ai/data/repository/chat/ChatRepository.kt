package org.easy.ai.data.repository.chat

import kotlinx.coroutines.flow.Flow
import org.easy.ai.model.EasyPrompt

interface ChatRepository {
    fun startChat()

    fun sendMessage(prompt: EasyPrompt): Flow<String>
}