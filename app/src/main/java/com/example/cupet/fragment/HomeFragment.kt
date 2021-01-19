package com.example.cupet.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cupet.R
import com.example.cupet.adapter.HomeAdapter
import com.example.cupet.model.Hospital
import com.example.cupet.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

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
                var toolbar: Toolbar = activity!!.findViewById(R.id.toolbar)
                var toolbar_txt: TextView = toolbar.findViewById(R.id.toolbar_title)
                user?.let {
                    stateInfo = user.state.toString()
                    hospitalInfo()
                    var city_txt = user.city.toString()
                    var state_txt = user.state.toString()
                    toolbar_txt.text = city_txt + " " + state_txt
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }
}
