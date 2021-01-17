package com.example.cupet.adapter

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cupet.HomeActivity
import com.example.cupet.R
import com.example.cupet.fragment.HospitalDetailFragment
import com.example.cupet.model.Hospital


class HomeAdapter(val context: Context, val hospitalList: ArrayList<Hospital>): RecyclerView.Adapter<HomeAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hospital_item, parent, false)

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return hospitalList.size
    }

    inner class Holder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        val hospital_name = itemView?.findViewById<TextView>(R.id.hospital_name)
        var address = itemView?.findViewById<TextView>(R.id.address)
        var hospital_img = itemView?.findViewById<ImageView>(R.id.hospital_img)
        var likes1 = itemView?.findViewById<ImageView>(R.id.likes1)
        var likes2 = itemView?.findViewById<ImageView>(R.id.likes2)
        var likes3 = itemView?.findViewById<ImageView>(R.id.likes3)

        fun bind(mHospital: Hospital, context: Context) {
            hospital_name?.text = mHospital.name
            address?.text = mHospital.city + mHospital.state
            hospital_img?.let { Glide.with(context).load(mHospital.image).into(it) }
            if(mHospital.likes == 3) {
                likes1?.setImageResource(R.drawable.ic_like)
                likes2?.setImageResource(R.drawable.ic_like)
                likes3?.setImageResource(R.drawable.ic_like)
            } else if(mHospital.likes == 2) {
                likes1?.setImageResource(R.drawable.ic_like)
                likes2?.setImageResource(R.drawable.ic_like)
            } else if(mHospital.likes == 1) {
                likes1?.setImageResource(R.drawable.ic_like)
            }

            itemView.setOnClickListener {
                var editor: SharedPreferences.Editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                editor.putString("name", mHospital.name)
                editor.apply()

                val fragment = (context as HomeActivity).supportFragmentManager.beginTransaction()
                fragment.replace(R.id.fragment_container, HospitalDetailFragment()).addToBackStack(null).commit()
            }
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(hospitalList[position], context)
    }
}