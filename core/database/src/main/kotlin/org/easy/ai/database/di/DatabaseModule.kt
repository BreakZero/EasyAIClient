package org.easy.ai.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.easy.ai.database.EasyAIDatabase
import org.easy.ai.database.migration.MIGRATION_1_2
import org.easy.ai.database.migration.MIGRATION_2_3
import org.easy.ai.database.migration.MIGRATION_3_4

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun providesDatabase(
        @ApplicationContext context: Context
    ): EasyAIDatabase {
        return Room.databaseBuilder(context, EasyAIDatabase::class.java, "easy_ai.db")
            .addMigrations(
                MIGRATION_1_2,
                MIGRATION_2_3,
                MIGRATION_3_4
            )
            .build()
    }
}