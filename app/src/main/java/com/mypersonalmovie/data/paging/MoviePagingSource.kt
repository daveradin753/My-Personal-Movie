package com.mypersonalmovie.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mypersonalmovie.data.source.MovieApiService
import com.mypersonalmovie.domain.model.MovieModel

class MoviePagingSource(
    private val api: MovieApiService,
    private val category: String
) : PagingSource<Int, MovieModel>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, MovieModel> {

        return try {
            val page = params.key ?: 1

            val response = when (category) {
                "popular" -> api.getPopularMovies(page)
                "top_rated" -> api.getTopRatedMovies(page)
                else -> api.getNowPlayingMovies(page)
            }

            val data = response.results?: emptyList()
            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(
        state: PagingState<Int, MovieModel>
    ): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }
}