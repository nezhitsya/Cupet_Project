package com.example.cupet

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cupet.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask

class editProfileActivity : AppCompatActivity() {

    private lateinit var mImageUri: Uri
    lateinit var firebaseUser: FirebaseUser
    lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        storageRef = FirebaseStorage.getInstance().getReference("profile")

        var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)
        val postListener = object: ValueEventListener {
            override fun onDataChange(dataSnapshop: DataSnapshot) {
                val user: User? = dataSnapshop.getValue(User::class.java)
                user?.let {

                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        }
        reference.addValueEventListener(postListener)
    }
}
