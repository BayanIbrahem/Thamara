package com.dev_bayan_ibrahim.flashcards.ui.util

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap

@Stable
interface LoadableContentList<T> {
    val loading: Boolean
    val error: Throwable?
    val initialized: Boolean
    val content: List<T>

}

class MutableLoadableContentList<T> : LoadableContentList<T> {
    override var loading: Boolean by mutableStateOf(false)
    override var error: Throwable? by mutableStateOf(null)
    override var initialized: Boolean by mutableStateOf(false)
    override val content: SnapshotStateList<T> = mutableStateListOf()

    fun needInitialize(): Boolean = !loading && !initialized
}
@Stable
interface LoadableContentMap<K, V> {
    val loading: Boolean
    val error: Throwable?
    val initialized: Boolean
    val content: Map<K, V>

}

class MutableLoadableContentMap<K, V> : LoadableContentMap<K, V> {
    override var loading: Boolean by mutableStateOf(false)
    override var error: Throwable? by mutableStateOf(null)
    override var initialized: Boolean by mutableStateOf(false)
    override val content: SnapshotStateMap<K, V> = mutableStateMapOf()

    fun needInitialize(): Boolean = !loading && !initialized
}
