package org.easy.ai.data.repository.model

import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GoogleGenerativeAIException
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.easy.ai.data.repository.UserPreferencesRepository
import org.easy.ai.model.ChatMessage
import org.easy.ai.model.ModelPlatform
import org.easy.ai.model.Participant
import org.easy.ai.model.UserDataValidateResult
import javax.inject.Inject

class GeminiChatRepository @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ModelChatRepository {

    private var generativeModel: GenerativeModel? = null
    private var chat: Chat? = null

    override fun initial(): Flow<Boolean> {
        return userPreferencesRepository.userData.map {
            val isValid = it.validate() == UserDataValidateResult.NORMAL
            if (isValid) {
                generativeModel = GenerativeModel(
                    modelName = "gemini-pro",
                    apiKey = it.apiKeys[ModelPlatform.GEMINI.name]!!
                )
                chat = generativeModel!!.startChat()
            }
            isValid
        }
    }

    override fun switchChat(history: List<ChatMessage>) {
        chat?.history?.clear()
        chat?.history?.addAll(history.filter { it.participant != Participant.ERROR }
            .map { message ->
                content {
                    role = if (Participant.USER == message.participant) "user" else "model"
                    text(message.text)
                }
            }
        )
    }

    override suspend fun sendMessage(userMessage: String): ChatMessage {
        return try {
            val response = chat?.sendMessage(userMessage)

            response?.text?.let { modelResponse ->
                ChatMessage(
                    text = modelResponse,
                    participant = Participant.MODEL,
                    isPending = false
                )
            } ?: ChatMessage(
                participant = Participant.ERROR
            )
        } catch (e: GoogleGenerativeAIException) {
            ChatMessage(
                text = e.localizedMessage.orEmpty(),
                participant = Participant.ERROR
            )
        }
    }
}