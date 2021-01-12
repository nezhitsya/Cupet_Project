package com.example.cupet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cupet.R
import com.example.cupet.model.Hospital
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class HomeAdapter(val context: Context, val mHospital: List<Hospital>): RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

//    lateinit var mHospital: List<Hospital>
    lateinit var firebaseUser: FirebaseUser

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var hospital_name: TextView
        var address: TextView
        var hospital_img: ImageView
        var likes1: ImageView
        var likes2: ImageView
        var likes3: ImageView

        init {
            hospital_name = view.findViewById(R.id.hospital_name)
            address = view.findViewById(R.id.address)
            hospital_img = view.findViewById(R.id.hospital_img)
            likes1 = view.findViewById(R.id.likes1)
            likes2 = view.findViewById(R.id.likes2)
            likes3 = view.findViewById(R.id.likes3)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hospital_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        var hospital = mHospital.get(position)

        holder.hospital_name.text = hospital.name
        holder.address.text = hospital.address
        Glide.with(context).load(hospital.image).into(holder.hospital_img)
        if(hospital.likes.equals(3)) {
            holder.likes1.setImageResource(R.drawable.ic_like)
            holder.likes2.setImageResource(R.drawable.ic_like)
            holder.likes3.setImageResource(R.drawable.ic_like)
        } else if(hospital.likes.equals(2)) {
            holder.likes1.setImageResource(R.drawable.ic_like)
            holder.likes2.setImageResource(R.drawable.ic_like)
        } else if(hospital.likes.equals(1)) {
            holder.likes1.setImageResource(R.drawable.ic_like)
        }
    }

    override fun getItemCount() = mHospital.size

//    fun getInfo(imageView: ImageView, textView: TextView) {
//        var reference: DatabaseReference
//        reference = FirebaseDatabase.getInstance().getReference().child("Hospital")
//
//        val postListener = object: ValueEventListener {
//            override fun onDataChange(dataSnapshop: DataSnapshot) {
////                val user = dataSnapshop.getValue<User>()
//            }
//
//            override fun onCancelled(dataSnapshot: DatabaseError) {
//
//            }
//        }
//        reference.addValueEventListener(postListener)
//    }
}