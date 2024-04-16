package org.easy.ai.database.migration

import androidx.room.migration.Migration

internal val MIGRATION_3_4 = Migration(3, 4) {
    it.execSQL(
        """
            CREATE TABLE IF NOT EXISTS 'tb_message_temporary'
            (
                'msg_id' INTEGER,
                'participant' TEXT NOT NULL,
                'content' TEXT NOT NULL,
                'chat_id' TEXT NOT NULL,
                'create_at' INTEGER NOT NULL,
                PRIMARY KEY('msg_id'),
                FOREIGN KEY('chat_id') REFERENCES 'tb_chat'('chat_id') ON UPDATE NO ACTION ON DELETE CASCADE
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
    it.execSQL("CREATE INDEX IF NOT EXISTS index_tb_message_chat_id ON tb_message(chat_id)")
}