package org.easy.gemini.client

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.easy.gemini.client.ui.theme.EasyGeminiTheme

@SuppressLint("ProduceStateDoesNotAssignValue")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EasyGeminiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val s by produceState(initialValue = false) {
                        value = true
                    }
                    val generativeModel = GenerativeModel(
                        modelName = "gemini-pro",
                        apiKey = ""
                    )
                    val prompt = "Write a story about a magic backpack."
                    val scope = rememberCoroutineScope()
                    var textResponse by remember {
                        mutableStateOf("How do you do?")
                    }
                    val chat = generativeModel.startChat()
                    LaunchedEffect(key1 = null, block = {
                        scope.launch {
                            chat.sendMessageStream("Write a story about a magic backpack.")
                                .collect {
                                    println("===== ${it.text}")
                                    textResponse = it.text ?: ""
                                }
                        }
                    })
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(text = textResponse, modifier = Modifier
                            .fillMaxWidth()
                            .weight(1.0f))
                        Button(onClick = {
                            scope.launch {
                                chat.sendMessageStream("How do you do right now?").collect {
                                    textResponse = it.text ?: ""
                                }
                            }
                        }) {
                            Text(text = "Send Hello")
                        }
                    }

                }
            }
        }
    }
}
