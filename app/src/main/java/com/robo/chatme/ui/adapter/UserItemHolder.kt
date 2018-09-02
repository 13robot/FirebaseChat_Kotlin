package com.robo.chatme.ui.adapter

import com.robo.chatme.R
import com.robo.chatme.model.UserModel
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_userlist.view.*

class UserItemHolder(val userModel: UserModel):Item<ViewHolder>() {
    override fun getLayout(): Int {
    return R.layout.item_userlist
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.tv_username.text = userModel.userName

        if (!userModel.profilePicUrl.isEmpty()) {

            Picasso.get()
                    .load(userModel.profilePicUrl)
                    .resize(100, 100)
                    .onlyScaleDown()
                    .centerCrop()
                    .into(viewHolder.itemView.iv_profilepic)
        }

    }
}