package com.example.movieapp

import Class.Movie

interface OnGetMoviesCallback {

    fun onSuccess(movies: List<Movie?>?)

    fun onError()
}