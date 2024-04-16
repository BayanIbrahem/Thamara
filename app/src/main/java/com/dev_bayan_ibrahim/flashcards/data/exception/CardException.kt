package com.dev_bayan_ibrahim.flashcards.data.exception

import io.ktor.http.HttpStatusCode

open class CardException(
    override val message: String?
): IllegalArgumentException(message)

open class CardDeserializationException(
    override val message: String?
): CardException(message)

open class CardDownloadException(
    val code: HttpStatusCode
): CardException(code.description)