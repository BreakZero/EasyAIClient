package org.easy.ai.database.converts

import androidx.room.TypeConverter
import org.easy.ai.model.ModelPlatform
import org.easy.ai.model.Participant

class EnumConverts {
    @TypeConverter
    fun toModelPlatform(value: String) = enumValueOf<ModelPlatform>(value)

    @TypeConverter
    fun fromModelPlatform(value: ModelPlatform) = value.name

    @TypeConverter
    fun toParticipant(value: String) = enumValueOf<Participant>(value)

    @TypeConverter
    fun fromParticipant(value: Participant) = value.name
}