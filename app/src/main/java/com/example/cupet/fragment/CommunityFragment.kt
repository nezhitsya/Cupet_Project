package com.example.cupet.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.cupet.R
import com.example.cupet.adapter.CommunityAdapter
import com.example.cupet.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_community.*

class CommunityFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    var postList = arrayListOf<Post>()
    var bookmarkList = arrayListOf<String>()

    lateinit var mReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view: View = inflater.inflate(R.layout.fragment_community, container, false)

        var write: ImageView = view.findViewById(R.id.write)
        var write_txt: TextView = view.findViewById(R.id.write_txt)

        write.setOnClickListener{
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, WriteFragment()).addToBackStack(null).commit()
        }

        write_txt.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, WriteFragment()).addToBackStack(null).commit()
        }

        var toolbar: Toolbar = activity!!.findViewById(R.id.toolbar)
        var toolbar_txt: TextView = toolbar.findViewById(R.id.toolbar_title)
//        var search: ImageView = toolbar.findViewById(R.id.search)
//        var spinner: ImageView = toolbar.findViewById(R.id.spinner)
//        var bookmark: ImageView = toolbar.findViewById(R.id.bookmark)
//        var trash: ImageView = toolbar.findViewById(R.id.trash)
////        toolbar_txt.text = "게시판"
//        search.visibility = View.GONE
//        spinner.visibility = View.GONE
//        bookmark.visibility = View.GONE
//        trash.visibility = View.GONE

        when(toolbar_txt.text) {
            "게시판" -> postInfo()
            "북마크" -> getBookmark()
            "내가 쓴 글" -> getMyPost()
        }

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

//        postInfo()

        return view
    }

    private fun postInfo() {
        var toolbar: Toolbar = activity!!.findViewById(R.id.toolbar)
        var toolbar_txt: TextView = toolbar.findViewById(R.id.toolbar_title)
        var search: ImageView = toolbar.findViewById(R.id.search)
        var spinner: ImageView = toolbar.findViewById(R.id.spinner)
        var bookmark: ImageView = toolbar.findViewById(R.id.bookmark)
        var trash: ImageView = toolbar.findViewById(R.id.trash)
        toolbar_txt.text = "게시판"
        search.visibility = View.GONE
        spinner.visibility = View.GONE
        bookmark.visibility = View.GONE
        trash.visibility = View.GONE

        mReference = FirebaseDatabase.getInstance().getReference("Posts")

        mReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                postList.clear()

                for(snapshot: DataSnapshot in dataSnapshot.children) {
                    val post: Post? = snapshot.getValue(Post::class.java)
                    post?.let {
                        postList?.add(post)
                    }
                }
                val adapter = CommunityAdapter(context!!, postList)
                recyclerView?.adapter = adapter
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }

    private fun getBookmark() {
        var toolbar: Toolbar = activity!!.findViewById(R.id.toolbar)
        var toolbar_txt: TextView = toolbar.findViewById(R.id.toolbar_title)
        var search: ImageView = toolbar.findViewById(R.id.search)
        var spinner: ImageView = toolbar.findViewById(R.id.spinner)
        var bookmark: ImageView = toolbar.findViewById(R.id.bookmark)
        var trash: ImageView = toolbar.findViewById(R.id.trash)
        search.visibility = View.GONE
        spinner.visibility = View.GONE
        bookmark.visibility = View.GONE
        trash.visibility = View.GONE
        toolbar_txt.text = "북마크"

        bookmarkList = ArrayList()

        var firebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        mReference = FirebaseDatabase.getInstance().getReference().child("Bookmark").child(firebaseUser.uid)

        mReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for(snapshot: DataSnapshot in dataSnapshot.children) {
                    snapshot.key?.let { bookmarkList.add(it) }
                }
                readBookmark()
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }

    private fun readBookmark() {
        mReference = FirebaseDatabase.getInstance().getReference("Posts")

        mReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                postList.clear()
                for(snapshot: DataSnapshot in dataSnapshot.children) {
                    val post: Post? = dataSnapshot.getValue(Post::class.java)

                    for(id: String in bookmarkList) {
                        var postid = post?.postid
                        if(postid.equals(id)) {
                            postList?.add(post!!)
                        }
                    }
                    val adapter = CommunityAdapter(context!!, postList)
                    recyclerView?.adapter = adapter
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }

    private fun getMyPost() {
        var firebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        mReference = FirebaseDatabase.getInstance().getReference("Posts")

        mReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                postList.clear()

                for(snapshot: DataSnapshot in dataSnapshot.children) {
                    val post: Post? = snapshot.getValue(Post::class.java)

                    if((post?.publisher).equals(firebaseUser.uid)) {
                        postList?.add(post!!)
                    }
                }
                val adapter = CommunityAdapter(context!!, postList)
                recyclerView?.adapter = adapter
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }
}
