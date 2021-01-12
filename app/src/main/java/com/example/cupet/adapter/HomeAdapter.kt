package com.example.cupet.adapter

import android.content.Context
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cupet.R
import com.example.cupet.model.Hospital
import com.example.cupet.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class HomeAdapter(val context: Context, val hospitalList: List<Hospital>, val user: String): RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    lateinit var mHospital: List<Hospital>
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
    }

    override fun getItemCount() = mHospital.size

    fun getInfo(imageView: ImageView, textView: TextView, user: String) {
        var reference: DatabaseReference
        reference = FirebaseDatabase.getInstance().getReference().child("Hospital").child(user)

        val postListener = object: ValueEventListener {
            override fun onDataChange(dataSnapshop: DataSnapshot) {
                val user = dataSnapshop.getValue<User>()
                Glide.with(context).load()
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        }

        reference.addValueEventListener(postListener)
    }
}