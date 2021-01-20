package com.example.cupet.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.cupet.R
import com.example.cupet.adapter.CommentAdapter
import com.example.cupet.model.Comment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_comment.*

class CommentFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    var commentList = arrayListOf<Comment>()

    lateinit var mReference: DatabaseReference
    lateinit var postid: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_comment, container, false)

        val preferences: SharedPreferences = context!!.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        postid = preferences.getString("name", "none").toString()

        var comment: EditText = view.findViewById(R.id.comment)
        var send: TextView = view.findViewById(R.id.send)

        send.setOnClickListener {
            postComment()
        }

        var toolbar: Toolbar = activity!!.findViewById(R.id.toolbar)
        var toolbar_txt: TextView = toolbar.findViewById(R.id.toolbar_title)
        var search: ImageView = toolbar.findViewById(R.id.search)
        var spinner: ImageView = toolbar.findViewById(R.id.spinner)
        var bookmark: ImageView = toolbar.findViewById(R.id.bookmark)
        var trash: ImageView = toolbar.findViewById(R.id.trash)
        toolbar_txt.text = "댓글"
        search.visibility = View.GONE
        spinner.visibility = View.GONE
        bookmark.visibility = View.GONE
        trash.visibility = View.GONE

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        getComment()

        return view
    }

    private fun postComment() {
        var comment = comment.text.toString()
        var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Comment")
        val commentid: String = reference.push().key.toString()

        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["comment"] = comment
        hashMap["commentid"] = commentid
        hashMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid

        reference.child(postid).child(commentid).setValue(hashMap)
    }

    private fun getComment() {
        mReference = FirebaseDatabase.getInstance().getReference("Comment").child(postid)

        mReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                commentList.clear()
                for (child in dataSnapshot.children) {
                    val comment: Comment? = child.getValue(Comment::class.java)
                    commentList?.add(comment!!)
                }
                val adapter = CommentAdapter(context!!, commentList, postid)
                recyclerView?.adapter = adapter
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }
}
