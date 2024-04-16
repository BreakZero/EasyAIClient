package org.easy.ai.database.migration

import androidx.room.migration.Migration

internal val MIGRATION_1_2 = Migration(1, 2) {
    it.execSQL(
        "ALTER TABLE 'tb_chat' ADD COLUMN 'model_platform' TEXT NOT NULL"
    )
    it.execSQL(
        """
            CREATE TABLE IF NOT EXISTS 'tb_message' 
            ('msg_id' INTEGER NOT NULL, 'participant' TEXT NOT NULL,'content' TEXT NOT NULL,'chat_id' TEXT NOT NULL,'create_at' INTEGER NOT NULL, PRIMARY KEY('msg_id')) 
        """.trimIndent()
    )
}