package com.example.cupet.fragment

import android.content.Context.MODE_PRIVATE
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
import com.example.cupet.adapter.EstimateAdapter
import com.example.cupet.model.Estimate
import com.example.cupet.model.Hospital
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_hospital_detail.*

class HospitalDetailFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    var estimateList = arrayListOf<Estimate>()

    lateinit var mReference: DatabaseReference
    lateinit var hospitalName: String
    lateinit var firebaseUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_hospital_detail, container, false)

        val preferences: SharedPreferences = context!!.getSharedPreferences("PREFS", MODE_PRIVATE)
        hospitalName = preferences.getString("name", "none").toString()
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        var toolbar: Toolbar = activity!!.findViewById(R.id.toolbar)
        var toolbar_txt: TextView = toolbar.findViewById(R.id.toolbar_title)
        var search: ImageView = toolbar.findViewById(R.id.search)
        var spinner: ImageView = toolbar.findViewById(R.id.spinner)
        var bookmark: ImageView = toolbar.findViewById(R.id.bookmark)
        var trash: ImageView = toolbar.findViewById(R.id.trash)
        var myhospital: ImageView = toolbar.findViewById(R.id.myhospital)
        myhospital.visibility = View.VISIBLE
        trash.visibility = View.GONE
        search.visibility = View.GONE
        spinner.visibility = View.GONE
        bookmark.visibility = View.GONE
        toolbar_txt.text = hospitalName

        var more_estimate: TextView = view.findViewById(R.id.more_estimate)
        var more_cost: TextView = view.findViewById(R.id.more_cost)

        more_estimate.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, EstimateFragment()).addToBackStack(null).commit()
        }

        more_cost.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CostFragment()).addToBackStack(null).commit()
        }

        myhospital.setOnClickListener {
            if(myhospital.tag == "not_myhospital") {
                FirebaseDatabase.getInstance().reference.child("Myhospital").child(firebaseUser.uid).child(hospitalName).setValue(true)
            } else {
                FirebaseDatabase.getInstance().reference.child("Myhospital").child(firebaseUser.uid).child(hospitalName).removeValue()
            }
        }

        recyclerView = view.findViewById(R.id.recycler_view_estimate)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        hospitalInfo()
        myHospital(hospitalName, myhospital)
        getEstimate()

        return view
    }

    private fun hospitalInfo() {

        mReference = FirebaseDatabase.getInstance().getReference("Hospital").child(hospitalName)

        mReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val hospital: Hospital? = dataSnapshot.getValue(Hospital::class.java)
                hospital?.let {
                    Glide.with(context!!).load(hospital.image).into(image)
                    address.text = hospital.address
                    intro.text = hospital.intro
                    if(hospital.likes == 3) {
                        likes1?.setImageResource(R.drawable.ic_like)
                        likes2?.setImageResource(R.drawable.ic_like)
                        likes3?.setImageResource(R.drawable.ic_like)
                    } else if(hospital.likes == 2) {
                        likes1?.setImageResource(R.drawable.ic_like)
                        likes2?.setImageResource(R.drawable.ic_like)
                    } else if(hospital.likes == 1) {
                        likes1?.setImageResource(R.drawable.ic_like)
                    } else {

                    }
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }

    private fun myHospital(hospitalName: String, imageView: ImageView) {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        mReference = FirebaseDatabase.getInstance().getReference().child("Myhospital").child(firebaseUser.uid)

        mReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.child(hospitalName).exists()) {
                    imageView.setImageResource(R.drawable.ic_favorite)
                    imageView.tag = "myhospital"
                } else {
                    imageView.setImageResource(R.drawable.ic_notfavorite)
                    imageView.tag = "not_myhospital"
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }

    private fun getEstimate() {
        mReference = FirebaseDatabase.getInstance().getReference("Estimate").child(hospitalName)

        mReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                estimateList.clear()

                for(snapshot: DataSnapshot in dataSnapshot.children) {
                    val estimate: Estimate? = snapshot.getValue(Estimate::class.java)
                    estimate?.let {
                        estimateList.add(estimate)
                    }
                }
//                for(i in 0..2) {
//                    estimateList?.get(i)
//                    val adapter = EstimateAdapter(context!!, estimateList)
//                    recyclerView.adapter = adapter
//                }
                val adapter = EstimateAdapter(context!!, estimateList)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }
}
