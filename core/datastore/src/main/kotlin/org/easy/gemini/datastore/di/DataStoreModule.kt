package org.easy.gemini.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import org.easy.gemini.common.network.Dispatcher
import org.easy.gemini.common.network.EasyDispatchers
import org.easy.gemini.common.network.di.ApplicationScope
import org.easy.gemini.datastore.UserPreferences
import org.easy.gemini.datastore.UserPreferencesSerializer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun providesUserPreferencesDataStore(
        @ApplicationContext context: Context,
        @ApplicationScope scope: CoroutineScope,
        @Dispatcher(EasyDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        userPreferencesSerializer: UserPreferencesSerializer
    ): DataStore<UserPreferences> = DataStoreFactory.create(
        serializer = userPreferencesSerializer,
        scope = CoroutineScope(scope.coroutineContext + ioDispatcher)
    ) {
        context.dataStoreFile("user_preferences.pb")
    }
}