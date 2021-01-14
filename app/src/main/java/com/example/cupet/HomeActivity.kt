package com.example.cupet

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cupet.adapter.HomeAdapter
import com.example.cupet.fragment.homeFragment
import com.example.cupet.model.Hospital
import com.example.cupet.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_register.view.*
import kotlinx.android.synthetic.main.home_include_drawer.*
import kotlinx.android.synthetic.main.toolbar_item.*

class HomeActivity : AppCompatActivity() {

    lateinit var firebaseUser: FirebaseUser
    lateinit var mReference: DatabaseReference
    private lateinit var profileid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_include_drawer)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment()).commit()

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        var prefs: SharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        profileid = prefs.getString("id", firebaseUser.uid).toString()

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

        userInfo()
    }

    private fun userInfo() {
        mReference = FirebaseDatabase.getInstance().getReference("Users").child(profileid)

        val postListener = object: ValueEventListener {
            override fun onDataChange(dataSnapshop: DataSnapshot) {
                val user: User? = dataSnapshop.getValue(User::class.java)
                user?.let {
                    toolbar_title.text = user.city + " " + user.state
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        }
        mReference.addValueEventListener(postListener)
    }
}