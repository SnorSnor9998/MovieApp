package com.example.movieapp

import Class.FavMovie
import Class.Movie
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.ryr_fav_movie.view.*

class Favorite : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        fav_btn_back.setOnClickListener { btn_back() }
    }

    private fun init(){

        //set ryr layout
        StaggeredGridLayoutManager(
            3, // span count
            StaggeredGridLayoutManager.VERTICAL // orientation
        ).apply {
            // specify the layout manager for recycler view
            fav_ryr_view.layoutManager = this
        }

        val uid = FirebaseAuth.getInstance().uid.toString()
        val adapter = GroupAdapter<GroupieViewHolder>()

        val ref = FirebaseDatabase.getInstance().getReference("FavMovie/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,error.toString(),Toast.LENGTH_SHORT).show()
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val mo = it.getValue(FavMovie::class.java)
                    if(mo != null){
                        adapter.add(bindData(mo))
                    }
                }
            }
        })

        fav_ryr_view.adapter = adapter




    }

    class bindData(val movie :FavMovie) :  Item<GroupieViewHolder>(){
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {

            val url : String = "https://image.tmdb.org/t/p/w342"+ movie.posterPath
            Picasso.get().load(url).into(viewHolder.itemView.movie_poster)

            viewHolder.itemView.movie_poster.setOnClickListener {
                val intent = Intent(it.context, movie_detail::class.java)
                intent.putExtra(MOVIE_ID, movie.id)
                intent.putExtra(MOVIE_BACKDROP, movie.backdropPath)
                intent.putExtra(MOVIE_POSTER, movie.posterPath)
                intent.putExtra(MOVIE_TITLE, movie.title)
                intent.putExtra(MOVIE_RATING, movie.rating)
                intent.putExtra(MOVIE_RELEASE_DATE, movie.releaseDate)
                intent.putExtra(MOVIE_OVERVIEW, movie.overview)
                it.context.startActivity(intent)
            }

        }



        override fun getLayout(): Int {
            return R.layout.ryr_fav_movie
        }
    }

    private fun btn_back(){
        val i = Intent(this,MainActivity::class.java)
        finish()
        startActivity(i)
    }


}