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
import com.bumptech.glide.Glide

import com.example.cupet.R
import com.example.cupet.model.Hospital
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_hospital_detail.*

class HospitalDetailFragment : Fragment() {

    lateinit var mReference: DatabaseReference
    lateinit var hospitalName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_hospital_detail, container, false)

        val preferences: SharedPreferences = context!!.getSharedPreferences("PREFS", MODE_PRIVATE)
        hospitalName = preferences.getString("name", "none").toString()

        var toolbar: Toolbar = activity!!.findViewById(R.id.toolbar)
        var toolbar_txt: TextView = toolbar.findViewById(R.id.toolbar_title)
        var search: ImageView = toolbar.findViewById(R.id.search)
        var spinner: ImageView = toolbar.findViewById(R.id.spinner)
        var bookmark: ImageView = toolbar.findViewById(R.id.bookmark)
        var trash: ImageView = toolbar.findViewById(R.id.trash)
        trash.visibility = View.GONE
        search.visibility = View.GONE
        spinner.visibility = View.GONE
        bookmark.visibility = View.GONE
        toolbar_txt.text = hospitalName

        hospitalInfo()

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
}
