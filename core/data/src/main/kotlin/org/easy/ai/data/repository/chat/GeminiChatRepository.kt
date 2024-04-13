package org.easy.ai.data.repository.chat

import com.google.ai.client.generativeai.type.InvalidStateException
import kotlinx.coroutines.sync.Semaphore
import org.easy.ai.database.dao.ChatDao
import org.easy.ai.database.dao.MessageDao
import org.easy.ai.database.entities.ChatEntity
import org.easy.ai.database.entities.MessageEntity
import org.easy.ai.model.EasyPrompt
import org.easy.ai.model.ModelPlatform
import org.easy.ai.model.Participant
import org.easy.ai.network.gemini.GeminiRestApi
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiChatRepository @Inject internal constructor(
    private val chatDao: ChatDao,
    private val messageDao: MessageDao,
    private val geminiRestApi: GeminiRestApi
) : AiModelChatRepository {
    private var lock = Semaphore(1)
    private val history: MutableList<EasyPrompt> = ArrayList()
    private var chatId: String? = null

    override suspend fun startChat(chatId: String?) {
        attemptLock()
        this.chatId = chatId
        val history = chatId?.let {
            messageDao.getChatHistoryByChatId(it).messages.map {
                EasyPrompt.TextPrompt(role = it.participant.name, it.content)
            }
        } ?: emptyList()

        this.history.clear()
        this.history.addAll(history)

        lock.release()
    }

    override suspend fun sendMessage(apiKey: String, prompt: EasyPrompt): String {
        prompt.assertComesFromUser()
        attemptLock()
        val text = (prompt as EasyPrompt.TextPrompt).text
        if (history.isEmpty() && chatId == null) {
            // saving chat
            val chatName = genChatName(text)
            this.chatId = UUID.randomUUID().toString()
            chatDao.insert(
                ChatEntity(
                    chatId = this.chatId!!,
                    chatName = chatName,
                    model = ModelPlatform.GEMINI,
                    createAt = System.currentTimeMillis()
                )
            )
        }

        val response = try {
            geminiRestApi.generateContent(
                apiKey,
                *history.filter { it.filterErrorType() }.toTypedArray(),
                prompt
            ).also { res ->
                // only success will save into local
                // saving message
                messageDao.insert(
                    MessageEntity(
                        participant = Participant.USER,
                        content = text,
                        chatId = chatId!!,
                        timestamp = System.currentTimeMillis()
                    )
                )
                // saving response
                messageDao.insert(
                    MessageEntity(
                        participant = Participant.MODEL,
                        content = res,
                        chatId = chatId!!,
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
        } catch (e: Exception) {
            messageDao.insert(
                MessageEntity(
                    participant = Participant.ERROR,
                    content = e.message ?: "",
                    chatId = chatId ?: UUID.randomUUID().toString(),
                    timestamp = System.currentTimeMillis()
                )
            )
            throw e
        } finally {
            lock.release()
        }
        return response
    }

    private fun genChatName(message: String, defaultLength: Int = 12): String {
        return if (message.length > defaultLength) {
            message.take(defaultLength) + "..."
        } else {
            message
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

    private fun EasyPrompt.filterErrorType(): Boolean {
        return this is EasyPrompt.TextPrompt && !this.role.equals(
            Participant.ERROR.name,
            ignoreCase = true
        )
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