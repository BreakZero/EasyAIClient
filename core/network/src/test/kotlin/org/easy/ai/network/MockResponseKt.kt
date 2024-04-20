package org.easy.ai.network

internal val geminiGenerateContentNormal = """
        {
            "candidates": [
                {
                    "content": {
                        "parts": [
                            {
                                "text": "In the tranquil village of Verdigny, nestled amidst rolling hills and whispering willows in 17th century France, there lived a young maiden named Antoinette. Her heart yearned for adventure, and her mind was filled with untold tales of magic and wonder."
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