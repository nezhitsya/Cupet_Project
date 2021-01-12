package com.example.cupet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txt_signup.setOnClickListener{
            val signupIntent = Intent(this, RegisterActivity::class.java)
            startActivity(signupIntent)
        }

        login.setOnClickListener{
//            doLogin()
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    private fun doLogin() {
        auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(emailText.text.toString(),
            passwordText.text.toString()).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                Toast.makeText(baseContext, "다시 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
