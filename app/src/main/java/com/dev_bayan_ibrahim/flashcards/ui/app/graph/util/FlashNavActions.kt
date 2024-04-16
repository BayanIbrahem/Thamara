package com.dev_bayan_ibrahim.flashcards.ui.app.graph.util

import androidx.compose.runtime.Immutable
import androidx.navigation.NavHostController

@Immutable
class FlashNavActions(
    private val navHostController: NavHostController
) {
    fun navigateTo(route: String, isTopLevelDestination: Boolean = false) {
        val currentRoute = navHostController.currentBackStackEntry?.destination?.route
        if (route == currentRoute) {
            return
        }
        if (isTopLevelDestination) {
            navHostController.popBackStack()
        }
        navHostController.navigate(route) {
            this.launchSingleTop = true
        }
    }

    fun navigateUp() {
        navHostController.popBackStack()
    }

    fun navigateToTopLevel(topLevel: FlashNavRoutes.TopLevel) {
        navigateTo(topLevel.getDestination(), true)
    }
}