package org.easy.gemini.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.easy.gemini.database.GeminiDatabase

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Provides
    fun database(
        @ApplicationContext context: Context
    ): GeminiDatabase {
        return Room.databaseBuilder(context, GeminiDatabase::class.java, "gemini_client.db").build()
    }
}