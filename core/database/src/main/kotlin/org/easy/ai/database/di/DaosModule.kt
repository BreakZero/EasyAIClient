package org.easy.ai.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.easy.ai.database.EasyAIDatabase
import org.easy.ai.database.dao.ChatDao
import org.easy.ai.database.dao.MessageDao

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesChatDao(database: EasyAIDatabase): ChatDao = database.chatDao()

    @Provides
    fun providesMessageDao(database: EasyAIDatabase): MessageDao = database.messageDao()
}