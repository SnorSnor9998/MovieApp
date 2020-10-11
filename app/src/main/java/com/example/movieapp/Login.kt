package com.example.movieapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import java.sql.ClientInfoStatus

class Login : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    val RC_SIGN_IN: Int = 9001


    override fun onStart() {
        super.onStart()

        if(mAuth.currentUser != null){
            val i = Intent(this,MainActivity::class.java)
            startActivity(i)
            finish()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        googlesetting()
        login_btn_google_Login.setOnClickListener {
            loginbygoogle()
        }

        login_btn_register.setOnClickListener {
            val i = Intent(this, Register::class.java)
            finish()
            overridePendingTransition( R.anim.ani_up, R.anim.ani_down )
            startActivity(i)
        }

        login_btn_login.setOnClickListener { loginbyemail() }

    }


    fun loginbyemail(){

        val email = login_txt_email.text.toString()
        val pass = login_txt_password.text.toString()

        if(email.isEmpty()){
            login_txt_email.error = "Email is require."
            return
        }

        if(pass.isEmpty()){
            login_txt_password.error = "Password is require."
            return
        }

        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
            if(!it.isSuccessful){
                Toast.makeText(this,"Email or Password Invalid",Toast.LENGTH_SHORT).show()
                return@addOnCompleteListener
            }else{
                val i = Intent(this,MainActivity::class.java)
                finish()
                overridePendingTransition( R.anim.ani_up, R.anim.ani_down )
                startActivity(i)
            }
        }


    }




    fun googlesetting() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        mAuth = Firebase.auth
    }


    fun loginbygoogle(){

        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this,"Google sign in failed",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser

                    val i = Intent(this,MainActivity::class.java)
                    finish()
                    overridePendingTransition( R.anim.ani_up, R.anim.ani_down )
                    startActivity(i)
                }


            }
    }







}