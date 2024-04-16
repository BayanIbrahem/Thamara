package com.dev_bayan_ibrahim.flashcards.ui.app.graph.util

import android.net.Uri
import com.dev_bayan_ibrahim.flashcards.R

/**
 * interface for every sub graph in navigation graph
 * @property graphPrefix prefix for every graph like "home" or "library" it can be any thing, and it
 * set whether a destination belongs to which subgraph.
 * @property startLocalRouteName start destination in this sub graph, you should not add graph prefix
 * to it, valid example "profile", "notification_settings"
 */


interface NavRoute : NavRouteType {
    val label: String get() = this.javaClass.simpleName

    val showTopAppBar: Boolean get() = true
    private fun routeBuilder(
        scheme: String,
        args: Set<FlashNavArg>,
    ): String {
        val uriBuilder = Uri.Builder()
        uriBuilder.apply {
            scheme(scheme)
            args.forEach {
                appendQueryParameter(it.label, it.value)
            }
        }
        return uriBuilder.build().toString()
    }

    val graph: String
    val args: Set<String>

    val route: String
        get() = routeBuilder(
            scheme = "${graph}_${label}",
            args = args.asNavArg()
        )

    fun generateDestinationRoute(vararg args: FlashNavArg): String {
        return args.filter {
            it.label in this.args && it.value != null
        }.distinctBy {
            it.label
        }.toSet().run {
            routeBuilder(
                scheme = "${graph}_${label}",
                this,
            )
        }
    }
}

interface NavRouteType {
    val nameRes: Int

    interface TopLevel : NavRouteType {
        val selectedIconRes: Int
        val unselectedIconRes: Int
    }

    interface Tool : NavRouteType
}

sealed interface FlashNavRoutes : NavRoute {
    override val graph: String get() = "flash"

    companion object Companion {
        fun byRoute(
            route: String,
        ): TopLevel? {
            return TopLevel.entries.firstOrNull {
                it.route == route
            }
        }
    }

    enum class TopLevel(
        override val nameRes: Int,
        override val selectedIconRes: Int,
        override val unselectedIconRes: Int,
    ) : FlashNavRoutes, NavRouteType.TopLevel {
        Home(R.string.home, R.drawable.home_fill, R.drawable.home_outline),
        Decks(R.string.decks, R.drawable.decks_fill, R.drawable.decks_outline),
        Statistics(R.string.statistics, R.drawable.statistics_fill, R.drawable.statistics_outline);

        override val label = name
        override val args: Set<String> get() = setOf()
        fun getDestination(): String = generateDestinationRoute()
    }

    data object Play : FlashNavRoutes, NavRouteType.Tool {
        override val args: Set<String> = Arg.entries.asNavLabels()
        override val nameRes = R.string.play

        fun getDestination(
            deckId: Long
        ): String = generateDestinationRoute(
            FlashNavArg(label = Arg.id.name, value = deckId.toString())
        )

        enum class Arg { id }
    }
}
