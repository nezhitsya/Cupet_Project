package com.example.cupet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    lateinit var mReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mReference = FirebaseDatabase.getInstance().reference.child("Users")

        next_btn.setOnClickListener {
            Register()
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
            city.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.state,
            android.R.layout.simple_spinner_item
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            state.adapter = adapter
        }
    }

    private fun Register() {
        val emailTxt = findViewById<View>(R.id.email) as EditText
        val passwordTxt = findViewById<View>(R.id.password) as EditText
        val nameTxt = findViewById<View>(R.id.username) as EditText
        val cityTxt = findViewById<View>(R.id.city) as Spinner
        val stateTxt = findViewById<View>(R.id.state) as Spinner

        var email = emailTxt.text.toString()
        var password = passwordTxt.text.toString()
        var username = nameTxt.text.toString()
        var city = cityTxt.selectedItem.toString()
        var state = stateTxt.selectedItem.toString()

        if (nameTxt.text.toString().isEmpty()) {
            nameTxt.error = "이름을 입력해주세요."
            nameTxt.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailTxt.text.toString()).matches()) {
            emailTxt.error = "이메일을 다시 입력해주세요."
            emailTxt.requestFocus()
            return
        }

        if (passwordTxt.text.toString().isEmpty()) {
            passwordTxt.error = "비밀번호를 입력해주세요."
            passwordTxt.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener { task ->
                if(task.isSuccessful) {
                    val user = auth.currentUser
                    val uid = user!!.uid

                    mReference = mReference.child(uid)
                    var hashMap: HashMap<String, Any> = HashMap()
                    hashMap["id"] = uid
                    hashMap["username"] = username
                    hashMap["nickname"] = ""
                    hashMap["profile"] = "https://firebasestorage.googleapis.com/v0/b/cupet-84a4d.appspot.com/o/profile.png?alt=media&token=883a5577-cf6c-430f-89a5-fa8eba3a25c5"
                    hashMap["city"] = city
                    hashMap["state"] = state

                    mReference.setValue(hashMap).addOnCompleteListener(OnCompleteListener<Void>() {
                        if(task.isSuccessful) {
                            startActivity(Intent(this, editProfileActivity::class.java))
                            finish()
                        }
                    })
                } else {
                    Toast.makeText(baseContext, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
        })
    }
}
