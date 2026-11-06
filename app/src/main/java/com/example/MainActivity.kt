package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.screens.DocumentEditorScreen
import com.example.ui.screens.DocumentListScreen
import com.example.ui.theme.WordProcessorTheme
import com.example.viewmodel.DocumentViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: DocumentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WordProcessorTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val currentScreen by viewModel.currentScreen.collectAsState()

                    AnimatedContent(
                        targetState = currentScreen,
                        transitionSpec = {
                            if (targetState == "editor") {
                                (slideInHorizontally { width -> width } + fadeIn())
                                    .togetherWith(slideOutHorizontally { width -> -width } + fadeOut())
                            } else {
                                (slideInHorizontally { width -> -width } + fadeIn())
                                    .togetherWith(slideOutHorizontally { width -> width } + fadeOut())
                            }
                        },
                        label = "screen_transition"
                    ) { screen ->
                        when (screen) {
                            "editor" -> DocumentEditorScreen(viewModel = viewModel)
                            else -> DocumentListScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}
