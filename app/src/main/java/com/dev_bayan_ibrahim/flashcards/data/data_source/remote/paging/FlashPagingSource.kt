package com.dev_bayan_ibrahim.flashcards.data.data_source.remote.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.io_field.input.InputField
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.io_field.output.OutputFieldSerializer
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.request_builder.RequestBuilder
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.util.result.foldOnStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.KSerializer

class FlashPagingSource<Data : Any>(
    private val requestBuilder: RequestBuilder,
    private val input: InputField,
    private val serializer: KSerializer<Data>,
    private val ignoredErrorCodes: List<Int> = emptyList(),
) : PagingSource<Int, Data>() {
    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey ?: anchorPage?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        input.page = params.key ?: 1

        return requestBuilder.buildGetRequest(
            input = input,
            deserializer = OutputFieldSerializer(serializer)
        ).execute()
            .foldOnStatusCode(
                *(ignoredErrorCodes.toIntArray()),
                onSuccess = {
                    LoadResult.Page(
                        data = it.results,
                        prevKey = if (input.page > 1) input.page.dec() else null,
                        nextKey = if (input.page < (it.lastPage ?: 0)) input.page.inc() else null,
                    )
                },
                onFailure = {
                    LoadResult.Error(it)
                },
                onFailureWithCode = {
                    LoadResult.Page(
                        emptyList(),
                        prevKey = null,
                        nextKey = null
                    )
                }
            )
    }

    companion object {
        /**
         * @param ignoredErrorCodes pass any code you want to handle its response as an empty response
         * instead of error
         */
        fun <Input : InputField, Output : Any> buildPagingDataFlow(
            input: Input,
            requestBuilder: RequestBuilder,
            serializer: KSerializer<Output>,
            ignoredErrorCodes: List<Int> = emptyList(),
        ): Flow<PagingData<Output>> = Pager(
            PagingConfig(
                pageSize = 100
            )
        ) {
            FlashPagingSource(
                input = input,
                requestBuilder = requestBuilder,
                serializer = serializer,
                ignoredErrorCodes = ignoredErrorCodes,
            )
        }.flow
    }
}