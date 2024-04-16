package com.dev_bayan_ibrahim.flashcards.data.data_source.remote.util

import kotlin.reflect.KProperty0

sealed interface BodyParam {
    val key: String
    data class Text(
        override val key: String,
        val value: String?,
    ) : BodyParam {
        constructor(property: KProperty0<*>): this(property.name, property.get()?.toString())
        fun notNullValue(): Text? = if (value == null) null else this
    }
//    data class File(
//        override val key: String,
//        val array: ByteArray,
//        val type: String,
//        val name: String,
//    ) : BodyParam
}