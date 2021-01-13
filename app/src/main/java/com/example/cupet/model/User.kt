package com.example.cupet.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var username: String? = "",
    var email: String? = "",
    var password: String? = "",
    var profile: String? = "",
    var city: String? = "",
    var state: String? = "",
    var nickname: String? = ""
)