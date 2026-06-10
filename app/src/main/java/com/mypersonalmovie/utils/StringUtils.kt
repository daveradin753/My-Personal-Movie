package com.mypersonalmovie.utils

fun String.getBackDropUrl(): String {
    return "https://image.tmdb.org/t/p/w780$this"
}

fun String.getPosterUrl(): String {
    return "https://image.tmdb.org/t/p/w500$this"
}