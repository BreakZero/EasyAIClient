package org.easy.ai.network.gemini.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import kotlinx.serialization.SerializationException
import org.easy.ai.network.gemini.internal.GenerateContentResponse
import org.easy.ai.network.gemini.internal.shared.Blob
import org.easy.ai.network.gemini.internal.shared.BlobPart
import org.easy.ai.network.gemini.internal.shared.Content
import org.easy.ai.network.gemini.internal.shared.Part
import org.easy.ai.network.gemini.internal.shared.TextPart
import org.easy.ai.network.model.ImagePart
import org.easy.ai.network.model.PromptResponse
import java.io.ByteArrayOutputStream

private const val BASE_64_FLAGS = Base64.NO_WRAP

internal fun org.easy.ai.network.model.Content.toInternal(): Content {
    return Content(this.role, this.parts.map { it.toInternal() })
}


internal fun org.easy.ai.network.model.Part.toInternal(): Part {
    return when (this) {
        is org.easy.ai.network.model.TextPart -> TextPart(text)
        is org.easy.ai.network.model.BlobPart -> BlobPart(
            Blob(mimeType, Base64.encodeToString(blob, BASE_64_FLAGS))
        )

        is org.easy.ai.network.model.ImagePart -> BlobPart(Blob("image/jpeg", encodeBitmapToBase64Png(image)))
        else -> {
            throw SerializationException(
                "The given subclass of Part (${javaClass.simpleName}) is not supported in the serialization yet."
            )
        }
    }
}

internal fun Part.toPublic(): org.easy.ai.network.model.Part {
    return when (this) {
        is TextPart -> org.easy.ai.network.model.TextPart(text)
        is BlobPart -> {
            val data = Base64.decode(inlineData.data, BASE_64_FLAGS)
            if (inlineData.mimeType.contains("image")) {
                ImagePart(decodeBitmapFromImage(data))
            } else {
                org.easy.ai.network.model.BlobPart(inlineData.mimeType, data)
            }
        }
    }
}

internal fun Content.toPublic(): org.easy.ai.network.model.Content =
    org.easy.ai.network.model.Content(role, parts.map { it.toPublic() })

internal fun GenerateContentResponse.toResult() = PromptResponse(
    contents = candidates?.map {
        it.content?.toPublic() ?: org.easy.ai.network.model.content("model") {}
    } ?: emptyList(),
    error = promptFeedback?.blockReason?.name
)

private fun encodeBitmapToBase64Png(input: Bitmap): String {
    ByteArrayOutputStream().let {
        input.compress(Bitmap.CompressFormat.JPEG, 80, it)
        return Base64.encodeToString(it.toByteArray(), BASE_64_FLAGS)
    }
}

private fun decodeBitmapFromImage(input: ByteArray) =
    BitmapFactory.decodeByteArray(input, 0, input.size)