package com.example.cupet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cupet.R
import com.example.cupet.model.Post
import com.example.cupet.model.User

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

        fun bind(mPost: Post, context: Context) {
            title?.text = mPost.title
            time?.text = mPost.time
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(postList[position], context)


    }
}