package com.robo.chatme.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.robo.chatme.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        btn_login.setOnClickListener{

loginAction()
        }

        tv_register_new.setOnClickListener{

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun loginAction(){

        val emailAddress = et_emailaddress.text.toString()
        val password = et_password.text.toString()

        if(emailAddress.isEmpty() || password.isEmpty()){

            Toast.makeText(this,"Please enter email and password", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener {

                    if(!it.isSuccessful) return@addOnCompleteListener

                    // if successful
                    Log.d("RegisterActivity","login successful "+it.result.user.uid)

                    val intent = Intent(this, LatestMessageActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                }
                .addOnFailureListener {

                    Log.d("RegisterActivity","login failure "+it.message)
                }
    }
}
