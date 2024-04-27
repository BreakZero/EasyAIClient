package org.easy.ai.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import org.easy.ai.data.plugins.ChatPlugin
import org.easy.ai.model.ChatMessage
import org.easy.ai.model.MessageType
import org.easy.ai.model.Participant
import java.util.concurrent.Semaphore

class Chat internal constructor(
    private val history: MutableList<ChatMessage> = ArrayList(),
    private val apiKey: String,
    private val chatPlugin: ChatPlugin
) {
    private var lock = Semaphore(1)

    fun release(history: List<ChatMessage> = emptyList()) {
        this.history.clear()
        if (history.isNotEmpty()) {
            this.history.addAll(history)
        }
    }

    suspend fun sendMessage(prompt: ChatMessage): ChatMessage {
        attemptLock()
        val response = try {
            getMessageResponse(apiKey, prompt)
        } catch (e: Exception) {
            ChatMessage(
                content = e.message ?: "unknown error from service",
                participant = Participant.MODEL,
                type = MessageType.ERROR
            )
        } finally {
            lock.release()
        }
        return response
    }

    suspend fun sendMessage(prompt: String): ChatMessage {
        val content = ChatMessage(content = prompt)
        return sendMessage(content)
    }

    fun sendMessageStream(prompt: ChatMessage): Flow<ChatMessage> {
        attemptLock()
        return flow {
            val response = getMessageResponse(apiKey, prompt)
            emit(response)
        }.catch { error ->
            ChatMessage(
                content = error.message ?: "unknown error from service",
                participant = Participant.MODEL,
                type = MessageType.ERROR
            ).also {
                emit(it)
            }
        }.onCompletion {
            lock.release()
        }
    }

    private suspend fun getMessageResponse(apiKey: String, message: ChatMessage): ChatMessage {
        val response = chatPlugin.sendMessage(
            apiKey = apiKey,
            // filter for migration error message from previous version data
            history = history.filter {
                it.participant in Participant.entries.toTypedArray()
            } + message
        )
        // add into history if send successful
        history.add(message)
        history.add(response)
        return response
    }

    fun sendMessageStream(prompt: String): Flow<ChatMessage> {
        val content = ChatMessage(content = prompt)
        return sendMessageStream(content)
    }

    private fun attemptLock() {
        if (!lock.tryAcquire()) {
            throw IllegalStateException(
                "This chat instance currently has an ongoing request, please wait for it to complete " +
                    "before sending more messages"
            )
        }
    }
}