package org.easy.ai.database.converts

import androidx.room.TypeConverter
import org.easy.ai.model.AiModel
import org.easy.ai.model.Participant

class EnumConverts {
    @TypeConverter
    fun toAiModel(value: String) = enumValueOf<AiModel>(value)

    @TypeConverter
    fun fromAiModel(value: AiModel) = value.name

    @TypeConverter
    fun toParticipant(value: String) = enumValueOf<Participant>(value)

    @TypeConverter
    fun fromParticipant(value: Participant) = value.name
}