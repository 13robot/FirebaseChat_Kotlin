package com.robo.chatme.model


import java.io.Serializable

class UserModel(val uid: String, val userName: String, val profilePicUrl: String): Serializable{
    constructor() : this("","","")
}

class ChatMessage(val id: String, val messageText: String, val fromId: String, val toId: String, val timeStamp: Long){
    constructor() : this("","","","",-1)
}