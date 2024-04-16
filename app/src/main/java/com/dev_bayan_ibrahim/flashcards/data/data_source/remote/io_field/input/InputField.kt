package com.dev_bayan_ibrahim.flashcards.data.data_source.remote.io_field.input

import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.util.BodyParam
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.util.RequestParam

open class InputField(
    private val paginated: Boolean = false,
    val requestParams: List<RequestParam> = emptyList(),
    val bodyParams: List<BodyParam> = emptyList(),
) {
    var page: Int = 1

    fun asRequestParams(filter: (RequestParam) -> Boolean): List<RequestParam> =
        listOfNotNull(
            if (paginated) {
                RequestParam("page", page.toString()).notNullValue()
            } else {
                null
            },
            *(requestParams.filter(filter).toTypedArray()),
        )

    fun asBodyParams(filter: (BodyParam) -> Boolean): List<BodyParam> = listOfNotNull(
        *(bodyParams.filter(filter).toTypedArray()),
    )
}