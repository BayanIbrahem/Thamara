package com.dev_bayan_ibrahim.flashcards.ui.screen.home


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.GeneralStatistics
import com.dev_bayan_ibrahim.flashcards.data.model.user.User
import com.dev_bayan_ibrahim.flashcards.ui.screen.home.component.HomeGeneralStatistics
import com.dev_bayan_ibrahim.flashcards.ui.screen.home.component.HomeUser
import com.dev_bayan_ibrahim.flashcards.ui.screen.home.viewmodel.HomeMutableUiState
import com.dev_bayan_ibrahim.flashcards.ui.screen.home.viewmodel.HomeUiActions
import com.dev_bayan_ibrahim.flashcards.ui.screen.home.viewmodel.HomeUiState
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeUiState,
    user: User?,
    generalStatistics: GeneralStatistics,
    actions: HomeUiActions,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HomeUser(
            user = user,
        )
        HomeGeneralStatistics(statistics = generalStatistics)
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            val state = HomeMutableUiState()
            val actions = object : HomeUiActions {}
            HomeScreen(
                state = state,
                actions = actions,
                generalStatistics = GeneralStatistics(),
                user = User(),
            )
        }
    }
}
