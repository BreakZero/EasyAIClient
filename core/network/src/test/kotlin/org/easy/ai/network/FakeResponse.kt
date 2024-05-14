package org.easy.ai.network

import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

internal const val SUCCESS_TEXT = "here is the success content from api request"
internal const val INVALID_API_KEY_TEXT = "API key not valid. Please pass a valid API key."
internal const val LOCATION_NOT_SUPPORTED = "User location is not supported for the API use."

internal enum class ExpectedType {
    UNDEFINED, UNSUPPORTED_LOCATION, EMPTY
}

internal fun MockRequestHandleScope.buildSuccessResponse(text: String): HttpResponseData {
    val content = """
   {
        "candidates": [
            {
                "content": {
                    "parts": [
                        {
                            "text": "$text"
                        }
                    ],
                    "role": "model"
                },
                "finishReason": "STOP",
                "index": 0,
                "safetyRatings": [
                    {
                        "category": "HARM_CATEGORY_SEXUALLY_EXPLICIT",
                        "probability": "LOW"
                    },
                    {
                        "category": "HARM_CATEGORY_HATE_SPEECH",
                        "probability": "NEGLIGIBLE"
                    },
                    {
                        "category": "HARM_CATEGORY_HARASSMENT",
                        "probability": "NEGLIGIBLE"
                    },
                    {
                        "category": "HARM_CATEGORY_DANGEROUS_CONTENT",
                        "probability": "NEGLIGIBLE"
                    }
                ]
            }
        ]
  }
""".trimIndent()
    return respond(
        content = content,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
    )
}

internal fun MockRequestHandleScope.buildErrorResponse(
    statusCode: HttpStatusCode = HttpStatusCode.BadRequest,
    text: String
): HttpResponseData {
    val content = """
   {
        "error": {
            "code": 400,
            "message": "$text",
            "status": "INVALID_ARGUMENT",
            "details": [
                {
                    "@type": "type.googleapis.com/google.rpc.ErrorInfo",
                    "reason": "API_KEY_INVALID",
                    "domain": "googleapis.com",
                    "metadata": {
                        "service": "generativelanguage.googleapis.com"
                    }
                }
            ]
        }
    }
""".trimIndent()
    return respondError(
        content = content,
        status = statusCode,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
    )
}

internal fun MockRequestHandleScope.buildStreamResponse(
    statusCode: HttpStatusCode = HttpStatusCode.OK,
    text1: String,
    text2: String
): HttpResponseData {
    val content = """
{"candidates": [{"content": {"parts": [{"text": "$text1"}],"role": "model"},"finishReason": "STOP","index": 0}],"usageMetadata": {"promptTokenCount": 24,"candidatesTokenCount": 1,"totalTokenCount": 25}}
{"candidates": [{"content": {"parts": [{"text": "$text2"}],"role": "model"},"finishReason": "STOP","index": 0,"safetyRatings": [{"category": "HARM_CATEGORY_SEXUALLY_EXPLICIT","probability": "NEGLIGIBLE"},{"category": "HARM_CATEGORY_HATE_SPEECH","probability": "NEGLIGIBLE"},{"category": "HARM_CATEGORY_HARASSMENT","probability": "NEGLIGIBLE"},{"category": "HARM_CATEGORY_DANGEROUS_CONTENT","probability": "NEGLIGIBLE"}]}],"usageMetadata": {"promptTokenCount": 24,"candidatesTokenCount": 16,"totalTokenCount": 40}}
""".trimIndent()
    return respond(
        content = content,
        status = statusCode,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
    )
}
