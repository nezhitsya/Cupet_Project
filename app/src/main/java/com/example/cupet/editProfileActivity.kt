package com.example.cupet

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cupet.model.User
import com.google.android.gms.tasks.Continuation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_edit_profile.*


class editProfileActivity : AppCompatActivity() {

    private lateinit var mImageUri: Uri
    lateinit var firebaseUser: FirebaseUser
    lateinit var storageRef: StorageReference
    lateinit var mReference: DatabaseReference
    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        mReference = FirebaseDatabase.getInstance().reference.child("Users")
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        storageRef = FirebaseStorage.getInstance().getReference("profile")

        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)
        reference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user: User? = dataSnapshot.getValue(User::class.java)
                if (user != null) {
                    nickname.setText(user.nickname)
                    Glide.with(applicationContext).load(user.profile).into(profile)
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        profile.setOnClickListener {
            CropImage.activity().setAspectRatio(1, 1).setCropShape(CropImageView.CropShape.OVAL).start(this)
        }

        next_btn.setOnClickListener {
            edit_profile(nickname.text.toString())

            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        next_txt.setOnClickListener {
            edit_profile(nickname.text.toString())

            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun edit_profile(nickname: String) {
        val reference =
            FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)

        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["nickname"] = nickname

        reference.updateChildren(hashMap)
    }

    private fun getFileExtension(uri: Uri): String? {
        var contentResolver: ContentResolver = getContentResolver()
        var mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    private fun uploadImage() {
        if(mImageUri != null) {
            val filereference: StorageReference = storageRef.child(getFileExtension(mImageUri).toString())
        }
    }
}
