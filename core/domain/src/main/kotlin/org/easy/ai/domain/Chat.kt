package org.easy.ai.domain

import com.google.ai.client.generativeai.type.InvalidStateException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import org.easy.ai.data.model.ChatMessageContent
import org.easy.ai.data.plugins.ChatPlugin
import org.easy.ai.model.Participant
import java.util.concurrent.Semaphore

class Chat internal constructor(
    val history: MutableList<ChatMessageContent> = ArrayList(),
    private val apiKey: String,
    private val chatPlugin: ChatPlugin
) {
    private var lock = Semaphore(1)

    fun release(history: List<ChatMessageContent> = emptyList()) {
        this.history.clear()
        if (history.isNotEmpty()) {
            this.history.addAll(history)
        }
    }

    suspend fun sendMessage(prompt: ChatMessageContent): ChatMessageContent {
        attemptLock()
        history.add(prompt)
        val response = try {
            val response = chatPlugin.sendMessage(
                apiKey,
                history.filter { it.participant != Participant.ERROR })
            history.add(response)
            response
        } catch (e: Exception) {
            ChatMessageContent(e.message.orEmpty(), participant = Participant.ERROR).also {
                history.add(it)
            }
        } finally {
            lock.release()
        }
        return response
    }

    suspend fun sendMessage(prompt: String): ChatMessageContent {
        val content = ChatMessageContent(message = prompt, participant = Participant.USER)
        return sendMessage(content)
    }

    fun sendMessageStream(prompt: ChatMessageContent): Flow<ChatMessageContent> {
        attemptLock()
        history.add(prompt)
        return flow {
            val response = chatPlugin.sendMessage(
                apiKey,
                history.filter { it.participant != Participant.ERROR }
            )
            history.add(response)
            emit(response)
        }.onCompletion {
            lock.release()
        }
    }

    fun sendMessageStream(prompt: String): Flow<ChatMessageContent> {
        val content = ChatMessageContent(message = prompt, participant = Participant.USER)
        return sendMessageStream(content)
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