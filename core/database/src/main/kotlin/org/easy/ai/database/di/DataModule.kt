package org.easy.ai.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.easy.ai.database.EasyAIDatabase

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Provides
    fun database(
        @ApplicationContext context: Context
    ): EasyAIDatabase {
        return Room.databaseBuilder(context, EasyAIDatabase::class.java, "easy_ai.db").build()
    }
}