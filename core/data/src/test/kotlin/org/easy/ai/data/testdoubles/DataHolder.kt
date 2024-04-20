package org.easy.ai.data.testdoubles

import org.easy.ai.database.entities.ChatEntity
import org.easy.ai.database.entities.MessageEntity

object DataHolder {
    val chatTable = mutableSetOf<ChatEntity>()
    val messageTable = mutableListOf<MessageEntity>()
}