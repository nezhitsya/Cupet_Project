package com.example.cupet.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.cupet.R
import com.google.firebase.database.*

class CommunityDetailFragment : Fragment() {

    lateinit var mReference: DatabaseReference
    lateinit var postid: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View =  inflater.inflate(R.layout.fragment_community_detail, container, false)

        val preferences: SharedPreferences = context!!.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        postid = preferences.getString("postid", "none").toString()

        return view
    }

    private fun postInfo() {

        mReference = FirebaseDatabase.getInstance().getReference("Posts").child(postid)

        mReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }
}
