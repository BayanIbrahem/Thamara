package com.dev_bayan_ibrahim.flashcards.data.data_source.init

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.model.deck.split
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.getLinkOfResDrawable
import kotlinx.datetime.Clock
import kotlin.random.Random
import kotlin.time.Duration.Companion.days

private var id: Long = 0L
private fun nextDeckId(): Long {
    id += 1
    return id
}

val onlinePatterns by lazy {
    listOf(
        "https://drive.google.com/uc?id=1IoGs1uhtweqZo81k6eY341qwir36H2s_&export=download",
        "https://drive.google.com/uc?id=1IpOHfgCRAsL70QtBKsqrPyJb1V3qZhH-&export=download",
        "https://drive.google.com/uc?id=1ItKh6GZK17yauixqAlz3vAe344KjfnhI&export=download",
    )

}
val patterns by lazy {
    listOf(
//        "1HiW96HMq-EPMLGvfsfS4Ow2VGZNTJGXt",
//        "1Ij89Wsaat9VBDwT2L5YMl4MUop_4rsBn",
//        "1IoGs1uhtweqZo81k6eY341qwir36H2s_",
//        "1IpOHfgCRAsL70QtBKsqrPyJb1V3qZhH-",
//        "1IrRJ1KWndLf1On_CehBW25_wnf332xr3",
//        "1ItKh6GZK17yauixqAlz3vAe344KjfnhI",
        R.drawable.img_pattern_1,
        R.drawable.img_pattern_2,
        R.drawable.img_pattern_3,
        R.drawable.img_pattern_4,
        R.drawable.img_pattern_5,
        R.drawable.img_pattern_6,
    ).map { it.getLinkOfResDrawable() }
}

val decksInitialValue by lazy {
    listOf(
        DeckHeader(
            id = nextDeckId(),
            tags = "انجليزية".split(),
            collection = "حيوانات",
            name = "اسماء الحيوانات",
            cardsCount = cardsInitialValues[1]?.count() ?: 0,
            pattern = patterns[0],
            color = Color.Red.toArgb(),
            level = 1,
            rates = 1,
            rate = 2f,
            creation = Clock.System.now(),
            offlineData = true,
            offlineImages = true,
        ),

        DeckHeader(
            id = nextDeckId(),
            tags = "قرآن, إسلام".split(),
            collection = "إسلام",
            name = "سورة الفاتحة",
            cardsCount = cardsInitialValues[2]?.count() ?: 0,
            pattern = patterns[1],
            color = Color.Green.toArgb(),
            level = 2,
            rates = 1,
            rate = 3f,
            creation = Clock.System.now(),
            allowShuffle = false,
            offlineData = true,
            offlineImages = true,
        ),
        DeckHeader(
            id = nextDeckId(),
            tags = "إسلام, أساسي".split(),
            collection = "إسلام",
            name = "أساسيات الإسلام",
            cardsCount = cardsInitialValues[3]?.count() ?: 0,
            pattern = patterns[2],
            color = Color.Yellow.toArgb(),
            level = 2,
            rates = 1,
            rate = 4f,
            creation = Clock.System.now(),
            offlineData = true,
            offlineImages = true,
        ),
        DeckHeader(
            id = nextDeckId(),
            tags = "إسلام, أساسي".split(),
            collection = "إسلام",
            name = "أسئلة متنوعة",
            cardsCount = cardsInitialValues[4]?.count() ?: 0,
            pattern = patterns[3],
            color = Color.Blue.toArgb(),
            level = 3,
            rates = 10,
            rate = 5f,
            creation = Clock.System.now(),
            allowShuffle = false,
            offlineData = true,
            offlineImages = true,
        ),
    )
}


private val dummyTags by lazy {
    List(100) {
        "tag #$it"
    }
}
private val dummyCollections by lazy {
    List(10) {
        "collection #$it"
    }
}
fun generateLargeFakeDecks(
    decksCount: Int = 100,
    cardsForEach: Int = 100,
    initialDeckId: Long,
) = List(decksCount) {
    DeckHeader(
        id = it.inc().toLong() + initialDeckId,
        tags = dummyTags.randomSublist(),
        collection = dummyCollections.random(),
        name = "deck - no.${it.inc()}",
        cardsCount = cardsForEach,
        pattern = patterns.random(),
        color = randomColor().toArgb(),
        level = Random.nextInt(1, 10),
        rates = Random.nextInt(0, 100_000),
        rate = Random.nextFloat() * 5,
        creation = Clock.System.now() - Random.nextInt(0, 100).days,
        offlineImages = true,
        offlineData = true,
    )
}

private fun randomColor(): Color {
    return Color(
        red = Random.nextInt(0, 255),
        green = Random.nextInt(0, 255),
        blue = Random.nextInt(0, 255),
    )
}

