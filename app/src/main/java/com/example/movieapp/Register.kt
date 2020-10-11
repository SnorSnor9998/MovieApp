package com.example.movieapp

import Class.User
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        reg_btn_register.setOnClickListener { registerUser() }
        reg_btn_back.setOnClickListener { backLogin() }


    }

    private fun backLogin() {
        val i = Intent(this, Login::class.java)
        finish()
        overridePendingTransition( R.anim.ani_up_back, R.anim.ani_down_back )
        startActivity(i)
    }

    private fun registerUser(){

        val email = reg_txt_email.text.toString()
        val pass1 = reg_txt_password.text.toString()
        val pass2 = reg_txt_password2.text.toString()



        if(email.isEmpty()){
            reg_txt_email.error = "Email is require."
            return
        }


        if (pass1.length < 6 || pass2.length <6){
            reg_txt_password2.error = "At least 6 character."
        }


        if (pass1.isEmpty()||pass2.isEmpty()){
            reg_txt_password2.error = "Password is require."
            return
        }

        if (!(pass1.equals(pass2))){
            reg_txt_password2.error = "Password not match."
            return
        }



            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pass1).addOnCompleteListener {
                if(!it.isSuccessful){
                    Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }else{
                    Toast.makeText(this,"User Created",Toast.LENGTH_SHORT).show()
                    savetoDB(FirebaseAuth.getInstance().uid.toString())
                    val i = Intent(this, MainActivity::class.java)
                    finish()
                    startActivity(i)

                }
            }

    }

    private fun savetoDB(uuid : String){

        val user = User()
        user.email = reg_txt_email.text.toString()
        user.uuid = uuid

        val ref = FirebaseDatabase.getInstance().getReference("/Users/$uuid")
        ref.setValue(user)
    }


}