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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.cupet.R
import com.example.cupet.model.Post
import com.example.cupet.model.User
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_community_detail.*
import java.text.DateFormat
import java.text.SimpleDateFormat

class CommunityDetailFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    lateinit var mReference: DatabaseReference
    lateinit var postid: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_community_detail, container, false)

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

        comment_img.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CommentFragment()).addToBackStack(null).commit()
        }
        comment.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CommentFragment()).addToBackStack(null).commit()
        }

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        bookmark.setOnClickListener {
            bookmark.setImageResource(R.drawable.ic_bookmarked)
        }

        postInfo()

        return view
    }

    private fun postInfo() {

        mReference = FirebaseDatabase.getInstance().getReference("Posts").child(postid)

        mReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post: Post? = dataSnapshot.getValue(Post::class.java)
                post?.let {
                    title.text = post.title
                    description.text = post.description
                    var df: DateFormat = SimpleDateFormat("yy.MM.dd  hh:mm")
                    time.text = df.format(post.time)

                    var publisher = post.publisher
                    if (publisher != null) {
                        publisherInfo(publisher)
                    }
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }

    private fun publisherInfo(publisher: String) {

        var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(publisher)

        reference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user: User? = dataSnapshot.getValue(User::class.java)
                user?.let {
                    nickname.text = user.nickname
                    Glide.with(context!!).load(user.profile).into(profile)
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }
}
