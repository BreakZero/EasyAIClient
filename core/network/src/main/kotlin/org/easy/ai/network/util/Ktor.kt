package org.easy.ai.network.util

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.serialization.json.Json

internal suspend fun ByteReadChannel.onEachLine(block: suspend (String) -> Unit) {
    while (!isClosedForRead) {
        awaitContent()
        val line = readUTF8Line()?.takeUnless { it.isEmpty() } ?: continue
        block(line)
    }
}

internal inline fun <reified T> Json.decodeToFlow(channel: ByteReadChannel): Flow<T> = channelFlow {
    channel.onEachLine {
        val data = it.removePrefix("data:")
        send(decodeFromString(data))
    }
}