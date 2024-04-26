package org.easy.ai.network.gemini.type

import android.graphics.Bitmap

class Content
@JvmOverloads
constructor(val role: String? = null, val parts: List<Part>) {
    class Builder {
        var role: String? = null

        var parts: MutableList<Part> = arrayListOf()

        @JvmName("addPart")
        fun <T : Part> part(data: T) = apply { parts.add(data) }

        @JvmName("addText")
        fun text(text: String) = part(TextPart(text))

        @JvmName("addBlob")
        fun blob(mimeType: String, blob: ByteArray) = part(BlobPart(mimeType, blob))

        @JvmName("addImage")
        fun image(image: Bitmap) = part(ImagePart(image))

        fun build(): Content = Content(role, parts)
    }
}

fun content(role: String? = null, init: Content.Builder.() -> Unit): Content {
    val builder = Content.Builder()
    builder.role = role
    builder.init()
    return builder.build()
}