package org.easy.ai.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import org.easy.ai.data.plugins.ChatPlugin
import org.easy.ai.model.ChatMessage
import org.easy.ai.model.Participant
import timber.log.Timber
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
            val response = chatPlugin.sendMessage(
                apiKey = apiKey,
                // filter for migration error message from previous version data
                history = history.takeUseful() + prompt
            )
            // add into history if send successful
            history.add(prompt)
            history.add(response)
            return response
        } catch (e: Exception) {
            ChatMessage.error(e)
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
        val historyWithPrompt = history.takeUseful() + prompt
        val resultTemp = StringBuilder()
        return chatPlugin.sendMessageStream(apiKey, historyWithPrompt)
            .onEach {
                resultTemp.append(it.content)
            }.onCompletion { error ->
                Timber.e(error)
                if (error == null) {
                    history.add(prompt)
                    history.add(
                        ChatMessage.success(
                            content = resultTemp.toString(),
                            participant = Participant.MODEL
                        )
                    )
                }
                lock.release()
            }
    }

    private fun List<ChatMessage>.takeUseful(): List<ChatMessage> {
        return this.filter {
            (it.participant in Participant.entries.toTypedArray())
        }
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