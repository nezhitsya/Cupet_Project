package com.example.cupet.fragment

import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

import com.example.cupet.R
import com.example.cupet.model.Hospital
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_hospital_detail.*
import kotlinx.android.synthetic.main.toolbar_item.*

class HospitalDetailFragment : Fragment() {

    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var mReference: DatabaseReference
    lateinit var firebaseUser: FirebaseUser
    private lateinit var profileid: String
    lateinit var stateInfo: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_hospital_detail, container, false)

        var likes1: ImageView = view.findViewById(R.id.likes1)
        var likes2: ImageView = view.findViewById(R.id.likes2)
        var likes3: ImageView = view.findViewById(R.id.likes3)
        var address: TextView = view.findViewById(R.id.address)
        var image: ImageView = view.findViewById(R.id.image)
        var intro: TextView = view.findViewById(R.id.intro)

        hospitalInfo()

        return view
    }

    private fun hospitalInfo() {

        mReference = FirebaseDatabase.getInstance().getReference("Hospital")

        mReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val hospital: Hospital? = dataSnapshot.getValue(Hospital::class.java)
                hospital?.let {
                    toolbar_title.text = hospital.name
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
                    } else {
                        likes1?.setImageResource(R.drawable.ic_like)
                    }
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }
}
