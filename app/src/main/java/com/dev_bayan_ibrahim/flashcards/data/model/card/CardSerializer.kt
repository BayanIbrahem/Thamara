package com.dev_bayan_ibrahim.flashcards.data.model.card

import com.dev_bayan_ibrahim.flashcards.data.exception.CardDeserializationException
import com.dev_bayan_ibrahim.flashcards.data.model.deck.decodeNullablePrimitive
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure

/**
 *{
 *                 "id": 2,
 *                 "question": "أختر المبرمج الأفضل",
 *                 "image_url": "https://picsum.photos/1/3/200/200",
 *                 "answer_type": "mc",
 *                 "right_answer_tf": null,
 *                 "right_answer_mc": "حسين",
 *                 "right_answer_s": null,
 *                 "mc_choice_1": "محمد",
 *                 "mc_choice_2": "عبدالله",
 *                 "mc_choice_3": "علي",
 *                 "deck_id": 3
 *             }
 */
object CardSerializer : KSerializer<Card> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
        "CardSerializer"
    ) {
        element<Long>("id")
        element<Long>("deck_id")
        element<String>("question")
        element<String>("image_url")

        element<String>("answer_type")
        element<Boolean>("right_answer_tf")
        element<String>("right_answer_mc")
        element<String>("right_answer_s")

        element<String>("mc_choice_1")
        element<String>("mc_choice_2")
        element<String>("mc_choice_3")
    }

    override fun deserialize(decoder: Decoder): Card {
        var id: Long? = null
        var deckId: Long? = null
        var question: String? = null
        var image: String? = null

        var answerType: String? = null
        var rightAnswerTf: Boolean? = null
        var rightAnswerMc: String? = null
        var rightAnswerS: String? = null

        var choice1: String? = null
        var choice2: String? = null
        var choice3: String? = null
        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> id = decodeLongElement(descriptor, index)
                    1 -> deckId = decodeLongElement(descriptor, index)
                    2 -> question = decodeStringElement(descriptor, index)
                    3 -> image = decodeStringElement(descriptor, index)

                    4 -> answerType = decodeStringElement(descriptor, index)
                    5 -> rightAnswerTf = decodeNullablePrimitive(descriptor, index)
                    6 -> rightAnswerMc = decodeNullablePrimitive(descriptor, index)
                    7 -> rightAnswerS = decodeNullablePrimitive(descriptor, index)

                    8 -> choice1 = decodeNullablePrimitive(descriptor, index)
                    9 -> choice2 = decodeNullablePrimitive(descriptor, index)
                    10 -> choice3 = decodeNullablePrimitive(descriptor, index)

                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("unexpected index $index")
                }
            }
        }
        val answer = when (answerType?.lowercase()) {
            "mc" -> {
                val choices = listOfNotNull(
                    choice1, choice2, choice3,
                )
                if (choices.count() !in 2..5) {
                    throw CardDeserializationException("invalid multi choice answers count must be between 2 and 5 but it was ${choices.count()}  with data $choices (null choices are filtered)")
                }
                val correctChoice = rightAnswerMc
                    ?: throw CardDeserializationException("right answer in mc answers type is null")

                if (correctChoice !in choices) {
                    throw CardDeserializationException("right answer in mc answers type is not between answers, current answer is $rightAnswerMc but valid answers are $choices")
                }

                CardAnswer.MultiChoice(
                    originalChoices = choices,
                    correctChoice = correctChoice,
                )
            }

            "tf" -> {
                CardAnswer.TrueFalse(
                    answer = rightAnswerTf
                        ?: throw CardDeserializationException("right answer is null in a true false answer type")
                )
            }

            "s" -> {
                CardAnswer.Sentence(
                    answer = rightAnswerS ?: throw CardDeserializationException("right answer is null in a sentence answer type")
                )
            }

            else -> {
                throw CardDeserializationException("unknown answer type $answerType")
            }
        }
        return Card(
            id = id ?: throw CardDeserializationException("null card id"),
            deckId = deckId ?: throw CardDeserializationException("null deck id"),
            question = question ?: throw CardDeserializationException("null question"),
            image = image ?: throw CardDeserializationException("null image"),
            answer = answer,
            index = 0,
        )
    }

    override fun serialize(
        encoder: Encoder,
        value: Card
    ) {
    }
}
