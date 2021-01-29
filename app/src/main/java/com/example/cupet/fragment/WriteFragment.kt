package com.example.cupet.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.cupet.R
import com.example.cupet.model.Post
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_write.*
import kotlin.collections.HashMap

class WriteFragment : Fragment() {

    lateinit var firebaseUser: FirebaseUser
    lateinit var storageRef: StorageReference

    private lateinit var recyclerView: RecyclerView
    var postList = arrayListOf<Post>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View =  inflater.inflate(R.layout.fragment_write, container, false)

        var write: ImageView = view.findViewById(R.id.finish)
        var write_txt: TextView = view.findViewById(R.id.finish_txt)
        var add_photo: FloatingActionButton = view.findViewById(R.id.addPhoto)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = linearLayoutManager

        write.setOnClickListener {
            post()
        }

        write_txt.setOnClickListener {
            post()
        }

        add_photo.setOnClickListener {

        }

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        storageRef = FirebaseStorage.getInstance().getReference("posts")

        return view
    }

    private fun post() {
        var description = description.text.toString()
        var title = title.text.toString()
        var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Posts")

        val postid: String = reference.push().key.toString()

        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["postid"] = postid
        hashMap["description"] = description
        hashMap["title"] = title
        hashMap["time"] = System.currentTimeMillis()
        hashMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid

        reference.child(postid).setValue(hashMap)

        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CommunityFragment()).addToBackStack(null).commit()
    }
}
