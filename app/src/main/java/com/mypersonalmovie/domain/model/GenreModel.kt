package com.mypersonalmovie.domain.model

import com.google.gson.annotations.SerializedName

data class GenreModel(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("name")
    var name: String? = null
)