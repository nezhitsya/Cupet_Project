package com.example.cupet.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cupet.R
import com.example.cupet.model.Post
import com.example.cupet.model.User
import com.google.firebase.database.*


class CommunityAdapter(val context: Context, val postList: ArrayList<Post>): RecyclerView.Adapter<CommunityAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    inner class Holder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        val title = itemView?.findViewById<TextView>(R.id.title)
        var nickname = itemView?.findViewById<TextView>(R.id.nickname)
        var profile = itemView?.findViewById<ImageView>(R.id.profile)
        var time = itemView?.findViewById<TextView>(R.id.time)
        var view_count = itemView?.findViewById<TextView>(R.id.view)
        var comment = itemView?.findViewById<Button>(R.id.comment)

        fun bind(mPost: Post, context: Context) {
            Log.d("DDD", mPost.title)
            title?.text = mPost.title
            time?.text = mPost.time.toString()
            publisherInfo(profile, nickname, mPost.publisher)
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(postList[position], context)
    }

    private fun publisherInfo(profile: ImageView?, nickname: TextView?, userid: String?) {
        var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(
            userid.toString()
        )

        reference.addValueEventListener(object: ValueEventListener {

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