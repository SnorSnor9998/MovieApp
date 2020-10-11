package com.example.movieapp

import Class.Movie
import Class.MoviesAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //most popular
    private lateinit var popularMoviesAdapter: MoviesAdapter
    private var popularMoviesPage = 1
    private lateinit var popularMoviesLayoutMgr: LinearLayoutManager
    ///

    //top rated
    private lateinit var topRatedMoviesAdapter: MoviesAdapter
    private var topRatedMoviesPage = 1
    private lateinit var topRatedMoviesLayoutMgr: LinearLayoutManager
    /////

    //now playing movie
    private lateinit var nowplayingMoviesAdapter: MoviesAdapter
    private var nowplayingMoviesPage = 1
    private lateinit var nowplayingMoviesLayoutMgr: LinearLayoutManager
    ////////

    //up coming
    private lateinit var upcomingMoviesAdapter: MoviesAdapter
    private var upcomingMoviesPage = 1
    private lateinit var upcomingMoviesLayoutMgr: LinearLayoutManager
    /////




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_btn_logout.setOnClickListener {
            Firebase.auth.signOut()
            val i = Intent(this, Login::class.java)
            startActivity(i)
            finish()

        }

        main_btn_fav.setOnClickListener { fav_page() }

        displayMovie()



    }

    private fun displayMovie(){

        //most popular
        popularMoviesLayoutMgr = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        popular_movies.layoutManager = popularMoviesLayoutMgr
        //on click to movie detail
        popularMoviesAdapter = MoviesAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
        popular_movies.adapter = popularMoviesAdapter



        getPopularMovies()
        /////////////////


        //top rated
        topRatedMoviesLayoutMgr = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        top_rated_movies.layoutManager = topRatedMoviesLayoutMgr
        //on click to movie detail
        topRatedMoviesAdapter = MoviesAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
        top_rated_movies.adapter = topRatedMoviesAdapter



        getTopRatedMovies()
        ///


        //now playing movie

        nowplayingMoviesLayoutMgr= LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        now_playing_movies.layoutManager = nowplayingMoviesLayoutMgr
        //on click to movie detail
        nowplayingMoviesAdapter = MoviesAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
        now_playing_movies.adapter = nowplayingMoviesAdapter



        getNowPlayingMoview()
        ////////


        //up coming movie
        upcomingMoviesLayoutMgr= LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        up_coming_movies.layoutManager = upcomingMoviesLayoutMgr
        //on click to movie detail
        upcomingMoviesAdapter = MoviesAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
        up_coming_movies.adapter = upcomingMoviesAdapter



        getUpComingMovie()





    }


    //most popular
    private fun getPopularMovies() {
        MoviesRepository.getPopularMovies(
            popularMoviesPage,
            ::onPopularMoviesFetched,
            ::onError
        )
    }

    private fun attachPopularMoviesOnScrollListener() {
        popular_movies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = popularMoviesLayoutMgr.itemCount
                val visibleItemCount = popularMoviesLayoutMgr.childCount
                val firstVisibleItem = popularMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    popular_movies.removeOnScrollListener(this)
                    popularMoviesPage++
                    getPopularMovies()
                }
            }
        })
    }

    private fun onPopularMoviesFetched(movies: List<Movie>) {
        popularMoviesAdapter.appendMovies(movies)
        attachPopularMoviesOnScrollListener()
    }
    ///////////////////////////////////


    //top rated
    private fun getTopRatedMovies() {
        MoviesRepository.getTopRatedMovies(
            topRatedMoviesPage,
            ::onTopRatedMoviesFetched,
            ::onError
        )
    }

    private fun attachTopRatedMoviesOnScrollListener() {
        top_rated_movies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = topRatedMoviesLayoutMgr.itemCount
                val visibleItemCount = topRatedMoviesLayoutMgr.childCount
                val firstVisibleItem = topRatedMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    top_rated_movies.removeOnScrollListener(this)
                    topRatedMoviesPage++
                    getTopRatedMovies()
                }
            }
        })
    }

    private fun onTopRatedMoviesFetched(movies: List<Movie>) {
        topRatedMoviesAdapter.appendMovies(movies)
        attachTopRatedMoviesOnScrollListener()
    }
    ///////////////////////


    //Now playing movie
    private fun getNowPlayingMoview(){
        MoviesRepository.getNowPlayingMovies(
            nowplayingMoviesPage,
            ::onNowPlayingMoviesFetched,
            ::onError
        )
    }

    private fun attachNowPlayingMoviesOnScrollListener() {
        now_playing_movies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = nowplayingMoviesLayoutMgr.itemCount
                val visibleItemCount = nowplayingMoviesLayoutMgr.childCount
                val firstVisibleItem = nowplayingMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    now_playing_movies.removeOnScrollListener(this)
                    nowplayingMoviesPage++
                    getNowPlayingMoview()
                }
            }
        })
    }

    private fun onNowPlayingMoviesFetched(movies: List<Movie>) {
        nowplayingMoviesAdapter.appendMovies(movies)
        attachNowPlayingMoviesOnScrollListener()
    }
    ///////////////////////////

    //Up Coming
    private fun getUpComingMovie(){
        MoviesRepository.getUpComingMovies(
            upcomingMoviesPage,
            ::onUpComingMoviesFetched,
            ::onError
        )
    }

    private fun attachUpComingMoviesOnScrollListener() {
        up_coming_movies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = upcomingMoviesLayoutMgr.itemCount
                val visibleItemCount = upcomingMoviesLayoutMgr.childCount
                val firstVisibleItem = upcomingMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    up_coming_movies.removeOnScrollListener(this)
                    upcomingMoviesPage++
                    getUpComingMovie()
                }
            }
        })
    }

    private fun onUpComingMoviesFetched(movies: List<Movie>) {
        upcomingMoviesAdapter.appendMovies(movies)
        attachUpComingMoviesOnScrollListener()
    }
    //////////////////////////


    //pass detail
    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(this, movie_detail::class.java)
        intent.putExtra(MOVIE_ID, movie.id)
        intent.putExtra(MOVIE_BACKDROP, movie.backdropPath)
        intent.putExtra(MOVIE_POSTER, movie.posterPath)
        intent.putExtra(MOVIE_TITLE, movie.title)
        intent.putExtra(MOVIE_RATING, movie.rating)
        intent.putExtra(MOVIE_RELEASE_DATE, movie.releaseDate)
        intent.putExtra(MOVIE_OVERVIEW, movie.overview)

        Log.d("CHE",movie.toString())
        startActivity(intent)
    }


    private fun onError() {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }

    //go to fav
    private fun fav_page(){
        val i = Intent(this, Favorite::class.java)
        finish()
        startActivity(i)
    }


}