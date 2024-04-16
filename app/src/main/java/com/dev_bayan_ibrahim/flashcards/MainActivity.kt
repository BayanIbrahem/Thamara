package com.dev_bayan_ibrahim.flashcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.dev_bayan_ibrahim.flashcards.ui.app.FlashApp
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme
import com.google.firebase.Firebase
import com.google.firebase.initialize
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashCardsTheme {
                val windowSize = calculateWindowSizeClass(this)
                FlashApp(widthSizeClass = windowSize.widthSizeClass)
            }
            MaterialTheme
        }
        Firebase.initialize(this)
    }
}
