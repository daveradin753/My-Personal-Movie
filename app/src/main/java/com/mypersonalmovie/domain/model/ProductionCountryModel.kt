package com.mypersonalmovie.domain.model

import com.google.gson.annotations.SerializedName

data class ProductionCountryModel(
    @SerializedName("iso_3166_1")
    var iso31661: String? = null,
    @SerializedName("name")
    var name: String? = null
)