package org.easy.ai.network.gemini.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import kotlinx.serialization.SerializationException
import org.easy.ai.network.gemini.internal.GenerateContentResponse
import org.easy.ai.network.gemini.internal.server.BlockReason
import org.easy.ai.network.gemini.internal.server.Candidate
import org.easy.ai.network.gemini.internal.server.PromptFeedback
import org.easy.ai.network.gemini.internal.shared.Blob
import org.easy.ai.network.gemini.internal.shared.BlobPart
import org.easy.ai.network.gemini.internal.shared.Content
import org.easy.ai.network.gemini.internal.shared.Part
import org.easy.ai.network.gemini.internal.shared.TextPart
import org.easy.ai.network.gemini.type.ImagePart
import org.easy.ai.network.gemini.type.content
import java.io.ByteArrayOutputStream

private const val BASE_64_FLAGS = Base64.NO_WRAP

internal fun org.easy.ai.network.gemini.type.Content.toInternal(): Content {
    return Content(this.role, this.parts.map { it.toInternal() })
}


internal fun Candidate.toPublic(): org.easy.ai.network.gemini.type.Candidate {
    return org.easy.ai.network.gemini.type.Candidate(
        this.content?.toPublic() ?: content("model") {},
        emptyList(),
        emptyList(),
        null
    )
}

internal fun org.easy.ai.network.gemini.type.Part.toInternal(): Part {
    return when(this) {
        is org.easy.ai.network.gemini.type.TextPart -> TextPart(text)
        is org.easy.ai.network.gemini.type.BlobPart -> BlobPart(Blob(mimeType, Base64.encodeToString(blob, BASE_64_FLAGS)))
        is ImagePart -> BlobPart(Blob("image/jpeg", encodeBitmapToBase64Png(image)))
        else -> {
            throw SerializationException(
                "The given subclass of Part (${javaClass.simpleName}) is not supported in the serialization yet."
            )
        }
    }
}

internal fun Content.toPublic(): org.easy.ai.network.gemini.type.Content =
    org.easy.ai.network.gemini.type.Content(role, parts.map { it.toPublic() })


internal fun Part.toPublic(): org.easy.ai.network.gemini.type.Part {
    return when (this) {
        is TextPart -> org.easy.ai.network.gemini.type.TextPart(text)
        is BlobPart -> {
            val data = Base64.decode(inlineData.data, BASE_64_FLAGS)
            if (inlineData.mimeType.contains("image")) {
                ImagePart(decodeBitmapFromImage(data))
            } else {
                org.easy.ai.network.gemini.type.BlobPart(inlineData.mimeType, data)
            }
        }
    }
}

internal fun BlockReason.toPublic() =
    when (this) {
        BlockReason.UNSPECIFIED -> org.easy.ai.network.gemini.type.BlockReason.UNSPECIFIED
        BlockReason.SAFETY -> org.easy.ai.network.gemini.type.BlockReason.SAFETY
        BlockReason.OTHER -> org.easy.ai.network.gemini.type.BlockReason.OTHER
        BlockReason.UNKNOWN -> org.easy.ai.network.gemini.type.BlockReason.UNKNOWN
    }

internal fun PromptFeedback.toPublic(): org.easy.ai.network.gemini.type.PromptFeedback {
    return org.easy.ai.network.gemini.type.PromptFeedback(
        blockReason?.toPublic(),
        emptyList(),
    )
}

internal fun GenerateContentResponse.toPublic() =
    org.easy.ai.network.gemini.type.GenerateContentResponse(
        candidates?.map { it.toPublic() }.orEmpty(),
        promptFeedback?.toPublic()
    )

private fun encodeBitmapToBase64Png(input: Bitmap): String {
    ByteArrayOutputStream().let {
        input.compress(Bitmap.CompressFormat.JPEG, 80, it)
        return Base64.encodeToString(it.toByteArray(), BASE_64_FLAGS)
    }
}

private fun decodeBitmapFromImage(input: ByteArray) =
    BitmapFactory.decodeByteArray(input, 0, input.size)