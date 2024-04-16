package com.dev_bayan_ibrahim.flashcards.ui.app.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dev_bayan_ibrahim.flashcards.ui.app.graph.util.FlashNavActions
import com.dev_bayan_ibrahim.flashcards.ui.app.graph.util.FlashNavRoutes.Play
import com.dev_bayan_ibrahim.flashcards.ui.app.graph.util.FlashNavRoutes.TopLevel
import com.dev_bayan_ibrahim.flashcards.ui.app.util.FlashSnackbarVisuals
import com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.PlayRoute
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.DecksRoute
import com.dev_bayan_ibrahim.flashcards.ui.screen.home.HomeRoute
import com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.StatisticsRoute
import com.dev_bayan_ibrahim.flashcards.ui.util.animation.navEnterAnim
import com.dev_bayan_ibrahim.flashcards.ui.util.animation.navExitAnim
import com.dev_bayan_ibrahim.flashcards.ui.util.animation.topNavEnterAnim
import com.dev_bayan_ibrahim.flashcards.ui.util.animation.topNavExitAnim


@Composable
fun FlashNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navActions: FlashNavActions,
    onShowSnackbarMessage: (FlashSnackbarVisuals) -> Unit
) {
    val layout = LocalLayoutDirection.current
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = TopLevel.Home.route
    ) {
        TopLevel.entries.forEach { screen ->
            composable(
                route = screen.route,
                enterTransition = {
                    topNavEnterAnim(screen, layout)
                },
                exitTransition = {
                    topNavExitAnim(screen, layout)
                },
                popEnterTransition = {
                    topNavEnterAnim(screen, layout)
                },
                popExitTransition = {
                    topNavExitAnim(screen, layout)
                },
            ) {
                when (screen) {
                    TopLevel.Home -> {
                        HomeRoute(
                            onShowSnackbarMessage = onShowSnackbarMessage
                        )
                    }

                    TopLevel.Decks -> {
                        DecksRoute(
                            onShowSnackbarMessage = onShowSnackbarMessage
                        ) {
                            navActions.navigateTo(Play.getDestination(it))
                        }
                    }

                    TopLevel.Statistics -> {
                        StatisticsRoute(
                            onShowSnackbarMessage = onShowSnackbarMessage
                        )
                    }
                }
            }
        }
        composable(
            route = Play.route,
            enterTransition = { navEnterAnim() },
            exitTransition = { navExitAnim() },
            popEnterTransition = { navEnterAnim() },
            popExitTransition = { navExitAnim() },
            arguments = listOf(
                navArgument(Play.Arg.id.name) {
                    type = NavType.LongType
                }
            )
        ) {
            it.arguments?.getLong(Play.Arg.id.name)?.let { deckId ->
                PlayRoute(
                    id = deckId,
                    onShowSnackbarMessage = onShowSnackbarMessage,
                    navigateUp = navActions::navigateUp,
                )
            } ?: navActions.navigateUp()
        }
    }
}