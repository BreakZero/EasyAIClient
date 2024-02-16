package org.easy.ai.model

//enum class AIModel(
//    val model: String,
//    val maxToken: Int
//) {
////    GPT35Turbo("", 4096),
//    GeminiPro("gemini-pro", 0),
//    GeminiProVision("gemini-pro-vision", 0);
//    companion object {
//        fun fromModelName(model: String): AIModel {
//            return when (model){
//                "gemini-pro" -> GeminiPro
//                "gemini-pro-vision" -> GeminiProVision
//                else -> GeminiPro
//            }
//        }
//    }
//}

enum class AIModel {
    GEMINI;
//    CHAT_GPT;

    companion object {
        fun fromModelName(model: String): AIModel {
            return GEMINI
//            return when (model) {
//                GEMINI.name -> GEMINI
//                CHAT_GPT.name -> CHAT_GPT
//                else -> GEMINI
//            }
        }
    }
}