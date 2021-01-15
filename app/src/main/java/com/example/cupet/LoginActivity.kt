package com.example.cupet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        txt_signup.setOnClickListener{
            val signupIntent = Intent(this, RegisterActivity::class.java)
            startActivity(signupIntent)
        }

        login.setOnClickListener {
            doLogin()
        }
    }

    private fun doLogin() {
        val emailTxt = findViewById<View>(R.id.emailText) as EditText
        var email = emailTxt.text.toString()
        val passwordTxt = findViewById<View>(R.id.passwordText) as EditText
        var password = passwordTxt.text.toString()

        this.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
            if (task.isSuccessful) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                Toast.makeText(baseContext, "다시 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
