package org.easy.ai.network.gemini.internal.shared

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import org.easy.ai.network.gemini.util.FirstOrdinalSerializer

internal object HarmCategorySerializer :
    KSerializer<HarmCategory> by FirstOrdinalSerializer(HarmCategory::class)

@Serializable(HarmCategorySerializer::class)
internal enum class HarmCategory {
    UNKNOWN,
    @SerialName("HARM_CATEGORY_HARASSMENT") HARASSMENT,
    @SerialName("HARM_CATEGORY_HATE_SPEECH") HATE_SPEECH,
    @SerialName("HARM_CATEGORY_SEXUALLY_EXPLICIT") SEXUALLY_EXPLICIT,
    @SerialName("HARM_CATEGORY_DANGEROUS_CONTENT") DANGEROUS_CONTENT
}

typealias Base64 = String

@Serializable internal data class Content(val role: String? = null, val parts: List<Part>)

@Serializable(PartSerializer::class) internal sealed interface Part

@Serializable internal data class TextPart(val text: String) : Part

@Serializable internal data class BlobPart(@SerialName("inline_data") val inlineData: Blob) : Part

@Serializable
internal data class Blob(
    @SerialName("mime_type") val mimeType: String,
    val data: Base64,
)

@Serializable
internal data class SafetySetting(val category: HarmCategory, val threshold: HarmBlockThreshold)

@Serializable
internal enum class HarmBlockThreshold {
    @SerialName("HARM_BLOCK_THRESHOLD_UNSPECIFIED") UNSPECIFIED,
    BLOCK_LOW_AND_ABOVE,
    BLOCK_MEDIUM_AND_ABOVE,
    BLOCK_ONLY_HIGH,
    BLOCK_NONE,
}

internal object PartSerializer : JsonContentPolymorphicSerializer<Part>(Part::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Part> {
        val jsonObject = element.jsonObject
        return when {
            "text" in jsonObject -> TextPart.serializer()
            "inlineData" in jsonObject -> BlobPart.serializer()
            else -> throw SerializationException("Unknown Part type")
        }
    }
}
