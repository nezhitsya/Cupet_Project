package com.example.cupet.model

data class Hospital(
    var name: String? = "",
    var city: String? = "",
    var state: String? = "",
    var image: String? = "",
    var intro: String? = "",
    var likes: Int = 0,
    var tel: String? = "",
    var address: String? = ""
)