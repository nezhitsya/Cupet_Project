package com.example.cupet.fragment

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.cupet.R
import com.example.cupet.adapter.CostAdapter
import com.example.cupet.adapter.EstimateAdapter
import com.example.cupet.model.Cost
import com.example.cupet.model.Estimate
import com.example.cupet.model.Hospital
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_hospital_detail.*

class HospitalDetailFragment : Fragment(), OnMapReadyCallback {

    private lateinit var recyclerView: RecyclerView
    var estimateList = arrayListOf<Estimate>()
    private lateinit var recyclerView_cost: RecyclerView
    var costList = arrayListOf<Cost>()

    lateinit var mReference: DatabaseReference
    lateinit var hospitalName: String
    lateinit var firebaseUser: FirebaseUser
    lateinit var tel: String

    lateinit var mapView: MapView

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
        var call_txt: TextView = view.findViewById(R.id.call_txt)
        var call: ImageView = view.findViewById(R.id.call)
        var datePicker: DatePicker = view.findViewById(R.id.datepicker)
        datePicker.spinnersShown = false

        more_estimate.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, EstimateFragment()).addToBackStack(null).commit()
        }

        more_cost.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CostFragment()).addToBackStack(null).commit()
        }

        call_txt.setOnClickListener {
            val input = tel
            val uri = Uri.parse("tel:${input}")
            val intent = Intent(Intent.ACTION_DIAL, uri)
            startActivity(intent)
        }

        call.setOnClickListener {
            val input = tel
            val uri = Uri.parse("tel:${input}")
            val intent = Intent(Intent.ACTION_DIAL, uri)
            startActivity(intent)
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

        recyclerView_cost = view.findViewById(R.id.recycler_view_cost)
        recyclerView_cost.setHasFixedSize(true)
        val linearLayoutManager1 = LinearLayoutManager(context)
        linearLayoutManager1.reverseLayout = true
        linearLayoutManager1.stackFromEnd = true
        recyclerView_cost.layoutManager = linearLayoutManager1

//        mapView = view.findViewById(R.id.googleMap)
//        mapView.getMapAsync(this)

        hospitalInfo()
        myHospital(hospitalName, myhospital)
        getEstimate()
        getCost()

        return view
    }

    override fun onMapReady(p0: GoogleMap?) {
//        var address: LatLng = LatLng()
//        var markerOptions: MarkerOptions = MarkerOptions()
//        markerOptions.position(address)
//        googleMap.(CameraUpdateFactory.newLatLng(address))
//        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13))
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
                    tel = hospital.tel.toString()
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
                val adapter = EstimateAdapter(context!!, estimateList)
                adapter.notifyDataSetChanged()
                recyclerView.adapter = adapter
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }

    private fun getCost() {
        mReference = FirebaseDatabase.getInstance().getReference("Cost").child(hospitalName)

        mReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                costList.clear()

                for(snapshot: DataSnapshot in dataSnapshot.children) {
                    val cost: Cost? = snapshot.getValue(Cost::class.java)
                    cost?.let {
                        costList.add(cost)
                    }
                }
                val adapter = CostAdapter(context!!, costList)
                adapter.notifyDataSetChanged()
                recyclerView_cost.adapter = adapter
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }
}
