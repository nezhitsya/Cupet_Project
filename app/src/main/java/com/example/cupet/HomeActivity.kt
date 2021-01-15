package com.example.cupet

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.example.cupet.fragment.CommunityFragment
import com.example.cupet.fragment.HomeFragment
import com.example.cupet.fragment.WriteFragment
import com.example.cupet.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.drawer_header.*
import kotlinx.android.synthetic.main.home_include_drawer.*
import kotlinx.android.synthetic.main.toolbar_item.*

class HomeActivity : AppCompatActivity() {

    lateinit var firebaseUser: FirebaseUser
    lateinit var mReference: DatabaseReference
    private lateinit var profileid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_include_drawer)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        var prefs: SharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        profileid = prefs.getString("id", firebaseUser.uid).toString()

        search.setOnClickListener{

        }

        drawer.setOnClickListener {
            home_drawer_layout.openDrawer((GravityCompat.END))
        }

        nav_join.setOnClickListener {
            toolbar_title.text = "즐겨찾는 병원"
            spinner.visibility = View.GONE
        }

        nav_cal.setOnClickListener {
            toolbar_title.text = "진료비 계산기"
            spinner.visibility = View.GONE
        }

        nav_post.setOnClickListener {
            home_drawer_layout.closeDrawer((GravityCompat.END))
            toolbar_title.text = "게시판"
            search.visibility = View.GONE
            spinner.visibility = View.GONE
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CommunityFragment()).commit()
        }

        nav_bookmark.setOnClickListener {
            toolbar_title.text = "북마크"
            spinner.visibility = View.GONE
        }

        nav_mypost.setOnClickListener {
            toolbar_title.text = "내가 쓴 글"
            spinner.visibility = View.GONE
        }

        userInfo()
    }

    private fun userInfo() {
        mReference = FirebaseDatabase.getInstance().getReference("Users").child(profileid)

        val postListener = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user: User? = dataSnapshot.getValue(User::class.java)
                user?.let {
                    toolbar_title.text = user.city + " " + user.state
                    nickname.text = user.nickname
                    Glide.with(this@HomeActivity).load(user.profile).into(profile)
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        }
        mReference.addValueEventListener(postListener)
    }
}