package com.example.cupet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cupet.R
import org.w3c.dom.Comment

class CommentAdapter(val context: Context, val commentList: ArrayList<Comment>): RecyclerView.Adapter<CommentAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    inner class Holder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {


        fun bind(mComment: Comment, context: Context) {

        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(commentList[position], context)
    }
}