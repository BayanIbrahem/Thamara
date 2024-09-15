package com.dev_bayan_ibrahim.flashcards.data.data_source.remote.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration.Companion.seconds

class FlashFakePagingSource<Data : Any>(
    private val pagesCount: Int,
    private val getDataForPage: (index: Int) -> List<Data>,
) : PagingSource<Int, Data>() {
    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey ?: anchorPage?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        delay(1.seconds)
        val page = params.key ?: 0
        val data = getDataForPage(page)
        return LoadResult.Page(
            data = data,
            prevKey = if (page > 0) page.dec() else null,
            nextKey = if (page < pagesCount.dec()) page.inc() else null,
        )
    }

    companion object {
        fun <Output : Any> buildPagingDataFlow(
            pagesCount: Int,
            getDataForPage: (index: Int) -> List<Output>,
        ): Flow<PagingData<Output>> = Pager(
            PagingConfig(
                pageSize = 100
            )
        ) {
            FlashFakePagingSource(
                getDataForPage = getDataForPage,
                pagesCount = pagesCount
            )
        }.flow
    }
}