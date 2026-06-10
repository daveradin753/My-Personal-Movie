package com.mypersonalmovie.data.dtos
import com.google.gson.annotations.SerializedName
import com.mypersonalmovie.domain.model.ReviewModel


class MovieReviews {

    data class Response(
        @SerializedName("id")
        var id: Int? = null,
        @SerializedName("page")
        var page: Int? = null,
        @SerializedName("results")
        var results: List<ReviewModel>? = null,
        @SerializedName("total_pages")
        var totalPages: Int? = null,
        @SerializedName("total_results")
        var totalResults: Int? = null
    )

}