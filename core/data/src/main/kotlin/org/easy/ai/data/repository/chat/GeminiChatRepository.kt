package org.easy.ai.data.repository.chat

import com.google.ai.client.generativeai.type.InvalidStateException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.sync.Semaphore
import org.easy.ai.database.dao.ChatDao
import org.easy.ai.database.dao.MessageDao
import org.easy.ai.datastore.UserPreferencesDataSource
import org.easy.ai.model.EasyPrompt
import org.easy.ai.network.gemini.GeminiRestApi
import javax.inject.Inject

class GeminiChatRepository @Inject internal constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val chatDao: ChatDao,
    private val messageDao: MessageDao,
    private val geminiRestApi: GeminiRestApi
) : ChatRepository {
    private var lock = Semaphore(1)
    private val history: MutableList<EasyPrompt.TextPrompt> = ArrayList()
    override fun startChat() {
        TODO("Not yet implemented")
    }

    override fun sendMessage(prompt: EasyPrompt): Flow<String> {
        prompt.assertComesFromUser()
        attemptLock()
        return userPreferencesDataSource.userData.map {
            if (it.apiKey.isBlank()) throw IllegalStateException("api key is not setup.")
            if (it.isAutomaticSaveChat) {

            }
            geminiRestApi.generateContent(it.apiKey, prompt)
        }.onCompletion {
            lock.release()
        }
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