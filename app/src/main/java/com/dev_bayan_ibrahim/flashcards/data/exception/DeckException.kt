package com.dev_bayan_ibrahim.flashcards.data.exception

open class DeckException(
    override val message: String?
) : IllegalArgumentException(message)

open class DeckDeserializationException(
    override val message: String?
) : DeckException(message)

open class DeckNotFoundException(
    val id: Long,
    val searchLocal: Boolean
) : DeckException(
    "Deck Not found id: $id, search source ${if (searchLocal) "local db" else "server"}"
)

open class DeckInvalidColorException(
    val color: String,
) : DeckException(
    "Deck has invalid color \'$color\' which can not deserialized to hex int"
)
