package com.dev_bayan_ibrahim.flashcards.ui.screen.app_design


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.model.card.Card
import com.dev_bayan_ibrahim.flashcards.data.model.card.CardAnswer
import com.dev_bayan_ibrahim.flashcards.ui.constant.cardRatio
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme

@Composable
fun ExpandedCard(
    modifier: Modifier = Modifier,
    card: Card,
    accent: Color,
    bgPattern: String?,
    clickable: Boolean,
    showContent: Boolean,
    onSelectAnswer: (String) -> Unit,
) {
    DeckCard(
        modifier = modifier
            .aspectRatio(cardRatio)
            .fillMaxSize()
            .padding(32.dp),
        accent = accent,
        enableClick = false
    ) {
        if (showContent) {
            bgPattern?.let {
                DynamicAsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.25f),
                    imageUrl = bgPattern
                )
            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CardQuestion(question = card.question)
                CardImage(
                    modifier = Modifier.weight(1f),
                    url = card.image,
                )
                CardAnswer(
                    answer = card.answer,
                    clickable = clickable,
                    onSelectAnswer = onSelectAnswer
                )
            }
        }
    }
}

@Composable
private fun CardQuestion(
    modifier: Modifier = Modifier,
    question: String,
) {
    DynamicText(
        modifier = modifier,
        text = question,
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center,
        minLines = 1,
        maxLines = 3,
    )
}

@Composable
fun CardImage(
    modifier: Modifier = Modifier,
    url: String,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        DynamicAsyncImage(
            modifier = Modifier
                .aspectRatio(1f)
                .then(modifier),
            imageUrl = url,
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun CardAnswer(
    modifier: Modifier = Modifier,
    answer: CardAnswer,
    clickable: Boolean,
    onSelectAnswer: (String) -> Unit,
) {
    when (answer) {
        is CardAnswer.Info -> CardInfoAnswer(
            modifier = modifier,
            answer = answer,
            clickable = clickable
        ) { onSelectAnswer("") }

        is CardAnswer.MultiChoice -> CardMultiChoiceAnswer(
            modifier = modifier,
            answer = answer,
            clickable = clickable,
            onSelectAnswer = onSelectAnswer,
        )

        is CardAnswer.TrueFalse -> CardTrueFalseAnswer(
            modifier = modifier,
            answer = answer,
            clickable = clickable,
            onSelectAnswer = onSelectAnswer
        )

        is CardAnswer.Sentence -> CardSentenceAnswer(
            modifier = modifier,
            answer = answer,
            clickable = clickable,
            onSelectAnswer = onSelectAnswer
        )
    }
}

@Composable
private fun CardInfoAnswer(
    modifier: Modifier = Modifier,
    answer: CardAnswer.Info,
    clickable: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(R.string.enable_reminder))
        OutlinedButton(
            onClick = onClick,
            enabled = clickable
        ) {
            Text(answer.repeatRate.toIsoString())
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CardMultiChoiceAnswer(
    modifier: Modifier = Modifier,
    answer: CardAnswer.MultiChoice,
    chipBorder: Color = MaterialTheme.colorScheme.onSurface,
    clickable: Boolean,
    onSelectAnswer: (String) -> Unit,
) {
    require(answer.choices.count() in 2..5)
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
    ) {
        answer.choices.forEach { choice ->
            Box(
                modifier = Modifier
                    .height(32.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .clickable(enabled = clickable) { onSelectAnswer(choice) }
                    .drawBehind {
                        drawRoundRect(
                            color = chipBorder,
                            style = Stroke(1.dp.toPx()),
                            cornerRadius = CornerRadius(32.dp.toPx())
                        )
                    }
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.basicMarquee(
                        iterations = Int.MAX_VALUE,
                        animationMode = MarqueeAnimationMode.Immediately
                    ),
                    text = choice,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun CardTrueFalseAnswer(
    modifier: Modifier = Modifier,
    answer: CardAnswer.TrueFalse,
    clickable: Boolean,
    onSelectAnswer: (String) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {
        OutlinedButton(
            modifier = Modifier
                .widthIn(max = 150.dp)
                .weight(1f),
            enabled = clickable,
            onClick = { onSelectAnswer(false.toString()) },
        ) {
            Text(text = stringResource(R.string._false))
        }
        OutlinedButton(
            modifier = Modifier
                .widthIn(max = 150.dp)
                .weight(1f),
            enabled = clickable,
            onClick = { onSelectAnswer(true.toString()) },
        ) {
            Text(text = stringResource(R.string._true))
        }
    }
}

@Composable
private fun CardSentenceAnswer(
    modifier: Modifier = Modifier,
    answer: CardAnswer.Sentence,
    clickable: Boolean,
    onSelectAnswer: (String) -> Unit
) {
    var answerValue by rememberSaveable {
        mutableStateOf("")
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            shape = RoundedCornerShape(16.dp),
            value = answerValue,
            onValueChange = {
                answerValue = it
            },
            label = {
                Text(text = stringResource(R.string.answer).lowercase())
            },
            placeholder = {
                Text(text = stringResource(R.string.enter_answer))
            },
            maxLines = 2,
        )
        OutlinedButton(
            onClick = { onSelectAnswer(answerValue) },
            enabled = clickable,
        ) {
            Text(text = stringResource(R.string.answer))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpandedCardPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {

            ExpandedCard(
                card = Card(
                    question = "this is a cat",
                    image = "https://drive.google.com/uc?export=download&id=1HiRtIjas0UjWmmAqEvAhkw5wVgMGPr2O",
                    answer = CardAnswer.Sentence(
                        "correct answer",
                    ),
                    deckId = 0,
                    id = 0,
                ),
                accent = Color.Magenta,
                bgPattern = "https://drive.google.com/uc?export=download&id=1HiW96HMq-EPMLGvfsfS4Ow2VGZNTJGXt",
                clickable = true,
                showContent = true
            ) {

            }
        }
    }
}

