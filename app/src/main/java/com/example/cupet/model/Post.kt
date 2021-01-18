package com.example.cupet.model

data class Post(
    var postid: String? = "",
    var postimage: String? = "",
    var description: String? = "",
    var publisher: String? = "",
    var title: String? = "",
    var time: Long? = 0
)