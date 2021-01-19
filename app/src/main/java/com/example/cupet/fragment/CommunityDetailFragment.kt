package com.example.cupet.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

import com.example.cupet.R
import com.example.cupet.model.Post
import com.google.firebase.database.*

class CommunityDetailFragment : Fragment() {

    lateinit var mReference: DatabaseReference
    lateinit var postid: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View =  inflater.inflate(R.layout.fragment_community_detail, container, false)

        var toolbar: Toolbar = activity!!.findViewById(R.id.toolbar)
        var toolbar_txt: TextView = toolbar.findViewById(R.id.toolbar_title)
        var search: ImageView = toolbar.findViewById(R.id.search)
        var spinner: ImageView = toolbar.findViewById(R.id.spinner)
        var bookmark: ImageView = toolbar.findViewById(R.id.bookmark)
        toolbar_txt.text = "게시글"
        search.visibility = View.GONE
        spinner.visibility = View.GONE
        bookmark.visibility = View.VISIBLE

        val preferences: SharedPreferences = context!!.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        postid = preferences.getString("postid", "none").toString()

        postInfo()

        return view
    }

    private fun postInfo() {

        mReference = FirebaseDatabase.getInstance().getReference("Posts").child(postid)

        mReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post: Post? = dataSnapshot.getValue(Post::class.java)
                post?.let {

                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }
}
