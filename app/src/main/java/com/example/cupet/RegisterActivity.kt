package com.example.cupet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val spinner: Spinner = findViewById(R.id.city)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        next_btn.setOnClickListener {
            signUp()
        }

        back_btn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.city,
            android.R.layout.simple_spinner_item
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
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
