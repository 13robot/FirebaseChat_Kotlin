package com.robo.chatme.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.robo.chatme.R
import com.robo.chatme.model.UserModel
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    var photoURI: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_register.setOnClickListener{

            registerAction()
        }
        tv_alreadyhaveaccount.setOnClickListener {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        iv_profilepic.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }
    }

    private fun registerAction(){

        val userName = et_username.text.toString()
        val emailAddress = et_emailaddress.text.toString()
        val password = et_password.text.toString()

        if(userName.isEmpty() || emailAddress.isEmpty() || password.isEmpty() || photoURI == null){

            Toast.makeText(this,"Please fill all data",Toast.LENGTH_SHORT).show()
            return
        }

        //firebase authentication
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener {

                    if(!it.isSuccessful) return@addOnCompleteListener

                    // if successful
                    Log.d("RegisterActivity","register successful "+it.result.user.uid)

                    uploadImageFile()
                }
                .addOnFailureListener {

                    Log.d("RegisterActivity","register failure "+it.message)
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){

            photoURI = data.data

            iv_profilepic.setImageURI(photoURI)
        }
    }

    private fun uploadImageFile(){

        if(photoURI == null) return

        val filename = UUID.randomUUID().toString()
         val firebaseref = FirebaseStorage.getInstance().getReference("/userprofileimage/$filename")

        firebaseref.putFile(photoURI!!)
                 .addOnSuccessListener {

                     Log.d("RegisterActivity","image upload successful "+it.metadata?.path)

                     firebaseref.downloadUrl.addOnSuccessListener {

                         updateUserDetails(it.toString())
                     }
                 }
                .addOnFailureListener {

                }
    }

    private fun updateUserDetails(profilePicUrl: String){

        val uid = FirebaseAuth.getInstance().uid ?: ""

        val firebaseref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = UserModel(uid, et_username.text.toString(), profilePicUrl)

        firebaseref.setValue(user)
                .addOnCompleteListener {

                    Log.d("RegisterActivity","user register successful")

                    val intent = Intent(this, LatestMessageActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }

    }
}
