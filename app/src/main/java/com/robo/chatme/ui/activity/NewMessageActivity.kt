package com.robo.chatme.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.robo.chatme.R
import com.robo.chatme.model.UserModel
import com.robo.chatme.ui.adapter.UserItemHolder
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : AppCompatActivity() {
    companion object {
        val USER_MODEL_KEY = "USER_MODEL_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        fetchUserList()
    }

    private fun fetchUserList(){

        val firebaseref = FirebaseDatabase.getInstance().getReference("/users")
        val uid = FirebaseAuth.getInstance().uid

        firebaseref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {

                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {

                    val user = it.getValue(UserModel::class.java)

                    if(user != null && user.uid != uid){
                        adapter.add(UserItemHolder(user))
                    }
                }

                adapter.setOnItemClickListener { item, view ->

                    val userItemHolder = item as UserItemHolder

                    val intent = Intent(this@NewMessageActivity, ChatActivity::class.java)
                    intent.putExtra(USER_MODEL_KEY,userItemHolder.userModel)
                    startActivity(intent)

                    finish()
                }

                rv_userlist.layoutManager = LinearLayoutManager(this@NewMessageActivity)
                rv_userlist.adapter = adapter
            }
        })

    }
}
