package org.easy.ai.common.network

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatcher: EasyDispatchers)

enum class EasyDispatchers{
    Default,
    IO,
}