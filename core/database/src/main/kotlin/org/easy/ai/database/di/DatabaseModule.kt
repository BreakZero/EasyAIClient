package org.easy.ai.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.easy.ai.database.EasyAIDatabase

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun providesDatabase(
        @ApplicationContext context: Context
    ): EasyAIDatabase {
        return Room.databaseBuilder(context, EasyAIDatabase::class.java, "easy_ai.db")
            .addMigrations(
                Migration(1, 2) {
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
            ).addMigrations(
                Migration(2,3) {
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
            ).build()
    }
}