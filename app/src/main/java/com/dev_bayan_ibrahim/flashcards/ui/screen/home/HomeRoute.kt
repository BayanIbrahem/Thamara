package com.dev_bayan_ibrahim.flashcards.ui.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev_bayan_ibrahim.flashcards.ui.app.util.FlashSnackbarVisuals
import com.dev_bayan_ibrahim.flashcards.ui.screen.home.viewmodel.HomeViewModel


@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    onShowSnackbarMessage: (FlashSnackbarVisuals) -> Unit
) {
    val user by homeViewModel.user.collectAsState()
    val generalStatistics by homeViewModel.generalStatistics.collectAsState()

    HomeScreen(
        modifier = modifier.fillMaxSize(),
        state = homeViewModel.state,
        user = user,
        generalStatistics = generalStatistics,
        actions = homeViewModel.getUiActions()
    )
}