@file:Suppress("SpellCheckingInspection")

package com.dev_bayan_ibrahim.flashcards.data.data_source.init

import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.model.card.Card
import com.dev_bayan_ibrahim.flashcards.data.model.card.CardAnswer
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.getLinkOfResDrawable
import kotlin.random.Random


//private val beeKey by lazy { "1IRAKXG5u-sGiRdttd5DmpTxjFS0HqDxb" }
//private val foxKey by lazy { "1ISY0uu1w9co7gBYmUmUDt-em3B3i4Mql" }
//private val koalaKey by lazy { "1Ho7LfAJF24dGHgUkZoWDSUh9BZZOvstP" }
//private val lionKey by lazy { "1IQN4SpSAEB9eaQ_2xn-tfsH0LZvmzlPU" }
//private val turtleKey by lazy { "1IA1alnPWYp4DvWPCcC0tjR3s0XQREhvp" }
//
//val qurranKey by lazy { "1Iy_Q7fOkE-tZEPs72naT9x2ey8swq-ZP" }
//
//val salatKey by lazy { "1JuY9oh2IP8ZR10VuzAqqquSV186D3hMr" }
//val paryingKey by lazy { "1JUXSfXfiYIugJA-TJFJwCFjPg-BkPGGv" }
//val mosqueKey by lazy { "1Jq5hzmmLpT14oiuWa-31ss_biRK42kFf" }
//val pulbitKey by lazy { "1JkOBUXs9S0sgywtuLaV75LJ9R6NIbXge" }
//val beadsKey by lazy { "1Jhqatdr3yhm1yEFKotoTtcSZGQv7Zi-E" }
//val kaabaKey by lazy { "1Jg70DvPMie2n7TB0VI1-7Sq8RZdbiQ7L" }
//val moonKey by lazy { "1K3jDFpV7Y-mm9j4pf6wQVoVxVPwA-NW7" }

private val beeKey by lazy { R.drawable.img_bee }
private val foxKey by lazy { R.drawable.img_fox }
private val koalaKey by lazy { R.drawable.img_koala }
private val lionKey by lazy { R.drawable.img_lion }
private val turtleKey by lazy { R.drawable.img_turtle }

private val qurranKey by lazy { R.drawable.img_qurran }
private val salatKey by lazy { R.drawable.img_shalat }
private val paryingKey by lazy { R.drawable.img_praying }
private val mosqueKey by lazy { R.drawable.img_nabawi_mosque }
private val pulbitKey by lazy { R.drawable.img_pulpit }
private val beadsKey by lazy { R.drawable.img_beads }
private val kaabaKey by lazy { R.drawable.img_kaaba }
private val moonKey by lazy { R.drawable.img_moon }


private var id: Long = 0L
    get() {
        id = field.inc()
        return field
    }

private fun nextCardId(): Long {
    id += 1
    return id
}


val cardsInitialValues by lazy {
    mapOf(
        // first is the deck id, must be increased on every map item
        1L to listOf(
            Card(
                id = nextCardId(),
                deckId = 1,
                index = 0,
                question = "ما اسم هذا الحيوان؟",
                image = beeKey.getLinkOfResDrawable(),
                answer = CardAnswer.MultiChoice(1, "bee", "fox", "koala")
            ),
            Card(
                id = nextCardId(),
                deckId = 1,
                index = 1,
                question = "ما اسم هذا الحيوان؟",
                image = foxKey.getLinkOfResDrawable(),
                answer = CardAnswer.MultiChoice(1, "fox", "koala", "lion")
            ),
            Card(
                id = nextCardId(),
                deckId = 1,
                index = 2,
                question = "ما اسم هذا الحيوان؟",
                image = koalaKey.getLinkOfResDrawable(),
                answer = CardAnswer.MultiChoice(1, "koala", "turtle", "lion")
            ),
            Card(
                id = nextCardId(),
                deckId = 1,
                index = 3,
                question = "ما اسم هذا الحيوان؟",
                image = turtleKey.getLinkOfResDrawable(),
                answer = CardAnswer.MultiChoice(3, "bee", "fox", "turtle")
            ),
            Card(
                id = nextCardId(),
                deckId = 1,
                index = 4,
                question = "ما اسم هذا الحيوان؟",
                image = lionKey.getLinkOfResDrawable(),
                answer = CardAnswer.MultiChoice(3, "koala", "turtle", "lion")
            ),
        ),
        2L to listOf(
            Card(
                id = nextCardId(),
                deckId = 2,
                index = 0,
                question = "ما هي السورة الأولى من سورة الفاتحة\nبسم الله الرحمن الرحيم",
                image = qurranKey.getLinkOfResDrawable(),
                answer = CardAnswer.Sentence("الحمد لله رب العالمين")
            ),
            Card(
                id = nextCardId(),
                deckId = 2,
                index = 1,
                question = "أتمم السورة التالية، الحمد لله رب العالمين، ...",
                image = qurranKey.getLinkOfResDrawable(),
                answer = CardAnswer.Sentence("الرحمن الرحيم")
            ),
            Card(
                id = nextCardId(),
                deckId = 2,
                index = 2,
                question = "أتمم السورة التالية، الرحمن الرحيم، ...",
                image = qurranKey.getLinkOfResDrawable(),
                answer = CardAnswer.Sentence("مالك يوم الدين")
            ),
            Card(
                id = nextCardId(),
                deckId = 2,
                index = 3,
                question = "أتمم السورة التالية، مالك يوم الدين، ...",
                image = qurranKey.getLinkOfResDrawable(),
                answer = CardAnswer.Sentence("إياك نعبد وإياك نستعين")
            ),
            Card(
                id = nextCardId(),
                deckId = 2,
                index = 4,
                question = "أتمم السورة التالية، إياك نعبد وإياك نستعين، ...",
                image = qurranKey.getLinkOfResDrawable(),
                answer = CardAnswer.Sentence("اهدنا الصراط المستقيم")
            ),
            Card(
                id = nextCardId(),
                deckId = 2,
                index = 5,
                question = "أتمم السورة التالية، اهدنا الصراط المستقيم، ...",
                image = qurranKey.getLinkOfResDrawable(),
                answer = CardAnswer.Sentence("صراط الذين أنعمت عليهم")
            ),
            Card(
                id = nextCardId(),
                deckId = 2,
                index = 6,
                question = "أتمم السورة التالية، صراط الذين أنعمت عليهم، ...",
                image = qurranKey.getLinkOfResDrawable(),
                answer = CardAnswer.Sentence("غير المغضوب عليهم")
            ),
            Card(
                id = nextCardId(),
                deckId = 2,
                index = 7,
                question = "أتمم السورة التالية، غير المغضوب عليهم، ...",
                image = qurranKey.getLinkOfResDrawable(),
                answer = CardAnswer.Sentence("ولا الضالين")
            ),
        ),
        3L to listOf(
            Card(
                id = nextCardId(),
                deckId = 3,
                index = 0,
                image = qurranKey.getLinkOfResDrawable(),
                question = "ما هي أول سورة في القرآن الكريم؟",
                answer = CardAnswer.MultiChoice(
                    correctChoice = 1,
                    firstChoice = "الفاتحة",
                    secondChoice = "البقرة",
                    thirdChoice = "آل عمران"
                )
            ),
            Card(
                id = nextCardId(),
                deckId = 3,
                image = paryingKey.getLinkOfResDrawable(),
                index = 1,
                question = "كم عدد الركعات في صلاة الفجر؟",
                answer = CardAnswer.MultiChoice(
                    correctChoice = 1,
                    firstChoice = "2",
                    secondChoice = "4",
                    thirdChoice = "6"
                )
            ),
            Card(
                id = nextCardId(),
                deckId = 3,
                index = 2,
                image = moonKey.getLinkOfResDrawable(),
                question = "من هو خاتم النبيين؟",
                answer = CardAnswer.MultiChoice(
                    correctChoice = 1,
                    firstChoice = "النبي محمد صلى الله عليه وسلم",
                    secondChoice = "النبي موسى عليه السلام",
                    thirdChoice = "النبي عيسى عليه السلام"
                )
            ),
            Card(
                id = nextCardId(),
                deckId = 3,
                index = 3,
                image = mosqueKey.getLinkOfResDrawable(),
                question = "ما هو عدد اركان الإسلام؟",
                answer = CardAnswer.MultiChoice(
                    correctChoice = 1,
                    firstChoice = "خمسة",
                    secondChoice = "ثلاثة",
                    thirdChoice = "سبعة"
                )
            ),
            Card(
                id = nextCardId(),
                deckId = 3,
                index = 4,
                image = kaabaKey.getLinkOfResDrawable(),
                question = "من هو نبي الله الخليل؟",
                answer = CardAnswer.MultiChoice(
                    correctChoice = 1,
                    firstChoice = "النبي إبراهيم عليه السلام",
                    secondChoice = "النبي موسى عليه السلام",
                    thirdChoice = "النبي عيسى عليه السلام"
                ),
            ),
        ),
        4L to listOf(
            Card(
                id = nextCardId(),
                deckId = 4,
                index = 0,
                image = kaabaKey.getLinkOfResDrawable(),
                question = "من هو نبي الله الخليل؟",
                answer = CardAnswer.MultiChoice(
                    correctChoice = 1,
                    firstChoice = "النبي إبراهيم عليه السلام",
                    secondChoice = "النبي موسى عليه السلام",
                    thirdChoice = "النبي عيسى عليه السلام"
                ),
            ),
            Card(
                id = nextCardId(),
                deckId = 4,
                index = 1,
                image = kaabaKey.getLinkOfResDrawable(),
                question = "الحج من أركان الإسلام",
                answer = CardAnswer.TrueFalse(true)
            ),
            Card(
                id = nextCardId(),
                deckId = 4,
                index = 2,
                image = kaabaKey.getLinkOfResDrawable(),
                question = "ما هو اسم أول سورة نزلت في القرآن الكريم؟",
                answer = CardAnswer.Sentence("الفلق")
            ),
        )
    )
}


fun <T> List<T>.randomSublist(): List<T> {
    val size = Random.nextInt(0, count().dec())
    val indeces = (0 until count()).toMutableSet()
    return mutableListOf<T>().apply {
        repeat(size) {
            val index = indeces.random()
            indeces.remove(index)
            add(this@randomSublist[index])
        }
    }
}


val dummyImages by lazy {
    listOf(
        beadsKey,
        beeKey,
        foxKey,
        kaabaKey,
        koalaKey,
        lionKey,
        moonKey,
        mosqueKey,
        paryingKey,
        pulbitKey,
        qurranKey,
        salatKey,
        turtleKey,
    )
}

fun generateLargeFakeCardsForDeck(
    d: Long,
    cardsForEach: Int = 100,
    initialDeckId: Long = 0,
) = List(cardsForEach) { i2 ->
    val c = i2.inc().toLong()
    val question = if (c % 3 == 2L) {
        possibleAnswers.random()
    } else {
        "question for card no.$c, of deck no.$d"
    }
    Card(
        id = cardsForEach * d.plus(initialDeckId) + c,
        deckId = d.inc() + initialDeckId,
        index = i2,
        question = question,
        image = dummyImages.random().getLinkOfResDrawable(),
        answer = when (c % 3) {
            0L -> CardAnswer.TrueFalse(true)
            1L -> CardAnswer.MultiChoice(
                correctChoice = 1,
                firstChoice = "right answer",
                secondChoice = "wrong answer",
                thirdChoice = "wrong answer",
            )

            else -> CardAnswer.Sentence(question)
        }
    )
}

private val possibleAnswers = listOf("answer", "إجابة")