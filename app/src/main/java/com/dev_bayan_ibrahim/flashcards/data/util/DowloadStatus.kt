package com.dev_bayan_ibrahim.flashcards.data.util

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
interface DownloadStatus {
    val error: Throwable?
    val success: Boolean
    val finished: Boolean
    val progress: Long
    val total: Long
    val downloadSpeed: Long?
    val cancelDownload: suspend () -> Unit
    operator fun plus(other: DownloadStatus): DownloadStatus
}

class MutableDownloadStatus(override val cancelDownload: suspend () -> Unit) : DownloadStatus {
    override var error: Throwable? by mutableStateOf(null)
    override var success: Boolean by mutableStateOf(false)
    override var finished: Boolean by mutableStateOf(false)
    override var progress: Long by mutableLongStateOf(0)
    override var total: Long by mutableLongStateOf(0)
    override var downloadSpeed: Long? by mutableStateOf(null)

    override operator fun plus(other: DownloadStatus): DownloadStatus {
        val sum = MutableDownloadStatus {
            cancelDownload()
            other.cancelDownload()
        }
        sum.error = this.error ?: other.error

        sum.total = this.total + other.total
        sum.progress = this.progress + other.progress
        sum.success = this.success && other.success

        sum.finished = this.finished && other.finished

        return sum
    }
}