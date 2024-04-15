package org.easy.ai.data.repository.chat

import com.google.ai.client.generativeai.type.InvalidStateException
import kotlinx.coroutines.sync.Semaphore
import org.easy.ai.database.dao.ChatDao
import org.easy.ai.database.dao.MessageDao
import org.easy.ai.database.entities.ChatEntity
import org.easy.ai.database.entities.MessageEntity
import org.easy.ai.model.ModelPlatform
import org.easy.ai.model.Participant
import org.easy.ai.network.gemini.GeminiRestApi
import org.easy.ai.network.gemini.type.Content
import org.easy.ai.network.gemini.type.content
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
    private val history: MutableList<Content> = ArrayList()
    private var chatId: String? = null

    override suspend fun startChat(chatId: String?) {
        attemptLock()
        this.chatId = chatId
        val history = chatId?.let {
            messageDao.getChatHistoryByChatId(it).messages.map {
                content(role = it.participant.name) { text(it.content) }
            }
        } ?: emptyList()

        this.history.clear()
        this.history.addAll(history)

        lock.release()
    }

    override suspend fun sendMessage(apiKey: String, message: String): String {
        val content = content(Participant.USER.name.lowercase()) { text(message) }
        attemptLock()
        if (history.isEmpty() && chatId == null) {
            // saving chat
            val chatName = genChatName(message)
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
                content
            ).also { res ->
                // only success will save into local
                // saving message
                messageDao.insert(
                    MessageEntity(
                        participant = Participant.USER,
                        content = message,
                        chatId = chatId!!,
                        timestamp = System.currentTimeMillis()
                    )
                )
                // saving response
                messageDao.insert(
                    MessageEntity(
                        participant = Participant.MODEL,
                        content = res.text.orEmpty(),
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
        return response.text.orEmpty()
    }

    private fun genChatName(message: String, defaultLength: Int = 12): String {
        return if (message.length > defaultLength) {
            message.take(defaultLength) + "..."
        } else {
            message
        }
    }

    private fun Content.filterErrorType(): Boolean {
        return !this.role.equals(
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