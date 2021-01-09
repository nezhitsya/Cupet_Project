package com.example.cupet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val homeIntent = Intent(this, HomeActivity::class.java)
            startActivity(homeIntent)
        } else {
            val loginActivity = Intent(this, LoginActivity::class.java)
            startActivity(login)
        }
    }
}