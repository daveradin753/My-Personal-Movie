package com.mypersonalmovie.data.dtos
import com.google.gson.annotations.SerializedName
import com.mypersonalmovie.domain.model.MovieModel


class Movie {

    data class Response(
        @SerializedName("page")
        var page: Int? = null,
        @SerializedName("results")
        var results: List<MovieModel>? = null,
        @SerializedName("total_pages")
        var totalPages: Int? = null,
        @SerializedName("total_results")
        var totalResults: Int? = null
    )

}