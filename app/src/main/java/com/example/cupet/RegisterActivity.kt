package com.example.cupet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        next_btn.setOnClickListener {
            signUp()

            val editProfileIntent = Intent(this, editProfileActivity::class.java)
            startActivity(editProfileIntent)
        }
    }

    fun signUp() {
        if (username.text.toString().isEmpty()) {
            username.error = "이름을 입력해주세요."
            username.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.error = "이메일을 다시 입력해주세요."
            email.requestFocus()
            return
        }

        if (password.text.toString().isEmpty()) {
            password.error = "비밀번호를 입력해주세요."
            password.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                startActivity(Intent(this, editProfileActivity::class.java))
                finish()
                } else {
                    Toast.makeText(baseContext, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
