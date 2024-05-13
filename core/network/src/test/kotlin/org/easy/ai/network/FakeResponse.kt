package org.easy.ai.network

import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

internal const val SUCCESS_TEXT = "here is the success content from api request"
internal const val INVALID_API_KEY_TEXT = "API key not valid. Please pass a valid API key."

internal enum class ResponseType {
    SUCCESS, INVALID_API_KEY, STREAM, EMPTY
}

private fun enumOf(type: String?): ResponseType {
    return when (type) {
        ResponseType.SUCCESS.name -> ResponseType.SUCCESS
        ResponseType.INVALID_API_KEY.name -> ResponseType.INVALID_API_KEY
        ResponseType.STREAM.name -> ResponseType.STREAM
        else -> ResponseType.EMPTY
    }
}


internal fun String?.generateResponse(handleScope: MockRequestHandleScope): HttpResponseData {
    val typeEnum = enumOf(this)
    return when (typeEnum) {
        ResponseType.SUCCESS -> handleScope.buildSuccessResponse(SUCCESS_TEXT)

        ResponseType.INVALID_API_KEY -> handleScope.buildErrorResponse(
            HttpStatusCode.BadRequest,
            INVALID_API_KEY_TEXT
        )

        ResponseType.STREAM -> handleScope.buildStreamResponse(
            HttpStatusCode.OK,
            SUCCESS_TEXT,
            "null"
        )

        ResponseType.EMPTY -> TODO()
    }
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
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
    )
}

internal fun MockRequestHandleScope.buildErrorResponse(
    statusCode: HttpStatusCode,
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
    return respond(
        content = content,
        status = statusCode,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
    )
}

internal fun MockRequestHandleScope.buildStreamResponse(
    statusCode: HttpStatusCode,
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
