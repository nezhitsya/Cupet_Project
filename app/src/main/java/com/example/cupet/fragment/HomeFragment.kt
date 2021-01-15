package com.example.cupet.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cupet.R
import com.example.cupet.adapter.HomeAdapter
import com.example.cupet.model.Hospital
import com.example.cupet.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.drawer_header.*
import kotlinx.android.synthetic.main.toolbar_item.*
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    var hospitalList = arrayListOf<Hospital>()

    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var mReference: DatabaseReference
    lateinit var firebaseUser: FirebaseUser
    private lateinit var profileid: String
    lateinit var stateInfo: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view: View = inflater.inflate(R.layout.fragment_home, container, false)

        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        var prefs: SharedPreferences? =
            context?.getSharedPreferences("pref", Context.MODE_PRIVATE)
        profileid = prefs?.getString("id", firebaseUser.uid).toString()

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        titleInfo()

        return view
    }

    private fun hospitalInfo() {
        mReference = FirebaseDatabase.getInstance().getReference("Hospital")
        var query: Query = FirebaseDatabase.getInstance().getReference("Hospital").orderByChild("state").equalTo(stateInfo)

        query.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (child in dataSnapshot.children) {
                    val hospital: Hospital? = child.getValue(Hospital::class.java)
                    hospitalList?.add(hospital!!)
                }
                val adapter = HomeAdapter(context!!, hospitalList)
                recyclerView?.adapter = adapter
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }

    private fun titleInfo() {
        mReference = FirebaseDatabase.getInstance().getReference("Users").child(profileid)

        mReference.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user: User? = dataSnapshot.getValue(User::class.java)
                user?.let {
                    stateInfo = user.state.toString()
                    hospitalInfo()
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }
}
