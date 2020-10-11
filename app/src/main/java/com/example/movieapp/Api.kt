package com.example.movieapp


import Class.GetMoviesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {


    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey: String = "940660eef69679d80c9600b4a1fe407f",
        @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("movie/upcoming")
    fun getUpComingMovies(
        @Query("api_key") apiKey: String = "940660eef69679d80c9600b4a1fe407f",
        @Query("page") page: Int
    ): Call<GetMoviesResponse>



    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = "940660eef69679d80c9600b4a1fe407f",
        @Query("page") page: Int
    ): Call<GetMoviesResponse>


    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String = "940660eef69679d80c9600b4a1fe407f",
        @Query("page") page: Int
    ): Call<GetMoviesResponse>

}