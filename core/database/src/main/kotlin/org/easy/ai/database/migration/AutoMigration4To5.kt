package org.easy.ai.database.migration

import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec

@RenameColumn(
    tableName = "tb_chat",
    fromColumnName = "model_platform",
    toColumnName = "on_ai_model"
)
internal class AutoMigration4To5 : AutoMigrationSpec