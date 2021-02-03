package com.example.cupet.adapter

import android.content.Context
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cupet.model.Comment
import com.example.cupet.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.DateFormat
import java.text.SimpleDateFormat

class CommentAdapter(val context: Context, val commentList: ArrayList<Comment>): RecyclerView.Adapter<CommentAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    inner class Holder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        var profile = itemView?.findViewById<ImageView>(R.id.profile)
        var nickname = itemView?.findViewById<TextView>(R.id.nickname)
        val comment = itemView?.findViewById<TextView>(R.id.comment)
        var time = itemView?.findViewById<TextView>(R.id.time)
        var option = itemView?.findViewById<Button>(R.id.option)

        fun bind(mComment: Comment, context: Context): Boolean {
            comment?.text = mComment.comment
            var df: DateFormat = SimpleDateFormat("yy.MM.dd  hh:mm")
            time?.text = df.format(mComment.time)
            publisherInfo(profile, nickname, mComment.publisher)

            if(!mComment.publisher?.equals(FirebaseAuth.getInstance().currentUser!!)!!) {
                option!!.visibility = View.GONE
            }

            option?.setOnClickListener{
                val popupMenu = PopupMenu(context, option, Gravity.END)
                popupMenu.menuInflater.inflate(R.menu.post_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                    when(item.itemId) {
                        R.id.edit -> {

                        }
                        R.id.delete -> {

                        }
                        else -> {
                            false
                        }
                    }
                }
                popupMenu.show()
            }
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(commentList[position], context)
    }

    private fun publisherInfo(profile: ImageView?, nickname: TextView?, userid: String?) {
        var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid.toString())

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