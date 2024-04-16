@file:OptIn(ExperimentalCoroutinesApi::class)

package com.dev_bayan_ibrahim.flashcards.ui.app


import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.ui.app.bar.FlashBottomBar
import com.dev_bayan_ibrahim.flashcards.ui.app.graph.FlashNavGraph
import com.dev_bayan_ibrahim.flashcards.ui.app.graph.util.FlashNavActions
import com.dev_bayan_ibrahim.flashcards.ui.app.graph.util.FlashNavRoutes
import com.dev_bayan_ibrahim.flashcards.ui.app.util.ConnectivityStatusBar
import com.dev_bayan_ibrahim.flashcards.ui.app.util.NewUserDialog
import com.dev_bayan_ibrahim.flashcards.ui.app.util.UserRankChangeDialog
import com.dev_bayan_ibrahim.flashcards.ui.app.viewmodel.AppUiState
import com.dev_bayan_ibrahim.flashcards.ui.app.viewmodel.AppViewModel
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@Composable
fun FlashApp(
    modifier: Modifier = Modifier,
    widthSizeClass: WindowWidthSizeClass,
    context: Context = LocalContext.current,
    appViewModel: AppViewModel = hiltViewModel()
) {
    val user by appViewModel.user.collectAsState()
    val uiState: AppUiState = appViewModel.uiState
    val uiActions = appViewModel.getAppActions()

    val initialized by appViewModel.initialized.collectAsState()
    if (!initialized) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(modifier = Modifier.size(56.dp))
                Text(stringResource(R.string.loading))
            }
        }
    } else if (user == null) {
        NewUserDialog(
            show = true,
            name = uiState.newUserName,
            age = uiState.newUserAge,
            onNameChange = uiActions::onNameChange,
            onAgeChange = uiActions::onAgeChange,
            onSave = uiActions::onSave,
        )
    } else {
        user?.let {
            UserRankChangeDialog(rank = it.rank)
        }


        val navController = rememberNavController()
        val navActions by remember(navController) {
            mutableStateOf(FlashNavActions(navController))
        }
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute by remember(navBackStackEntry) {
            derivedStateOf {
                navBackStackEntry?.destination?.route?.run {
                    FlashNavRoutes.byRoute(this)
                } ?: FlashNavRoutes.Play
            }
        }
        val isCurrentTopLevel by remember(currentRoute) {
            derivedStateOf {
                currentRoute.route in FlashNavRoutes.TopLevel.entries.map { it.route }
            }
        }

        val snackbarState by remember {
            mutableStateOf(SnackbarHostState())
        }
        val scope = rememberCoroutineScope()

        val isOnline by appViewModel.isOnline.collectAsState()
        Scaffold(
            bottomBar = {
                AnimatedVisibility(
                    visible = isCurrentTopLevel,
                    enter = fadeIn() + slideInVertically { it },
                    exit = fadeOut() + slideOutVertically { it },
                ) {
                    FlashBottomBar(
                        selected = currentRoute,
                        onClick = {
                            navActions.navigateToTopLevel(it)
                        }
                    )
                }
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarState)
            }
        ) {
            Column {
                ConnectivityStatusBar(isOnline = isOnline)
                FlashNavGraph(
                    modifier = Modifier
                        .padding(it),
                    navController = navController,
                    navActions = navActions
                ) {
                    scope.launch {
                        snackbarState.showSnackbar(
                            it.asSnackbarVisuals(context)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FlashAppPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            FlashApp(
                widthSizeClass = WindowWidthSizeClass.Compact
            )
        }
    }
}
