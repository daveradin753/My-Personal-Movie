package com.mypersonalmovie.data.dtos

import com.google.gson.annotations.SerializedName
import com.mypersonalmovie.domain.model.BelongsToCollectionModel
import com.mypersonalmovie.domain.model.GenreModel
import com.mypersonalmovie.domain.model.ProductionCompanyModel
import com.mypersonalmovie.domain.model.ProductionCountryModel
import com.mypersonalmovie.domain.model.SpokenLanguageModel

class MovieDetail {

    data class Response(
        @SerializedName("adult")
        var adult: Boolean? = null,
        @SerializedName("backdrop_path")
        var backdropPath: String? = null,
        @SerializedName("belongs_to_collection")
        var belongsToCollection: BelongsToCollectionModel? = null,
        @SerializedName("budget")
        var budget: Int? = null,
        @SerializedName("genres")
        var genres: List<GenreModel?>? = null,
        @SerializedName("homepage")
        var homepage: String? = null,
        @SerializedName("id")
        var id: Int? = null,
        @SerializedName("imdb_id")
        var imdbId: String? = null,
        @SerializedName("origin_country")
        var originCountry: List<String?>? = null,
        @SerializedName("original_language")
        var originalLanguage: String? = null,
        @SerializedName("original_title")
        var originalTitle: String? = null,
        @SerializedName("overview")
        var overview: String? = null,
        @SerializedName("popularity")
        var popularity: Double? = null,
        @SerializedName("poster_path")
        var posterPath: String? = null,
        @SerializedName("production_companies")
        var productionCompanies: List<ProductionCompanyModel>? = null,
        @SerializedName("production_countries")
        var productionCountries: List<ProductionCountryModel>? = null,
        @SerializedName("release_date")
        var releaseDate: String? = null,
        @SerializedName("revenue")
        var revenue: Int? = null,
        @SerializedName("runtime")
        var runtime: Int? = null,
        @SerializedName("spoken_languages")
        var spokenLanguages: List<SpokenLanguageModel>? = null,
        @SerializedName("status")
        var status: String? = null,
        @SerializedName("tagline")
        var tagline: String? = null,
        @SerializedName("title")
        var title: String? = null,
        @SerializedName("video")
        var video: Boolean? = null,
        @SerializedName("vote_average")
        var voteAverage: Double? = null,
        @SerializedName("vote_count")
        var voteCount: Int? = null,
        @SerializedName("status_code")
        var statusCode: Int? = null,
        @SerializedName("status_message")
        var status_message: String? = null
    )

}