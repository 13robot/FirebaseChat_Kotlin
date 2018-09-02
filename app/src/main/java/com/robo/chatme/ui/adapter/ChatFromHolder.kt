package com.robo.chatme.ui.adapter

import com.robo.chatme.R
import com.robo.chatme.model.ChatMessage
import com.robo.chatme.model.UserModel
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_chat_from.view.*

class ChatFromHolder(val chatMessage: ChatMessage, val userModel: UserModel):Item<ViewHolder>() {
    override fun getLayout(): Int {
    return R.layout.item_chat_from
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.tv_usermessage.text = chatMessage.messageText

        val targetImageView = viewHolder.itemView.iv_profilepic
        val userImageUrl = userModel.profilePicUrl

        Picasso.get()
                .load(userImageUrl)
                .resize(100, 100)
                .onlyScaleDown()
                .centerCrop()
                .into(targetImageView)
    }
}