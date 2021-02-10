package com.example.cupet.fragment

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide

import com.example.cupet.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_write.*
import kotlin.collections.HashMap

class WriteFragment : Fragment() {

    lateinit var firebaseUser: FirebaseUser
    lateinit var storageRef: StorageReference
    lateinit var mImageUri: Uri
    lateinit var postid: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View =  inflater.inflate(R.layout.fragment_write, container, false)

        var write: ImageView = view.findViewById(R.id.finish)
        var write_txt: TextView = view.findViewById(R.id.finish_txt)
        var add_photo: FloatingActionButton = view.findViewById(R.id.addPhoto)

        write.setOnClickListener {
            uploadImage()
        }

        write_txt.setOnClickListener {
            uploadImage()
        }

        add_photo.setOnClickListener {
            CropImage.activity().setAspectRatio(1, 1).start(this.context as Activity)
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        storageRef = FirebaseStorage.getInstance().getReference("posts")

        return view
    }

    private fun getFileExtension(uri: Uri): String? {
        var contentResolver: ContentResolver = context!!.contentResolver
        var mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    private fun uploadImage() {
        val filereference: StorageReference = storageRef.child(getFileExtension(mImageUri).toString())
        var uploadTask = filereference.putFile(mImageUri)

        uploadTask.continueWithTask { task ->
            if(!task.isSuccessful) {

            }
            filereference.downloadUrl
        }.addOnCompleteListener { task ->
            if(task.isSuccessful) {
                val downloadUri = task.result
                val url = downloadUri!!.toString()

                var description1 = description1.text.toString()
                var description2 = description2.text.toString()
                var title = title.text.toString()

                var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Posts")

                postid = reference.push().key.toString()

                val hashMap: HashMap<String, Any> = HashMap()
                hashMap["postid"] = postid
                hashMap["description"] = description1
                hashMap["description1"] = description2
                hashMap["title"] = title
                hashMap["time"] = System.currentTimeMillis()
                hashMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
                hashMap["postimage"] = url
                reference.child(postid).setValue(hashMap)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            var result: CropImage.ActivityResult  = CropImage.getActivityResult(data)
            mImageUri = result.uri
            photo.setImageURI(mImageUri)
        } else {
            Toast.makeText(context, "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }
    }
}
