package com.dev_bayan_ibrahim.flashcards.data.util

enum class SimpleDownloadStatus {
    DOWNLOADED,
    LOADING,
    NOT_DOWNLOADED,
}

fun Boolean.asSimpleDownloadStatus(): SimpleDownloadStatus = if (this) {
    SimpleDownloadStatus.DOWNLOADED
} else {
    SimpleDownloadStatus.NOT_DOWNLOADED
}