package com.example.movieapp

import Class.FavMovie
import Class.Movie
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_movie_detail.*

const val MOVIE_ID = "extra_movie_id"
const val MOVIE_BACKDROP = "extra_movie_backdrop"
const val MOVIE_POSTER = "extra_movie_poster"
const val MOVIE_TITLE = "extra_movie_title"
const val MOVIE_RATING = "extra_movie_rating"
const val MOVIE_RELEASE_DATE = "extra_movie_release_date"
const val MOVIE_OVERVIEW = "extra_movie_overview"


class movie_detail : AppCompatActivity() {

    var uid = FirebaseAuth.getInstance().uid.toString()
    var movie_id : Long ?= null
    var check_exist : Boolean = false

    override fun onStart() {
        super.onStart()
        movie_id = intent.extras?.getLong(MOVIE_ID,0)
        init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        movie_overview.movementMethod = ScrollingMovementMethod.getInstance()

        val extras = intent.extras

        if (extras != null) {
            populateDetails(extras)
        } else {
            finish()
        }

        //button
        md_btn_addFav.setOnClickListener { btn_fav() }

    }


    private fun populateDetails(extras: Bundle) {
        extras.getString(MOVIE_BACKDROP)?.let { backdropPath ->
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w1280$backdropPath")
                .transform(CenterCrop())
                .into(movie_backdrop)
        }

        extras.getString(MOVIE_POSTER)?.let { posterPath ->
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w342$posterPath")
                .transform(CenterCrop())
                .into(movie_poster)
        }

        movie_title.text = extras.getString(MOVIE_TITLE, "")
        movie_rating.rating = extras.getFloat(MOVIE_RATING, 0f) / 2
        movie_release_date.text = extras.getString(MOVIE_RELEASE_DATE, "")
        movie_overview.setText(extras.getString(MOVIE_OVERVIEW, ""))

    }

    private fun btn_fav(){

        if (check_exist){
            //remove from db
            val ref = FirebaseDatabase.getInstance().getReference("/FavMovie/$uid/$movie_id")
            ref.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.ref.removeValue()
                }
            })

            md_txt_fav.setText("ADD TO FAVORITE")
            md_pic_love.visibility = View.VISIBLE
            check_exist = false

        }else if(!check_exist){
            //store to own database
            val fmovie = FavMovie()
            fmovie.id = movie_id
            fmovie.title = intent.extras?.getString(MOVIE_TITLE, "").toString()
            fmovie.overview = intent.extras?.getString(MOVIE_OVERVIEW, "").toString()
            fmovie.posterPath = intent.extras?.getString(MOVIE_POSTER, "").toString()
            fmovie.backdropPath = intent.extras?.getString(MOVIE_BACKDROP, "").toString()
            fmovie.rating = intent.extras?.getFloat(MOVIE_RATING, 0f)
            fmovie.releaseDate = intent.extras?.getString(MOVIE_RELEASE_DATE, "").toString()


            val ref = FirebaseDatabase.getInstance().getReference("/FavMovie/$uid/$movie_id")
            ref.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                        ref.setValue(fmovie)
                }
            })

            md_txt_fav.setText("REMOVE FROM FAVORITE")
            check_exist = true
            md_pic_love.visibility = View.INVISIBLE

        }




    }




    private fun init(){


        var ref = FirebaseDatabase.getInstance().getReference("/FavMovie/$uid/$movie_id")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    md_txt_fav.setText("REMOVE FROM FAVORITE")
                    check_exist = true
                }else{
                    md_txt_fav.setText("ADD TO FAVORITE")
                    md_pic_love.visibility = View.VISIBLE
                }
            }
        })

    }




}