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
import com.example.cupet.adapter.CommentAdapter
import com.example.cupet.model.Comment
import com.example.cupet.model.Post
import com.example.cupet.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_community_detail.*
import java.text.DateFormat
import java.text.SimpleDateFormat

class CommunityDetailFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    var commentList = arrayListOf<Comment>()

    lateinit var mReference: DatabaseReference
    lateinit var postid: String
    lateinit var firebaseUser: FirebaseUser

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
        var trash: ImageView = toolbar.findViewById(R.id.trash)
        search.visibility = View.GONE
        spinner.visibility = View.GONE
        bookmark.visibility = View.VISIBLE
        trash.visibility = View.GONE
        toolbar_txt.text = "게시글"

//        when(toolbar_txt.text) {
//            "북마크" -> getBookmark()
//            "내 글" -> getMyPost()
//        }

        var comment_img: ImageView = view.findViewById(R.id.comment_img)
        var comment: TextView = view.findViewById(R.id.comment)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val preferences: SharedPreferences = context!!.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        postid = preferences.getString("postid", "none").toString()

        comment_img.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CommentFragment()).addToBackStack(null).commit()
        }
        comment.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CommentFragment()).addToBackStack(null).commit()
        }

        bookmark.setOnClickListener {
            if(bookmark.getTag().equals("bookmarked")) {
                FirebaseDatabase.getInstance().getReference().child("Bookmark").child(firebaseUser.uid).child(postid).setValue(true)
            } else {
                FirebaseDatabase.getInstance().getReference().child("Bookmark").child(firebaseUser.uid).child(postid).removeValue()
            }
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
        getComment()
        bookmarked(postid, bookmark)

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

        mReference = FirebaseDatabase.getInstance().getReference("Users").child(publisher)

        mReference.addValueEventListener(object: ValueEventListener {
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

    private fun getComment() {
        mReference = FirebaseDatabase.getInstance().getReference("Comment").child(postid)

        mReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                commentList.clear()

                for(snapshot: DataSnapshot in dataSnapshot.children) {
                    val comment: Comment? = snapshot.getValue(Comment::class.java)
                    comment?.let {
                        commentList.add(comment)
                    }
                }
                val adapter = CommentAdapter(context!!, commentList)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }

    private fun bookmarked(postid: String, imageView: ImageView) {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        mReference = FirebaseDatabase.getInstance().getReference().child("Bookmark").child(firebaseUser.uid)

        mReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.child(postid).exists()) {
                    imageView.setImageResource(R.drawable.ic_bookmarked)
                    imageView.setTag("bookmarked")
                } else {
                    imageView.setImageResource(R.drawable.ic_notbookmark)
                    imageView.setTag("not_bookmark")
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }

    private fun getBookmark() {

    }

    private fun getMyPost() {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        mReference = FirebaseDatabase.getInstance().getReference("Posts")
        var query: Query = mReference.orderByChild("publisher").equalTo(firebaseUser.uid)

        query.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }
}
