package com.dev_bayan_ibrahim.flashcards.data.model.user

data class User(
    val name: String = "",
    val age: Int = 0,
    val rank: UserRank = UserRank.Init,
    val totalPlays: Int = 0,
) {
    companion object Comanion {
        val INVALID = User(
            name = "",
            age = 0,
            rank = UserRank.Init,
            totalPlays = 0
        )

    }
}
