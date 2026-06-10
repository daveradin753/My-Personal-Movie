package com.mypersonalmovie.domain.model

import com.google.gson.annotations.SerializedName

data class AuthorDetailsModel(
    @SerializedName("avatar_path")
    var avatarPath: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("rating")
    var rating: Int? = null,
    @SerializedName("username")
    var username: String? = null
)