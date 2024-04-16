package org.easy.ai.database.migration

import androidx.room.migration.Migration

internal val MIGRATION_2_3 = Migration(2, 3) {
    it.execSQL(
        """
            CREATE TABLE IF NOT EXISTS 'tb_message_temporary'
            (
                'msg_id' INTEGER,
                'participant' TEXT NOT NULL,
                'content' TEXT NOT NULL,
                'chat_id' TEXT NOT NULL,
                'create_at' INTEGER NOT NULL,
                PRIMARY KEY('msg_id')
            )
        """.trimIndent()
    )
    it.execSQL(
        """
            INSERT INTO tb_message_temporary(msg_id, participant, content, chat_id, create_at) 
            SELECT msg_id, participant, content, chat_id, create_at FROM tb_message
        """.trimIndent()
    )
    it.execSQL("DROP TABLE tb_message")
    it.execSQL("ALTER TABLE tb_message_temporary RENAME TO tb_message")
    it.execSQL(
        """
            DROP TABLE 'tb_chat_message'
        """.trimIndent()
    )
}