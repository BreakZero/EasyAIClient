package org.easy.ai.domain

import kotlinx.coroutines.flow.map
import org.easy.ai.datastore.UserPreferencesDataSource
import javax.inject.Inject

class ExactChatRepositoryUseCase @Inject internal constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource
) {

    operator fun invoke() {
        userPreferencesDataSource.userData.map {

        }
    }
}