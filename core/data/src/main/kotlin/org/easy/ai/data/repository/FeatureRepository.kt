package org.easy.ai.data.repository

import org.easy.ai.model.ModelFeature
import org.easy.ai.model.ModelPlatform

interface FeatureRepository {
    suspend fun fetchFeature()

    suspend fun loadSupported(platform: ModelPlatform): List<ModelFeature>
}