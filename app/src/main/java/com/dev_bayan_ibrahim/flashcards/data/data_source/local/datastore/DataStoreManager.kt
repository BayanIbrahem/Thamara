package com.dev_bayan_ibrahim.flashcards.data.data_source.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dev_bayan_ibrahim.flashcards.data.model.user.User
import com.dev_bayan_ibrahim.flashcards.data.model.user.UserRank
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val usernameKey by lazy { stringPreferencesKey("username") }
val ageKey by lazy { intPreferencesKey("age") }
val rankKey by lazy { intPreferencesKey("rank") }
val expKey by lazy { intPreferencesKey("exp") }
val initialized by lazy { booleanPreferencesKey("initialized") }

class DataStoreManager(
    private val datastore: DataStore<Preferences>
) {
    private suspend fun <T> updateKey(key: Preferences.Key<T>, value: T) {
        datastore.edit { settings ->
            settings[key] = value
        }
    }

    private operator fun <T> Preferences.get(
        key: Preferences.Key<T>,
        default: T
    ): T {
        return this[key] ?: default
    }

    suspend fun setUser(name: String, age: Int) {
        updateKey(usernameKey, name)
        updateKey(ageKey, age)
        updateKey(rankKey, 0)
    }

    suspend fun updateRank(rank: UserRank) {
        updateKey(rankKey, rank.rank)
        updateKey(expKey, rank.exp)
    }

    fun getUser(): Flow<User?> = datastore.data.map {
        val username = it[usernameKey] ?: return@map null
        val age = it[ageKey] ?: return@map null
        val rank = it[rankKey] ?: 0
        val exp = it[expKey] ?: 0
        User(name = username, age = age, rank = UserRank(rank, exp))
    }

    suspend fun initializedDb(): Boolean = datastore.data.map { it[initialized, false] }.first()
    suspend fun markAsInitializedDb() = updateKey(initialized, true)
}