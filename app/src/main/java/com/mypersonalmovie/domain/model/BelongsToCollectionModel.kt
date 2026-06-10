package com.mypersonalmovie.domain.model

import com.google.gson.annotations.SerializedName

data class BelongsToCollectionModel(
    @SerializedName("backdrop_path")
    var backdropPath: String? = null,
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("poster_path")
    var posterPath: String? = null
)