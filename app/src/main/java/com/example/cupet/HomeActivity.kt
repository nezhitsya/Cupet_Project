package com.example.cupet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cupet.adapter.HomeAdapter
import com.example.cupet.model.Hospital
import com.example.cupet.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.home_include_drawer.*
import kotlinx.android.synthetic.main.toolbar_item.*

class HomeActivity : AppCompatActivity() {

    var hospitalList = arrayListOf<Hospital>()
    lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_include_drawer)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        search.setOnClickListener{

        }

        drawer.setOnClickListener {
            home_drawer_layout.openDrawer((GravityCompat.END))
        }

        nav_join.setOnClickListener {

        }

        nav_cal.setOnClickListener {

        }

        nav_post.setOnClickListener {

        }

        nav_bookmark.setOnClickListener {

        }

        nav_mypost.setOnClickListener {

        }

        val homeAdapter = HomeAdapter(this, hospitalList)
        recycler_view.adapter = homeAdapter

        val linearLayoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = linearLayoutManager
        recycler_view.setHasFixedSize(true)

        userInfo()
    }

    fun userInfo() {
        var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("User").child("profileid")

        val postListener = object: ValueEventListener {
            override fun onDataChange(dataSnapshop: DataSnapshot) {
                val user = dataSnapshop.value as User
                toolbar_title.text = user.address
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        }
        reference.addValueEventListener(postListener)
    }
}