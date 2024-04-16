package com.dev_bayan_ibrahim.flashcards.data.data_source.remote.util

/**
 * single http param
 * @param key key of the the param
 * @param value value of the param
 */
data class RequestParam(
    val key: String,
    val value: String?,
) {
    override fun toString(): String = "$key=$value"

    fun notNullValue(): RequestParam? = if (value == null) null else this
}

