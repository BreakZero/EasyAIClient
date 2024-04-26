package org.easy.ai.network.gemini.type

import android.graphics.Bitmap

interface Part

/** Represents text or string based data sent to and received from requests. */
class TextPart(val text: String) : Part

/**
 * Represents image data sent to and received from requests. When this is sent to the server it is
 * converted to jpeg encoding at 80% quality.
 */
class ImagePart(val image: Bitmap) : Part

/** Represents binary data with an associated MIME type sent to and received from requests. */
class BlobPart(val mimeType: String, val blob: ByteArray) : Part

/** @return The part as a [String] if it represents text, and null otherwise */
fun Part.asTextOrNull(): String? = (this as? TextPart)?.text

/** @return The part as a [Bitmap] if it represents an image, and null otherwise */
fun Part.asImageOrNull(): Bitmap? = (this as? ImagePart)?.image

/** @return The part as a [BlobPart] if it represents a blob, and null otherwise */
fun Part.asBlobPartOrNull(): BlobPart? = this as? BlobPart