package org.easy.ai.data.repository.chat

import com.google.ai.client.generativeai.type.InvalidStateException
import kotlinx.coroutines.sync.Semaphore
import org.easy.ai.database.dao.ChatDao
import org.easy.ai.database.dao.MessageDao
import org.easy.ai.model.EasyPrompt
import org.easy.ai.network.gemini.GeminiRestApi
import javax.inject.Inject

class GeminiChatRepository @Inject internal constructor(
    private val chatDao: ChatDao,
    private val messageDao: MessageDao,
    private val geminiRestApi: GeminiRestApi
) : AiModelChatRepository {
    private var lock = Semaphore(1)
    private val history: MutableList<EasyPrompt> = ArrayList()

    override fun startChat(history: List<EasyPrompt>) {
        this.history.clear()
        this.history.addAll(history)
    }

    override suspend fun sendMessage(apiKey: String, prompt: EasyPrompt): String {
        prompt.assertComesFromUser()
        attemptLock()
        val response = try {
            geminiRestApi.generateContent(apiKey, prompt)
        } finally {
            lock.release()
        }
        return response
    }

    private fun EasyPrompt.assertComesFromUser() {
        if (this !is EasyPrompt.TextPrompt) {
            throw InvalidStateException("Chat prompts should come from the 'user' role.")
        } else {
            if (this.role != "user") {
                throw InvalidStateException("Chat prompts should come from the 'user' role.")
            }
        }
    }

    private fun attemptLock() {
        if (!lock.tryAcquire()) {
            throw InvalidStateException(
                "This chat instance currently has an ongoing request, please wait for it to complete " +
                        "before sending more messages"
            )
        }
    }
}