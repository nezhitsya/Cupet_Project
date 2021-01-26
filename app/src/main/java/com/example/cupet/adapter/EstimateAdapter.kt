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
import com.example.cupet.model.Estimate
import com.example.cupet.model.User
import com.google.firebase.database.*
import java.text.DateFormat
import java.text.SimpleDateFormat

class EstimateAdapter(val context: Context, val estimateList: ArrayList<Estimate>): RecyclerView.Adapter<EstimateAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.estimate_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return estimateList.size
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var profile = itemView?.findViewById<ImageView>(R.id.profile)
        var nickname = itemView?.findViewById<TextView>(R.id.nickname)
        val estimate = itemView?.findViewById<TextView>(R.id.estimate)
        var time = itemView?.findViewById<TextView>(R.id.time)
        var likes1 = itemView?.findViewById<ImageView>(R.id.likes1)
        var likes2 = itemView?.findViewById<ImageView>(R.id.likes2)
        var likes3 = itemView?.findViewById<ImageView>(R.id.likes3)

        fun bind(mEstimate: Estimate, context: Context) {
            estimate?.text = mEstimate.estimate
            var df: DateFormat = SimpleDateFormat("yy.MM.dd  hh:mm")
            time?.text = df.format(mEstimate.time)
            publisherInfo(profile, nickname, mEstimate.publisher)
            if (mEstimate.likes == 3) {
                likes1?.setImageResource(R.drawable.ic_like)
                likes2?.setImageResource(R.drawable.ic_like)
                likes3?.setImageResource(R.drawable.ic_like)
            } else if (mEstimate.likes == 2) {
                likes1?.setImageResource(R.drawable.ic_like)
                likes2?.setImageResource(R.drawable.ic_like)
            } else if (mEstimate.likes == 1) {
                likes1?.setImageResource(R.drawable.ic_like)
            }
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(estimateList[position], context)
    }

    private fun publisherInfo(profile: ImageView?, nickname: TextView?, userid: String?) {
        var reference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(userid.toString())

        reference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user: User? = dataSnapshot.getValue(User::class.java)
                if (user != null) {
                    if (nickname != null) {
                        nickname.text = user.nickname
                    }
                    if (profile != null) {
                        Glide.with(context).load(user.profile).into(profile)
                    }
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }
}
