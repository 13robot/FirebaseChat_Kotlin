package com.robo.chatme.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.robo.chatme.R
import com.robo.chatme.model.ChatMessage
import com.robo.chatme.model.UserModel
import com.robo.chatme.ui.adapter.ChatFromHolder
import com.robo.chatme.ui.adapter.ChatToHolder
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    var toUser: UserModel? = null

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        toUser = intent.getSerializableExtra(NewMessageActivity.USER_MODEL_KEY) as UserModel


        rv_chatlog.layoutManager = LinearLayoutManager(this@ChatActivity)
        rv_chatlog.adapter = adapter

        chatMessageListener()

        iv_send.setOnClickListener {

            val messageText = et_newmessage.text.toString()

            if (messageText.isEmpty() || toUser == null) return@setOnClickListener

            // else if success
            messageSendAction(messageText, toUser!!)
            et_newmessage.setText("")
        }
    }

    private fun messageSendAction(messageText: String, fromUser: UserModel) {

        val fromUser = LatestMessageActivity.currentUser

        val fromUID = fromUser?.uid
        val toUID = toUser?.uid

        if (fromUID == null) return
// if not null

        val firebaseref = FirebaseDatabase.getInstance().getReference("user_messages/$fromUID/$toUID").push()
        val tofirebaseref = FirebaseDatabase.getInstance().getReference("user_messages/$toUID/$fromUID").push()

        val chatMessage = ChatMessage(firebaseref.key!!, messageText, fromUID, toUID!!, (System.currentTimeMillis() / 1000))

        firebaseref.setValue(chatMessage)
                .addOnSuccessListener {

                    et_newmessage.text.clear()
                    rv_chatlog.scrollToPosition(adapter.itemCount - 1)
                    Log.d("ChatActivity", "Message send successful ${firebaseref.key}")
                }

        tofirebaseref.setValue(chatMessage)

    }

    private fun chatMessageListener() {


        val fromUser = LatestMessageActivity.currentUser

        val fromUID = fromUser?.uid
        val toUID = toUser?.uid

        val firebaseref = FirebaseDatabase.getInstance().getReference("user_messages/$fromUID/$toUID")

        firebaseref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                val chatMessage = p0.getValue(ChatMessage::class.java)

                Log.d("ChatActivity", chatMessage?.messageText)

                if (chatMessage != null) {

                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {

                        adapter.add(ChatFromHolder(chatMessage, fromUser!!))
                    } else {

                        adapter.add(ChatToHolder(chatMessage, toUser!!))
                    }

                    rv_chatlog.scrollToPosition(adapter.itemCount - 1)
                }

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }


        })
    }
}
