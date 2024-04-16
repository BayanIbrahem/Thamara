package com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dev_bayan_ibrahim.flashcards.data.model.user.UserRank

@Dao
interface RankDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun registerRankChange(rank: UserRank)

    @Query(
        """
            select * from ranks 
            order by datetime
        """
    )
    suspend fun getRanksStatistics(
    ): List<UserRank>
}